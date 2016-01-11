package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.JMXResponse;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractJmxRunner implements Callable<JMXResponse> {

    private final ObjectName objectName;
    private final String attribute;
    private final String display;

    public AbstractJmxRunner(ObjectName objectName, String attribute, String display) {
        this.objectName = objectName;
        this.display = display;
        this.attribute = attribute;
    }

    public JMXResponse call() throws Exception {
        JMXConnector jmxc = JMXConnectorFactory.connect(getUrl(), getEnv());
        MBeanServerConnection mBeanServerConnection = jmxc.getMBeanServerConnection();
        Object value = mBeanServerConnection.getAttribute(objectName, this.attribute);
        return new JMXResponse(display, value.toString());
    }

    protected abstract Map<String, ?> getEnv();

    protected abstract JMXServiceURL getUrl();
}
