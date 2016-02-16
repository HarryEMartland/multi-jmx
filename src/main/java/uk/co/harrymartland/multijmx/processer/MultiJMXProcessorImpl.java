package uk.co.harrymartland.multijmx.processer;

import com.google.inject.Inject;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.connection.ConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.order.OrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;
import uk.co.harrymartland.multijmx.service.valueretriever.ValueRetrieverService;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXProcessorImpl implements MultiJMXProcessor {

    private ValueRetrieverService valueRetrieverService;
    private ConnectionOptionValue connectionOptionValue;
    private MaxThreadsOptionValue maxThreadsOptionValue;
    private PasswordOptionValue passwordOptionValue;
    private UserNameOptionValue userNameOptionValue;
    private ObjectOptionValue objectOptionValue;
    private SignatureOptionValue signatureOptionValue;
    private OrderConnectionOptionValue orderConnectionOptionValue;
    private OrderValueOptionValue orderValueOptionValue;
    private ReverseOrderOptionValue reverseOrderOptionValue;
    private OrderOptionValue orderOptionValue;


    @Inject
    public MultiJMXProcessorImpl(ValueRetrieverService valueRetrieverService, ConnectionOptionValue connectionOptionValue, MaxThreadsOptionValue maxThreadsOptionValue, PasswordOptionValue passwordOptionValue, UserNameOptionValue userNameOptionValue, ObjectOptionValue objectOptionValue, SignatureOptionValue signatureOptionValue, OrderConnectionOptionValue orderConnectionOptionValue, OrderValueOptionValue orderValueOptionValue, ReverseOrderOptionValue reverseOrderOptionValue) {
        this.valueRetrieverService = valueRetrieverService;
        this.connectionOptionValue = connectionOptionValue;
        this.maxThreadsOptionValue = maxThreadsOptionValue;
        this.passwordOptionValue = passwordOptionValue;
        this.userNameOptionValue = userNameOptionValue;
        this.objectOptionValue = objectOptionValue;
        this.signatureOptionValue = signatureOptionValue;
        this.orderConnectionOptionValue = orderConnectionOptionValue;
        this.orderValueOptionValue = orderValueOptionValue;
        this.reverseOrderOptionValue = reverseOrderOptionValue;
    }

    @Override
    public Stream<JMXConnectionResponse> run() {
        ExecutorService executorService = null;

        try {
            final ExecutorService finalExecutorService = executorService = createExecutorService();
            Stream<JMXConnectionResponse> objectStream = connectionOptionValue.getValue().stream()
                    .map(connection -> requestObject(finalExecutorService, connection))
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

    private Future<JMXConnectionResponse> requestObject(ExecutorService executorService, JMXConnection connection) {
        return executorService.submit(createJmxRunner(connection));
    }

    private RemoteJmxRunner createJmxRunner(JMXConnection connection) {
        if (userNameOptionValue.getValue() != null && passwordOptionValue.getValue() != null) {
            return new PasswordJmxRunner(signatureOptionValue.getValue(), objectOptionValue.getValue(), connection, userNameOptionValue.getValue(), passwordOptionValue.getValue());
        } else {
            return new RemoteJmxRunner(signatureOptionValue.getValue(), objectOptionValue.getValue(), connection);
        }
    }

}
