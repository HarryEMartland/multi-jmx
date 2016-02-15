package uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.service.file.FileReaderService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionFileOptionValueImpl extends AbstractSingleOptionValue<List<JMXConnection>> implements ConnectionFileOptionValue {

    private FileReaderService fileReaderService;
    private ConnectionService connectionService;

    private List<String> fromFile;
    private Path path;


    @Inject
    public ConnectionFileOptionValueImpl(CommandLineService commandLineService, FileReaderService fileReaderService, ConnectionService connectionService) {
        super(commandLineService);
        this.fileReaderService = fileReaderService;
        this.connectionService = connectionService;
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Only one file can be used";
    }

    @Override
    public String getArg() {
        return "f";
    }

    @Override
    protected List<JMXConnection> lazyLoadValue() {
        try {
            return loadFromFile().stream().map(connectionService::createConnection).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate() throws ValidationException {
        super.validate();
        if (hasOption() && !fileReaderService.exists(getPath())) {
            throw new ValidationException("Connection file not found: " + getStringValue());
        }

        try {
            for (String connectionString : loadFromFile()) {
                connectionService.validate(connectionString);
            }
        } catch (IOException e) {
            throw new ValidationException("Could not read file: " + getStringValue(), e);
        }

        return true;
    }

    private List<String> loadFromFile() throws IOException {
        if (fromFile == null) {
            if (hasOption()) {
                fromFile = fileReaderService.readFromFile(getPath());
            } else {
                return Collections.emptyList();
            }
        }
        return fromFile;
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("file")
                .hasArg()
                .argName("file path")
                .desc("Read JMX connections from file")
                .build();
    }


    public Path getPath() {
        if (path == null && hasOption()) {
            path = Paths.get(getStringValue());
        }
        return path;
    }
}
