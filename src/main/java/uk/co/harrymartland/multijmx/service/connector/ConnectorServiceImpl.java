package uk.co.harrymartland.multijmx.service.connector;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Map;

public class ConnectorServiceImpl implements ConnectorService {
    @Override
    public JMXConnector connect(JMXServiceURL serviceURL, Map<String, ?> environment) throws IOException {
        return JMXConnectorFactory.connect(serviceURL, environment);
    }
}
