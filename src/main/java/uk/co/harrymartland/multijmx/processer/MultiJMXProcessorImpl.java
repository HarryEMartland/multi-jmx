package uk.co.harrymartland.multijmx.processer;

import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;

import javax.management.remote.JMXServiceURL;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJAEProcessor {

    @Override
    public void run(MultiJMXOptions multiJMXOptions) {
        ExecutorService executorService = null;

        try {
            final ExecutorService finalExecutorService = executorService = createExecutorService(multiJMXOptions);
            Stream<JMXResponse> objectStream = multiJMXOptions.getUrls().stream()
                    .map(url -> requestObject(finalExecutorService, multiJMXOptions, url))
                    .collect(Collectors.toList())
                    .stream()
                    .map(this::getObject);

            sort(objectStream, multiJMXOptions)
                    .forEach(this::display);
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

    private void display(JMXResponse jmxResponse) {
        System.out.printf("%s\t%s\n", jmxResponse.getDisplay(), jmxResponse.getValue());
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
            e.printStackTrace();//todo logger
            return null;
        }
    }

    private Future<JMXResponse> requestObject(ExecutorService executorService, MultiJMXOptions options, JMXServiceURL url) {
        return executorService.submit(createJmxRunner(options, url));
    }

    private RemoteJmxRunner createJmxRunner(MultiJMXOptions options, JMXServiceURL url) {
        if (options.isAuthenticationRequired()) {
            return new PasswordJmxRunner(url, options.getObjectName(), options.getAttribute(), options.getUsername(), options.getPassword(), url.getHost());
        } else {
            return new RemoteJmxRunner(url, options.getObjectName(), options.getAttribute(), url.getHost());//todo display
        }
    }
}
