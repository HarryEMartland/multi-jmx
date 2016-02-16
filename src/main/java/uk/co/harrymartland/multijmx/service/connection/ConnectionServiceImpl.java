package uk.co.harrymartland.multijmx.service.connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.management.ConnectorAddressLink;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

public class ConnectionServiceImpl implements ConnectionService {

    @Override
    public JMXConnection createConnection(String connectionString) {
        try {
            if (NumberUtils.isParsable(connectionString)) {
                String s = ConnectorAddressLink.importFrom(Integer.parseInt(connectionString));
                if (StringUtils.isBlank(s)) {
                    throw new RuntimeException("No process found for id: " + connectionString);
                }
                return new JMXConnection("Process: " + connectionString, new JMXServiceURL(s));
            } else {
                JMXServiceURL jmxServiceURL = new JMXServiceURL(connectionString);
                return new JMXConnection(jmxServiceURL.getHost(), jmxServiceURL);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url: " + connectionString, e);
        } catch (IOException e) {
            throw new RuntimeException("Url is not parsable as a number", e);
        }
    }

    @Override
    public boolean validate(String connectionString) throws ValidationException {
//// TODO: 14/02/16
        return true;
    }

}
