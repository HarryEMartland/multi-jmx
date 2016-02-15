package uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg;

import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.util.Arrays;
import java.util.List;

public class ConnectionArgOptionValueImplTest extends AbstractOptionValueTest<ConnectionArgOptionValueImpl, List<JMXConnection>> {

    private ConnectionService connectionService;

    @Override
    protected ConnectionArgOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        connectionService = Mockito.mock(ConnectionService.class);
        return new ConnectionArgOptionValueImpl(commandLineService, connectionService);
    }

    @Test
    public void testShouldPassValidationWithOneValidConnection() throws Exception {
        givenCommandLineArguments("connection1");
        givenValidConnections("connection1");
        thenShouldPassValidation();
    }

    @Test
    public void testShouldFailValidationWhenInvalidConnection() throws Exception {
        givenInvalidValidConnections("connectionarg");
        givenCommandLineArguments("connectionarg");
        thenShouldThrowValidationExceptionOnValidation("Invalid connectionarg");
    }

    @Test
    public void testShouldFailValidationWhenInvalidSecondConnection() throws Exception {
        givenInvalidValidConnections("connection2");
        givenValidConnections("connectionarg");
        givenCommandLineArguments("connectionarg", "connection2");
        thenShouldThrowValidationExceptionOnValidation("Invalid connectionarg");
    }

    @Test
    public void testShouldReturnConnections() throws Exception {
        givenCommandLineArguments("connection1", "connection2");

        JMXConnection connection1 = Mockito.mock(JMXConnection.class);
        JMXConnection connection2 = Mockito.mock(JMXConnection.class);

        givenConnectionStringReturnConnection("connection1", connection1);
        givenConnectionStringReturnConnection("connection2", connection2);

        thenShouldReturn(Arrays.asList(connection1, connection2));
    }

    private void givenValidConnections(String... connections) throws ValidationException {
        for (String connection : connections) {
            Mockito.when(connectionService.validate(connection)).thenReturn(true);
        }
    }

    private void givenInvalidValidConnections(String... connections) throws ValidationException {
        for (String connection : connections) {
            Mockito.when(connectionService.validate(connection)).thenThrow(new ValidationException("Invalid connectionarg"));
        }
    }

    private void givenConnectionStringReturnConnection(String connectionString, JMXConnection connection) {
        Mockito.when(connectionService.createConnection(connectionString)).thenReturn(connection);
    }

    @Override
    protected void givenCommandLineArguments(String... commandLineArguments) {
        Mockito.when(getCommandLine().getArgList()).thenReturn(Arrays.asList(commandLineArguments));
    }
}