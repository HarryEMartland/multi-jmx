package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.ObjectName;
import java.util.List;
import java.util.Map;

public class RemoteJmxRunner extends AbstractJmxRunner {

    public RemoteJmxRunner(List<ObjectName> objectNames, List<String> attributes, JMXConnection jmxConnection) {
        super(objectNames, attributes, jmxConnection);
    }

    @Override
    protected Map<String, ?> getEnv() {
        return null;
    }

}
