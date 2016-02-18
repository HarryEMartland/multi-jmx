package uk.co.harrymartland.multijmx.processer;

import com.google.inject.Inject;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.optionvalue.jmxrunner.JMXRunnerOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.order.OrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.threadpool.ThreadPoolOptionValue;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJMXProcessor {

    private OrderOptionValue orderOptionValue;
    private JMXRunnerOptionValue jmxRunnerOptionValue;
    private ThreadPoolOptionValue threadPoolOptionValue;


    @Inject
    public MultiJMXProcessorImpl(OrderOptionValue orderOptionValue, JMXRunnerOptionValue jmxRunnerOptionValue, ThreadPoolOptionValue threadPoolOptionValue) {
        this.orderOptionValue = orderOptionValue;
        this.jmxRunnerOptionValue = jmxRunnerOptionValue;
        this.threadPoolOptionValue = threadPoolOptionValue;
    }

    @Override
    public Stream<JMXConnectionResponse> run() {

        Stream<JMXConnectionResponse> objectStream = jmxRunnerOptionValue.getValue().stream()
                .map(threadPoolOptionValue.getValue()::submit)
                .collect(Collectors.toList())
                .stream()
                .map(this::getObject);

        return sort(objectStream);
    }

    private Stream<JMXConnectionResponse> sort(Stream<JMXConnectionResponse> objectStream) {

        Comparator<JMXConnectionResponse> comparator = orderOptionValue.getValue();
        if (comparator != null) {
            objectStream = objectStream.sorted(comparator);
        }
        return objectStream;
    }

    private <T> T getObject(Future<T> objectFuture) {
        try {
            return objectFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
