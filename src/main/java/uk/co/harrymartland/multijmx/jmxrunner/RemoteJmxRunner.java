package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.ObjectName;
import java.util.Map;

public class RemoteJmxRunner extends AbstractJmxRunner {

    public RemoteJmxRunner(ObjectName objectName, String attribute, JMXConnection jmxConnection) {
        super(objectName, attribute, jmxConnection);
    }

    @Override
    protected Map<String, ?> getEnv() {
        return null;
    }

}
