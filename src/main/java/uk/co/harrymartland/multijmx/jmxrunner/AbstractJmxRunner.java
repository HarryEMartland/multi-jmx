package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractJmxRunner implements Callable<JMXResponse> {

    private final List<ObjectName> objectNames;
    private final List<String> attributes;
    private final JMXConnection jmxConnection;

    public AbstractJmxRunner(List<ObjectName> objectNames, List<String> attributes, JMXConnection jmxConnection) {
        this.objectNames = objectNames;
        this.attributes = attributes;
        this.jmxConnection = jmxConnection;
    }

    public JMXResponse call() throws Exception {
        try {
            JMXConnector jmxc = JMXConnectorFactory.connect(jmxConnection.getJmxServiceURL(), getEnv());
            MBeanServerConnection mBeanServerConnection = jmxc.getMBeanServerConnection();

            Iterator<String> attributeIterator = attributes.iterator();
            Iterator<ObjectName> objectNameIterator = objectNames.iterator();

            ObjectName objectName = null;
            List<Comparable> values = new ArrayList<>(attributes.size());

            while (attributeIterator.hasNext()) {

                if (objectNameIterator.hasNext()) {
                    objectName = objectNameIterator.next();
                }
                values.add((Comparable) mBeanServerConnection.getAttribute(objectName, attributeIterator.next()));

            }
            return new JMXResponse(jmxConnection.getDisplay(), values);//todo error per attribute

        } catch (Exception e) {
            return new JMXResponse(jmxConnection.getDisplay(), e);
        }
    }

    protected abstract Map<String, ?> getEnv();

}
