package uk.co.harrymartland.multijmx.processer;

import com.google.inject.Inject;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.optionvalue.jmxrunner.JMXRunnerOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.order.OrderOptionValue;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJMXProcessor {

    private MaxThreadsOptionValue maxThreadsOptionValue;
    private OrderOptionValue orderOptionValue;
    private JMXRunnerOptionValue jmxRunnerOptionValue;


    @Inject
    public MultiJMXProcessorImpl(MaxThreadsOptionValue maxThreadsOptionValue, OrderOptionValue orderOptionValue, JMXRunnerOptionValue jmxRunnerOptionValue) {
        this.maxThreadsOptionValue = maxThreadsOptionValue;
        this.orderOptionValue = orderOptionValue;
        this.jmxRunnerOptionValue = jmxRunnerOptionValue;
    }

    @Override
    public Stream<JMXConnectionResponse> run() {
        ExecutorService executorService = null;

        try {
            final ExecutorService finalExecutorService = executorService = createExecutorService();
            Stream<JMXConnectionResponse> objectStream = jmxRunnerOptionValue.getValue().stream()
                    .map(finalExecutorService::submit)
                    .collect(Collectors.toList())
                    .stream()
                    .map(this::getObject);

            return sort(objectStream);
        } finally {
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }

    private Stream<JMXConnectionResponse> sort(Stream<JMXConnectionResponse> objectStream) {

        Comparator<JMXConnectionResponse> comparator = orderOptionValue.getValue();
        if (comparator != null) {
            objectStream = objectStream.sorted(comparator);
        }
        return objectStream;
    }


    private ExecutorService createExecutorService() {
        if (maxThreadsOptionValue.getValue() != null) {
            return Executors.newFixedThreadPool(maxThreadsOptionValue.getValue());
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

}
