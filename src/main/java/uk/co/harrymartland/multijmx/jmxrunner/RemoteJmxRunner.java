package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;

import javax.management.ObjectName;
import java.util.List;
import java.util.Map;

public class RemoteJmxRunner extends AbstractJmxRunner {

    public RemoteJmxRunner(List<JMXValueRetriever> attributes, List<ObjectName> objectNames, JMXConnection jmxConnection, ConnectorService connectorService) {
        super(attributes, objectNames, jmxConnection, connectorService);
    }

    @Override
    protected Map<String, ?> getEnv() {
        return null;
    }

}
