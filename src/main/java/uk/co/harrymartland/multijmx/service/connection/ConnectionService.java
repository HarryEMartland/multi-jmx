package uk.co.harrymartland.multijmx.service.connection;

import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.validator.ValidationException;

public interface ConnectionService {

    JMXConnection createConnection(String connectionString);

    boolean validate(String connectionString) throws ValidationException;

}
