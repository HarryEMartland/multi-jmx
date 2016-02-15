package uk.co.harrymartland.multijmx.domain.optionvalue.connection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg.ConnectionArgOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile.ConnectionFileOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.util.Arrays;

public class ConnectionOptionValueImplTest {

    private CommandLineService commandLineService = Mockito.mock(CommandLineService.class);
    private ConnectionArgOptionValue connectionArgOptionValue = Mockito.mock(ConnectionArgOptionValue.class);
    private ConnectionFileOptionValue connectionFileOptionValue = Mockito.mock(ConnectionFileOptionValue.class);

    private ConnectionOptionValue connectionOptionValue = new ConnectionOptionValueImpl(commandLineService, connectionArgOptionValue, connectionFileOptionValue);

    @Test
    public void testWhenNoConnectionsShouldThrowValidationException() {
        try {
            connectionOptionValue.validate();
        } catch (ValidationException e) {
            Assert.assertEquals("At least one connection must be specified", e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testWhenOnlyArgConnectionsShouldReturnConnections() throws Exception {
        JMXConnection connection1 = createConnection();
        givenArgConnections(connection1);
        givenFileConnections();
        thenShouldReturn(connection1);
    }

    @Test
    public void testWhenOnlyFileConnectionsShouldReturnConnections() throws Exception {
        JMXConnection connection1 = createConnection();
        givenArgConnections(connection1);
        givenFileConnections();
        thenShouldReturn(connection1);
    }

    @Test
    public void testShouldPassValidationWhenOneFileConnection() throws Exception {
        givenFileConnections(createConnection());
        connectionOptionValue.validate();
    }

    @Test
    public void testShouldPassValidationWhenOneArgConnection() throws Exception {
        givenArgConnections(createConnection());
        connectionOptionValue.validate();
    }


    @Test
    public void testWhenFileConnectionsAndArgConnectionsShouldReturnConnections() throws Exception {
        JMXConnection connection1 = createConnection();
        JMXConnection connection2 = createConnection();
        givenArgConnections(connection1);
        givenFileConnections(connection2);
        thenShouldReturn(connection1, connection2);
    }

    private void thenShouldReturn(JMXConnection... connection1) {
        Assert.assertEquals(Arrays.asList(connection1), connectionOptionValue.getValue());
    }

    private JMXConnection createConnection() {
        return Mockito.mock(JMXConnection.class);
    }

    private void givenArgConnections(JMXConnection... connections) {
        Mockito.when(connectionArgOptionValue.getValue()).thenReturn(Arrays.asList(connections));
        Mockito.when(connectionArgOptionValue.getNumberOfValues()).thenReturn(connections.length);
    }

    private void givenFileConnections(JMXConnection... connections) {
        Mockito.when(connectionFileOptionValue.getValue()).thenReturn(Arrays.asList(connections));
        Mockito.when(connectionFileOptionValue.getNumberOfValues()).thenReturn(connections.length);
    }

}