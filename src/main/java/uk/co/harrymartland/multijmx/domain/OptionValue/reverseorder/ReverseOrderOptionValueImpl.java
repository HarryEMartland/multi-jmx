package uk.co.harrymartland.multijmx.domain.OptionValue.reverseorder;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.OptionValue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class ReverseOrderOptionValueImpl extends AbstractSingleOptionValue<Boolean> implements ReverseOrderOptionValue {

    private CommandLineService commandLineService;

    @Inject
    public ReverseOrderOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getArg() {
        return "r";
    }

    @Override
    protected Boolean lazyLoadValue() {
        return hasOption();
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("reverse-order")
                .desc("Order the results in reverse")
                .build();
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Reversed order specified twice";
    }

}
