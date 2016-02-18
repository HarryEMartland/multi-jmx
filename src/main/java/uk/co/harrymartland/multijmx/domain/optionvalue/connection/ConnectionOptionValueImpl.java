package uk.co.harrymartland.multijmx.domain.optionvalue.connection;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractMultiOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg.ConnectionArgOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile.ConnectionFileOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectionOptionValueImpl extends AbstractMultiOptionValue<List<JMXConnection>> implements ConnectionOptionValue {

    private ConnectionArgOptionValue connectionArgOptionValue;
    private ConnectionFileOptionValue connectionFileOptionValue;

    @Inject
    public ConnectionOptionValueImpl(CommandLineService commandLineService, ConnectionArgOptionValue connectionArgOptionValue, ConnectionFileOptionValue connectionFileOptionValue) {
        super(commandLineService);
        this.connectionArgOptionValue = connectionArgOptionValue;
        this.connectionFileOptionValue = connectionFileOptionValue;
    }

    @Override
    public String getArg() {
        return null;
    }

    @Override
    public void validate() throws ValidationException {
        if (connectionArgOptionValue.getNumberOfValues() + connectionFileOptionValue.getNumberOfValues() < 1) {
            throw new ValidationException("At least one connection must be specified");
        }
    }

    @Override
    protected List<JMXConnection> lazyLoadValue() {
        return Stream.concat(connectionArgOptionValue.getValue().stream(), connectionFileOptionValue.getValue().stream())
                .collect(Collectors.toList());
    }

    @Override
    protected Option lazyLoadOption() {
        return null;
    }
}
