package uk.co.harrymartland.multijmx.domain.OptionValue.password;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.OptionValue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class PasswordOptionValueImpl extends AbstractSingleOptionValue<String> implements PasswordOptionValue {

    @Inject
    public PasswordOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "All connections must use the same password";
    }

    @Override
    protected String getArg() {
        return "p";
    }

    @Override
    protected String lazyLoadValue() {
        return getStringValue();
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg()).longOpt("password").hasArg().argName("password").desc("Password to connect to JMX server").build();
    }


}
