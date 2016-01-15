package uk.co.harrymartland.multijmx.processer;

import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJAEProcessor {


    @Override
    public Stream<JMXResponse> run(MultiJMXOptions multiJMXOptions) {
        ExecutorService executorService = null;

        try {
            final ExecutorService finalExecutorService = executorService = createExecutorService(multiJMXOptions);
            Stream<JMXResponse> objectStream = multiJMXOptions.getUrls().stream()
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

    private Stream<JMXResponse> sort(Stream<JMXResponse> objectStream, MultiJMXOptions multiJMXOptions) {
        if (multiJMXOptions.isOrdered()) {
            Comparator<JMXResponse> comparator = null;
            if (multiJMXOptions.isOrderValue() && multiJMXOptions.isOrderDisplay()) {
                throw new RuntimeException("Cannot order by value and display");//todo create and move to validation object
            } else if (multiJMXOptions.isOrderDisplay()) {
                comparator = new JMXResponse.DisplayComparator();
            } else if (multiJMXOptions.isOrderValue()) {
                comparator = new JMXResponse.ValueComparator();
            }

            if (comparator != null) {
                objectStream = objectStream.sorted(reverseComparator(comparator, multiJMXOptions.isReverseOrder()));
            }
        }
        return objectStream;
    }

    private Comparator<JMXResponse> reverseComparator(Comparator<JMXResponse> comparator, boolean reverse) {
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

    private Future<JMXResponse> requestObject(ExecutorService executorService, MultiJMXOptions options, JMXConnection connection) {
        return executorService.submit(createJmxRunner(options, connection));
    }

    private RemoteJmxRunner createJmxRunner(MultiJMXOptions options, JMXConnection connection) {
        if (options.isAuthenticationRequired()) {
            return new PasswordJmxRunner(options.getObjectName(), options.getAttribute(), connection, options.getUsername(), options.getPassword());
        } else {
            return new RemoteJmxRunner(options.getObjectName(), options.getAttribute(), connection);
        }
    }
}
