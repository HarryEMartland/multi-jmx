package uk.co.harrymartland.multijmx.processer;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.typeable.Typeable;
import uk.co.harrymartland.multijmx.domain.valueretriver.AttributeValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.SpelMethodValueRetriever;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJMXProcessor {

    private ExpressionParser expressionParser;

    @Inject
    public MultiJMXProcessorImpl(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
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
            return new PasswordJmxRunner(createJMXValueRetriever(options.getSignatures(), options.getObjectNames()), connection, options.getUsername(), options.getPassword());
        } else {
            return new RemoteJmxRunner(createJMXValueRetriever(options.getSignatures(), options.getObjectNames()), connection);
        }
    }

    private List<JMXValueRetriever> createJMXValueRetriever(List<String> signatures, List<ObjectName> objectNames) {

        if (objectNames.size() == 1) {
            ObjectName objectName = objectNames.get(0);
            return signatures.stream().map(signature -> createJMXValueRetriever(signature, objectName)).collect(Collectors.toList());
        } else {
            Iterator<ObjectName> objectNameIterator = objectNames.iterator();
            Iterator<String> signatureIterator = signatures.iterator();
            List<JMXValueRetriever> jmxValueRetrievers = new ArrayList<>(objectNames.size());

            while (objectNameIterator.hasNext() && signatureIterator.hasNext()) {
                jmxValueRetrievers.add(createJMXValueRetriever(signatureIterator.next(), objectNameIterator.next()));
            }
            return jmxValueRetrievers;
        }

    }

    //todo refactor
    private JMXValueRetriever createJMXValueRetriever(String signature, ObjectName objectName) {
        if (signature.contains("(")) {


            final int firstOpenBracket = signature.indexOf("(");
            final String methodName = signature.substring(0, firstOpenBracket);
            final String argumentsString = signature.substring(firstOpenBracket + 1, signature.length() - 1);//todo assert no brakcets and ends with bracket
            final String[] arguments = StringUtils.split(argumentsString, ",");
            final Typeable[] argumentValues = new Typeable[arguments.length];

            for (int i = 0; i < arguments.length; i++) {
                String argument = arguments[i];
                argumentValues[i] = (Typeable) expressionParser.parseExpression(argument).getValue();

            }
            return new SpelMethodValueRetriever(objectName, methodName, argumentValues);

        } else {
            return new AttributeValueRetriever(objectName, signature);
        }
    }
}
