package uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractMultiOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

public class ConnectionArgOptionValueImpl extends AbstractMultiOptionValue<List<JMXConnection>> implements ConnectionArgOptionValue {

    private ConnectionService connectionService;

    @Inject
    public ConnectionArgOptionValueImpl(CommandLineService commandLineService, ConnectionService connectionService) {
        super(commandLineService);
        this.connectionService = connectionService;
    }

    @Override
    public String getArg() {
        return null;
    }

    @Override
    protected List<JMXConnection> lazyLoadValue() {
        return getCommandLine().getArgList().stream()
                .map(connectionString -> connectionService.createConnection(connectionString))
                .collect(Collectors.toList());
    }

    @Override
    public int getNumberOfValues() {
        return getCommandLine().getArgList().size();
    }

    @Override
    protected boolean hasOption() {
        return getCommandLine().getArgList().size() > 0;
    }

    @Override
    public boolean validate() throws ValidationException {
        if (hasOption()) {
            for (String connectionString : getCommandLine().getArgList()) {
                connectionService.validate(connectionString);
            }
        }
        return true;
    }

    @Override
    protected Option lazyLoadOption() {
        return null;
    }

}
