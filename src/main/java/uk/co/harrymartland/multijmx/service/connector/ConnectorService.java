package uk.co.harrymartland.multijmx.service.connector;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Map;

public interface ConnectorService {
    JMXConnector connect(JMXServiceURL serviceURL, Map<String, ?> environment) throws IOException;
}
