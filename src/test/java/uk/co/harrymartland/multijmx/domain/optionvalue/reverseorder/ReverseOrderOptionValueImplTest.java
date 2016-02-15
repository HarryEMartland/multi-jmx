package uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder;

import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class ReverseOrderOptionValueImplTest extends AbstractSingleOptionValueTest<ReverseOrderOptionValueImpl, Boolean> {

    @Override
    protected ReverseOrderOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new ReverseOrderOptionValueImpl(commandLineService);
    }

    @Test
    public void testShouldNotThrowExceptionWhenValueSet() throws Exception {
        givenCommandLineArguments("r");
        thenShouldPassValidation();
    }

    @Test
    public void testShouldReturnTrueWhenValueSet() throws Exception {
        givenCommandLineArguments("true");
        thenShouldReturn(true);
    }

    @Test
    public void testShouldReturnFalseWhenValueSet() throws Exception {
        thenShouldReturn(false);
    }

    @Override
    protected String getMoreThanOneArgumentError() {
        return "Reversed order specified twice";
    }
}