package uk.co.harrymartland.multijmx.domain.OptionValue.delimiter;

import uk.co.harrymartland.multijmx.domain.OptionValue.AbstractSingleOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class DelimiterOptionValueImplTest extends AbstractSingleOptionValueTest<DelimiterOptionValueImpl, String> {

    @Override
    protected String getMoreThanOneArgumentError() {
        return "Only one delimiter can be provided";
    }

    @Override
    protected DelimiterOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new DelimiterOptionValueImpl(commandLineService);
    }
}