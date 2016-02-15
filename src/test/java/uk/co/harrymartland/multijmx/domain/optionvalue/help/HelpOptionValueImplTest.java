package uk.co.harrymartland.multijmx.domain.optionvalue.help;

import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class HelpOptionValueImplTest extends AbstractSingleOptionValueTest<HelpOptionValueImpl, Boolean> {

    @Test
    public void testShouldReturnFalseWhenArgumentNotSet() throws Exception {
        thenShouldReturn(false);
    }

    @Test
    public void testShouldReturnTrueWhenArgumentSet() throws Exception {
        givenCommandLineArgument("");
        thenShouldReturn(true);
    }

    @Test
    public void testShouldValidateWhenNoArgumentSet() throws Exception {
        thenShouldPassValidation();
    }

    @Test
    public void testShouldValidateWhenArgumentSet() throws Exception {
        givenCommandLineArgument("");
        thenShouldPassValidation();
    }

    @Override
    protected String getMoreThanOneArgumentError() {
        return "Help option specified twice";
    }

    @Override
    protected HelpOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new HelpOptionValueImpl(commandLineService);
    }
}