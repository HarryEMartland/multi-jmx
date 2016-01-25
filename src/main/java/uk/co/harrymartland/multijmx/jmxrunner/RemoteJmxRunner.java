package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import java.util.List;
import java.util.Map;

public class RemoteJmxRunner extends AbstractJmxRunner {

    public RemoteJmxRunner(List<JMXValueRetriever> attributes, JMXConnection jmxConnection) {
        super(attributes, jmxConnection);
    }

    @Override
    protected Map<String, ?> getEnv() {
        return null;
    }

}
