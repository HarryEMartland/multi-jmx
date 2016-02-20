package uk.co.harrymartland.multijmx.jmxrunner;

import com.google.common.collect.Iterables;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class AbstractJmxRunner implements Callable<JMXConnectionResponse> {

    private final List<JMXValueRetriever> jmxValueRetrievers;
    private final List<ObjectName> objectNames;
    private final JMXConnection jmxConnection;
    private final ConnectorService connectorService;

    public AbstractJmxRunner(List<JMXValueRetriever> jmxValueRetrievers, List<ObjectName> objectNames, JMXConnection jmxConnection, ConnectorService connectorService) {
        this.jmxValueRetrievers = jmxValueRetrievers;
        this.objectNames = objectNames;
        this.jmxConnection = jmxConnection;
        this.connectorService = connectorService;
    }

    public JMXConnectionResponse call() throws Exception {
        try {

            MBeanServerConnection mBeanServerConnection = connectorService.
                    connect(jmxConnection.getJmxServiceURL(), getEnv()).getMBeanServerConnection();

            Iterator<ObjectName> objectNameIterator = Iterables.cycle(objectNames).iterator();
            List<JMXValueResult> values = jmxValueRetrievers.stream().map(jmxValueRetriever -> {
                try {
                    return new JMXValueResult(jmxValueRetriever.getValue(mBeanServerConnection, objectNameIterator.next()));
                } catch (Exception e) {
                    return new JMXValueResult(e);
                }
            }).collect(Collectors.toList());

            return new JMXConnectionResponse(jmxConnection.getDisplay(), values);
        } catch (Exception e) {
            return new JMXConnectionResponse(jmxConnection.getDisplay(), e);
        }
    }

    protected abstract Map<String, ?> getEnv();

}
