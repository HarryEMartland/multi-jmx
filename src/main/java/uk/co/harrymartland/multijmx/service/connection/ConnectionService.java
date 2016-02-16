package uk.co.harrymartland.multijmx.service.connection;

import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

public interface ConnectionService {

    JMXConnection createConnection(String connectionString);

    boolean validate(String connectionString) throws ValidationException;

}
