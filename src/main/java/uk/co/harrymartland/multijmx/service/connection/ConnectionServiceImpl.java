package uk.co.harrymartland.multijmx.service.connection;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;

import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

public class ConnectionServiceImpl implements ConnectionService {

    private ConnectorService connectorService;

    @Inject
    public ConnectionServiceImpl(ConnectorService connectorService) {
        this.connectorService = connectorService;
    }

    @Override
    public JMXConnection createConnection(String connectionString) {
        try {
            return doCreateConnection(connectionString);
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate(String connectionString) throws ValidationException {
        try {
            doCreateConnection(connectionString);
            return true;
        } catch (ConnectionException e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    private JMXConnection doCreateConnection(String connectionString) throws ConnectionException {
        try {
            if (NumberUtils.isParsable(connectionString)) {
                String processConnectionString = connectorService.importFrom(Integer.parseInt(connectionString));
                if (StringUtils.isBlank(processConnectionString)) {
                    throw new ConnectionException("No process found for id: " + connectionString);
                }
                return new JMXConnection("Process: " + connectionString, new JMXServiceURL(processConnectionString));
            } else {
                JMXServiceURL jmxServiceURL = new JMXServiceURL(connectionString);
                return new JMXConnection(jmxServiceURL.getHost(), jmxServiceURL);
            }
        } catch (MalformedURLException e) {
            throw new ConnectionException("Invalid url: " + connectionString, e);
        } catch (IOException e) {
            throw new ConnectionException("Cannot find connection for: " + connectionString, e);
        }
    }

    protected static class ConnectionException extends Exception {
        private static final long serialVersionUID = -5419836336711792157L;

        public ConnectionException(String message) {
            super(message);
        }

        public ConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
