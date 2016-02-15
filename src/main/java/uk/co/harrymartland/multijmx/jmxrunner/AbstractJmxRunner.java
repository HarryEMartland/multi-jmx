package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractJmxRunner implements Callable<JMXConnectionResponse> {

    private final List<JMXValueRetriever> jmxValueRetrievers;
    private final List<ObjectName> objectNames;
    private final JMXConnection jmxConnection;

    public AbstractJmxRunner(List<JMXValueRetriever> jmxValueRetrievers, List<ObjectName> objectNames, JMXConnection jmxConnection) {
        this.jmxValueRetrievers = jmxValueRetrievers;
        this.objectNames = objectNames;
        this.jmxConnection = jmxConnection;
    }

    public JMXConnectionResponse call() throws Exception {//todo refactor
        try {
            JMXConnector jmxc = JMXConnectorFactory.connect(jmxConnection.getJmxServiceURL(), getEnv());
            MBeanServerConnection mBeanServerConnection = jmxc.getMBeanServerConnection();

            Iterator<JMXValueRetriever> jmxValueRetrieverIterator = jmxValueRetrievers.iterator();
            Iterator<ObjectName> objectNameIterator = objectNames.iterator();

            List<JMXValueResult> values = new ArrayList<>(jmxValueRetrievers.size());
            ObjectName objectName = objectNameIterator.next();
            while (jmxValueRetrieverIterator.hasNext()) {

                try {
                    values.add(new JMXValueResult(jmxValueRetrieverIterator.next().getValue(mBeanServerConnection, objectName)));
                } catch (Exception e) {
                    values.add(new JMXValueResult(e));
                }

                if (objectNameIterator.hasNext()) {
                    objectName = objectNameIterator.next();
                }
            }
            return new JMXConnectionResponse(jmxConnection.getDisplay(), values);

        } catch (Exception e) {
            return new JMXConnectionResponse(jmxConnection.getDisplay(), e);
        }
    }

    protected abstract Map<String, ?> getEnv();

}
