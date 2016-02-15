package uk.co.harrymartland.multijmx.domain.OptionValue.connectionarg;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.OptionValue.AbstractMultiOptionValue;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
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
    protected String getArg() {
        return null;
    }

    @Override
    protected List<JMXConnection> lazyLoadValue() {
        return getCommandLine().getArgList().stream()
                .map(connectionString -> connectionService.createConnection(connectionString))
                .collect(Collectors.toList());
    }

    @Override
    protected boolean hasOption() {
        return getCommandLine().getArgList().size() > 0;
    }

    @Override
    public void validate() throws ValidationException {
        if (!hasOption()) {
            return;
        }

        for (String connectionString : getCommandLine().getArgList()) {
            connectionService.validate(connectionString);
        }
    }

    @Override
    protected Option lazyLoadOption() {
        return null;
    }

}
