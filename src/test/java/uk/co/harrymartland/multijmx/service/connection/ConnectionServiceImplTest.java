package uk.co.harrymartland.multijmx.service.connection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.harrymartland.multijmx.ExceptionUtils;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;

import javax.management.remote.JMXServiceURL;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionServiceImplTest {

    private static final String VALID_PROCESS_CONNECTION = "service:jmx:rmi://127.0.0.1/stub/rO0ABXNyAC5qYXZheC5tYW5hZ2VtZW50LnJlbW90ZS5ybWkuUk1JU2VydmVySW1wbF9TdHViAAAAAAAAAAICAAB4cgAaamF2YS5ybWkuc2VydmVyLlJlbW90ZVN0dWLp/tzJi+FlGgIAAHhyABxqYXZhLnJtaS5zZXJ2ZXIuUmVtb3RlT2JqZWN002G0kQxhMx4DAAB4cHc0AAtVbmljYXN0UmVmMgAACWxvY2FsaG9zdAAAovv9TYQQJYa4e0qZwuwAAAFTChjF+oAOAHg=";
    private static final String VALID_URL_CONNECTION = "service:jmx:rmi://test1/jndi/rmi://:9999/jmxrmi";

    @Mock
    private ConnectorService connectorService;
    @InjectMocks
    private ConnectionService connectionService = new ConnectionServiceImpl(connectorService);

    @Test
    public void testShouldPassValidationUsingProcessId() throws Exception {
        givenProcessIdReturn(23, VALID_PROCESS_CONNECTION);
        thenShouldPassValidation("23");
        thenShouldReturnConnection("23", "Process: 23", new JMXServiceURL(VALID_PROCESS_CONNECTION));
    }

    //todo readme example url connection and process connection
    @Test
    public void testShouldFailValidationWithInvalidProcessId() throws Exception {
        thenShouldFailValidation("23", "No process found for id: 23");
    }

    @Test
    public void testShouldPassValidationWithValidUrl() throws Exception {
        thenShouldPassValidation(VALID_URL_CONNECTION);
        thenShouldReturnConnection(VALID_URL_CONNECTION, "test1", new JMXServiceURL(VALID_URL_CONNECTION));
    }

    @Test
    public void testShouldFailValidationWithInvalidUrl() throws Exception {
        thenShouldFailValidation("http://google.com", "Invalid url: http://google.com");
    }

    @Test
    public void testShouldFailValidationWhenImportConnectionFails() throws Exception {
        Mockito.when(connectorService.importFrom(67)).thenThrow(new IOException());
        thenShouldFailValidation("67", "Cannot find connection for: 67");
    }

    @Test(expected = RuntimeException.class)
    public void testShouldThrowRuntimeExceptionWhenInvalidUrl() throws Exception {
        connectionService.createConnection("invalid");
    }

    private void thenShouldReturnConnection(String connectionString, String display, JMXServiceURL url) {
        JMXConnection connection = connectionService.createConnection(connectionString);
        Assert.assertEquals(display, connection.getDisplay());
        Assert.assertEquals(url, connection.getJmxServiceURL());
    }

    private void thenShouldFailValidation(String connectionString, String errorMessage) {
        try {
            connectionService.validate(connectionString);
        } catch (ValidationException e) {
            Assert.assertEquals(errorMessage, e.getMessage());
            return;
        }
        Assert.fail("Validation passed");
    }

    private void thenShouldPassValidation(String connectionString) throws ValidationException {
        connectionService.validate(connectionString);
    }

    private void givenProcessIdReturn(int processId, String connectionString) {
        try {
            Mockito.when(connectorService.importFrom(Mockito.eq(processId))).thenReturn(connectionString);
        } catch (IOException e) {
            ExceptionUtils.eat(e);
        }
    }
}