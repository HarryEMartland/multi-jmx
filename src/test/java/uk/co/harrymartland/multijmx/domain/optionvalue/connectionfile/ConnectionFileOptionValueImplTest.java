package uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.service.file.FileReaderService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConnectionFileOptionValueImplTest extends AbstractSingleOptionValueTest<ConnectionFileOptionValueImpl, List<JMXConnection>> {

    private FileReaderService fileReaderService = Mockito.mock(FileReaderService.class);
    private ConnectionService connectionService = Mockito.mock(ConnectionService.class);

    @Override
    protected String getMoreThanOneArgumentError() {
        return "Only one file can be used";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.when(fileReaderService.exists(Mockito.any(Path.class))).thenReturn(false);
    }

    @Test
    public void testShouldReturnFile() throws Exception {
        JMXConnection connection1 = Mockito.mock(JMXConnection.class);
        givenCommandLineArgument("test");
        givenFileValue(Paths.get("test"), "connection1");
        givenConnectionString("connection1", connection1);
        thenShouldReturn(Collections.singletonList(connection1));
    }

    @Test
    public void testShouldThrowValidationExceptionWhenFileNotExists() throws Exception {
        givenCommandLineArgument("test");
        thenShouldThrowValidationExceptionOnValidation("Connection file not found: test");
    }

    @Test
    public void testShouldPassValidationWithExistingFile() throws Exception {
        givenCommandLineArgument("file");
        givenFileValue(Paths.get("file"));
        thenShouldPassValidation();
    }

    @Test
    public void testShouldFailValidationWhenInvalidConnection() throws Exception {
        givenCommandLineArgument("file");
        givenFileValue(Paths.get("file"), "valid", "invalid");
        givenConnectionString("valid", Mockito.mock(JMXConnection.class));
        givenInvalidConnectionString("invalid");
        thenShouldThrowValidationExceptionOnValidation("Invalid Connection");
    }

    @Override
    protected ConnectionFileOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new ConnectionFileOptionValueImpl(commandLineService, fileReaderService, connectionService);
    }

    private void givenConnectionString(String connectionString, JMXConnection connection) {
        Mockito.when(connectionService.createConnection(connectionString)).thenReturn(connection);
    }

    private void givenInvalidConnectionString(String connectionString) throws ValidationException {
        Mockito.when(connectionService.validate(connectionString)).thenThrow(new ValidationException("Invalid Connection"));
    }

    private void givenFileValue(Path path, String... fileValues) throws IOException {
        Mockito.when(fileReaderService.readFromFile(path)).thenReturn(Arrays.asList(fileValues));
        Mockito.when(fileReaderService.exists(path)).thenReturn(true);
    }
}