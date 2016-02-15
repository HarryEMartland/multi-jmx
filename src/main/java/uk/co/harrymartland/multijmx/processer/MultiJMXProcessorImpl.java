package uk.co.harrymartland.multijmx.processer;

import com.google.inject.Inject;
import org.springframework.expression.ExpressionParser;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;
import uk.co.harrymartland.multijmx.service.valueretriever.ValueRetrieverService;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJMXProcessor {

    private ExpressionParser expressionParser;
    private ValueRetrieverService valueRetrieverService;

    @Inject
    public MultiJMXProcessorImpl(ExpressionParser expressionParser, ValueRetrieverService valueRetrieverService) {
        this.expressionParser = expressionParser;
        this.valueRetrieverService = valueRetrieverService;
    }

    @Override
    public Stream<JMXConnectionResponse> run(MultiJMXOptions multiJMXOptions) {
        ExecutorService executorService = null;

        try {
            final ExecutorService finalExecutorService = executorService = createExecutorService(multiJMXOptions);
            Stream<JMXConnectionResponse> objectStream = multiJMXOptions.getUrls().stream()
                    .map(connection -> requestObject(finalExecutorService, multiJMXOptions, connection))
                    .collect(Collectors.toList())
                    .stream()
                    .map(this::getObject);

            return sort(objectStream, multiJMXOptions);
        } finally {
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }

    private Stream<JMXConnectionResponse> sort(Stream<JMXConnectionResponse> objectStream, MultiJMXOptions multiJMXOptions) {
        if (multiJMXOptions.isOrdered()) {
            Comparator<JMXConnectionResponse> comparator = null;

            if (multiJMXOptions.isOrderConnection()) {
                comparator = new JMXConnectionResponse.DisplayComparator();
            }
            if (multiJMXOptions.isOrderValue()) {
                comparator = new JMXConnectionResponse.ValueComparator();
            }

            if (comparator != null) {
                objectStream = objectStream.sorted(reverseComparator(comparator, multiJMXOptions.isReverseOrder()));
            }
        }
        return objectStream;
    }

    private Comparator<JMXConnectionResponse> reverseComparator(Comparator<JMXConnectionResponse> comparator, boolean reverse) {
        if (reverse) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private ExecutorService createExecutorService(MultiJMXOptions options) {
        if (options.isThreadsLimited()) {
            return Executors.newFixedThreadPool(options.getMaxThreads());
        } else {
            return Executors.newCachedThreadPool();
        }
    }

    private <T> T getObject(Future<T> objectFuture) {
        try {
            return objectFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Future<JMXConnectionResponse> requestObject(ExecutorService executorService, MultiJMXOptions options, JMXConnection connection) {
        return executorService.submit(createJmxRunner(options, connection));
    }

    private RemoteJmxRunner createJmxRunner(MultiJMXOptions options, JMXConnection connection) {
        if (options.isAuthenticationRequired()) {
            return new PasswordJmxRunner(createJMXValueRetrievers(options.getSignatures()), options.getObjectNames(), connection, options.getUsername(), options.getPassword());
        } else {
            return new RemoteJmxRunner(createJMXValueRetrievers(options.getSignatures()), options.getObjectNames(), connection);
        }
    }

    private List<JMXValueRetriever> createJMXValueRetrievers(List<String> signatures) {
        return signatures.stream().map(signature -> valueRetrieverService.createRetriever(signature)).collect(Collectors.toList());
    }

}
