package uk.co.harrymartland.multijmx.domain.optionvalue.password;

import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class PasswordOptionValueImplTest extends AbstractOptionValueTest<PasswordOptionValueImpl, String> {

    @Test
    public void testShouldReturnPassword() throws Exception {
        givenCommandLineArgument("password");
        thenShouldReturn("password");
    }

    @Test
    public void testShouldPassValidationWhenNoArgumentSet() throws Exception {
        thenShouldPassValidation();
    }

    @Test
    public void testShouldFailValidationWhenMultipleValuesSet() throws Exception {
        givenCommandLineArguments("p1", "p2");
        thenShouldThrowValidationExceptionOnValidation("All connections must use the same password");
    }

    @Override
    protected PasswordOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new PasswordOptionValueImpl(commandLineService);
    }
}