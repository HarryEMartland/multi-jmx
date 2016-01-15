package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractJmxRunner implements Callable<JMXResponse> {

    private final ObjectName objectName;
    private final String attribute;
    private final JMXConnection jmxConnection;

    public AbstractJmxRunner(ObjectName objectName, String attribute, JMXConnection jmxConnection) {
        this.objectName = objectName;
        this.attribute = attribute;
        this.jmxConnection = jmxConnection;
    }

    public JMXResponse call() throws Exception {
        try {
            JMXConnector jmxc = JMXConnectorFactory.connect(jmxConnection.getJmxServiceURL(), getEnv());
            MBeanServerConnection mBeanServerConnection = jmxc.getMBeanServerConnection();
            Object value = mBeanServerConnection.getAttribute(objectName, this.attribute);
            return new JMXResponse(jmxConnection.getDisplay(), value.toString());
        } catch (Exception e) {
            return new JMXResponse(jmxConnection.getDisplay(), e);
        }
    }

    protected abstract Map<String, ?> getEnv();

}
