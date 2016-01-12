package uk.co.harrymartland.multijmx;

import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;

import javax.management.remote.JMXServiceURL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class MultiJMX {

    public void run(MultiJMXOptions multiJMXOptions) {

        ExecutorService executorService = createExecutorService(multiJMXOptions);

        //todo execute in parallel
        Stream<JMXResponse> objectStream = multiJMXOptions.getUrls().stream()
                .map(url -> requestObject(executorService, multiJMXOptions, url))
                .map(this::getObject);

        objectStream = sort(objectStream, multiJMXOptions);
        objectStream.forEach(this::display);

        //todo close in finally
        executorService.shutdownNow();
    }

    private Stream<JMXResponse> sort(Stream<JMXResponse> objectStream, MultiJMXOptions multiJMXOptions) {
        if (multiJMXOptions.isOrdered()) {
            if (multiJMXOptions.isOrderValue() && multiJMXOptions.isOrderDisplay()) {
                throw new RuntimeException("Cannot order by value and display");
            } else if (multiJMXOptions.isOrderDisplay()) {
                return objectStream.sorted(new JMXResponse.DisplayComparator());
            } else if (multiJMXOptions.isOrderValue()) {
                return objectStream.sorted(new JMXResponse.ValueComparator());
            }
        }
        return objectStream;
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
