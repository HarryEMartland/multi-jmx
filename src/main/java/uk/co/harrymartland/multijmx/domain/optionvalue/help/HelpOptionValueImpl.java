package uk.co.harrymartland.multijmx.domain.optionvalue.help;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class HelpOptionValueImpl extends AbstractSingleOptionValue<Boolean> implements HelpOptionValue {

    @Inject
    public HelpOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Help option specified twice";
    }

    @Override
    public String getArg() {
        return "h";
    }

    @Override
    protected Boolean lazyLoadValue() {
        return hasOption();
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("help")
                .hasArg(false)
                .desc("Display help")
                .build();
    }
}
