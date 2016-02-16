package uk.co.harrymartland.multijmx.domain.optionvalue.delimiter;

import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class DelimiterOptionValueImplTest extends AbstractSingleOptionValueTest<DelimiterOptionValueImpl, String> {

    @Override
    protected String getMoreThanOneArgumentError() {
        return "Only one delimiter can be provided";
    }

    @Test
    public void testShouldReturnDelimiter() throws Exception {
        givenCommandLineArgument(",");
        thenShouldReturn(",");
    }

    @Test
    public void testShouldReturnDefault() throws Exception {
        thenShouldReturn("\t");
    }

    @Override
    protected DelimiterOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new DelimiterOptionValueImpl(commandLineService);
    }
}