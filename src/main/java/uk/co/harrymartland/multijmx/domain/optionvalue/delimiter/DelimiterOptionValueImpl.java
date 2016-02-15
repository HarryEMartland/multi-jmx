package uk.co.harrymartland.multijmx.domain.optionvalue.delimiter;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class DelimiterOptionValueImpl extends AbstractSingleOptionValue<String> implements DelimiterOptionValue {

    @Inject
    public DelimiterOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Only one delimiter can be provided";
    }

    @Override
    public String getArg() {
        return "d";
    }

    @Override
    protected String lazyLoadValue() {
        return getStringValue();
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("delimiter").
                        hasArg().numberOfArgs(1)
                .argName("delimiter")
                .desc("Delimiter used to split results")
                .build();
    }
}
