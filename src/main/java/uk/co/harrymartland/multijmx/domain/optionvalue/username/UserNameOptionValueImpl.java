package uk.co.harrymartland.multijmx.domain.optionvalue.username;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class UserNameOptionValueImpl extends AbstractSingleOptionValue<String> implements UserNameOptionValue {

    @Inject
    public UserNameOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Connections must use the same username";
    }

    @Override
    protected String getArg() {
        return "u";
    }

    @Override
    protected String lazyLoadValue() {
        return getStringValue();
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("username")
                .hasArg()
                .argName("username")
                .desc("Username to connect to JMX server")
                .build();
    }

}
