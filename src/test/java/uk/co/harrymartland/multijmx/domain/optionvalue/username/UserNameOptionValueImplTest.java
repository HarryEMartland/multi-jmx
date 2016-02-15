package uk.co.harrymartland.multijmx.domain.optionvalue.username;

import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class UserNameOptionValueImplTest extends AbstractOptionValueTest<UserNameOptionValueImpl, String> {

    @Test
    public void testShouldReturnUserName() throws Exception {
        givenCommandLineArgument("username");
        thenShouldReturn("username");
    }

    @Test
    public void testShouldPassValidationWhenNotSet() throws Exception {
        thenShouldPassValidation();
    }

    @Test
    public void testShouldNotAllowMultipleUserNames() throws Exception {
        givenCommandLineArguments("user1", "user2");
        thenShouldThrowValidationExceptionOnValidation("Connections must use the same username");
    }

    @Override
    protected UserNameOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new UserNameOptionValueImpl(commandLineService);
    }
}