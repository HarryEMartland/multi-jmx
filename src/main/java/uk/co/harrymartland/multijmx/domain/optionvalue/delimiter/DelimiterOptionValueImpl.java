package uk.co.harrymartland.multijmx.domain.optionvalue.delimiter;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.util.Optional;

public class DelimiterOptionValueImpl extends AbstractSingleOptionValue<String> implements DelimiterOptionValue {

    private static final String DEFAULT_DELIMITER = "\t";

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
        return Optional.ofNullable(getStringValue()).orElse(DEFAULT_DELIMITER);
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
