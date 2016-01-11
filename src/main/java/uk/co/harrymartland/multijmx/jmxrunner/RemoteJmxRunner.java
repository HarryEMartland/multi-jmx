package uk.co.harrymartland.multijmx.jmxrunner;

import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.util.Map;

public class RemoteJmxRunner extends AbstractJmxRunner {

    private final JMXServiceURL url;

    public RemoteJmxRunner(JMXServiceURL url, ObjectName objectName, String attribute, String display) {
        super(objectName, attribute, display);
        this.url = url;
    }

    @Override
    protected Map<String, ?> getEnv() {
        return null;
    }

    @Override
    protected JMXServiceURL getUrl() {
        return url;
    }
}
