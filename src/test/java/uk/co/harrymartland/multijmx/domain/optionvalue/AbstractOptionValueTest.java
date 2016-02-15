package uk.co.harrymartland.multijmx.domain.optionvalue;

import org.apache.commons.cli.CommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

public abstract class AbstractOptionValueTest<T extends AbstractOptionValue<K>, K> {

    private CommandLine commandLine = Mockito.mock(CommandLine.class);
    private CommandLineService commandLineService = Mockito.mock(CommandLineService.class);
    private T optionValue;

    @Before
    public void setUp() throws Exception {
        Mockito.when(commandLineService.get()).thenReturn(commandLine);
        optionValue = createOptionValue(commandLineService);
    }


    @Test
    public void testValidateNotSet() throws Exception {
        if (getOptionValue().isRequired()) {
            thenShouldThrowValidationExceptionOnValidation(getRequiredValidation());
        } else {
            thenShouldPassValidation();
        }
    }

    protected String getRequiredValidation() {
        return null;
    }

    protected abstract T createOptionValue(CommandLineService commandLineService);

    protected T getOptionValue() {
        return optionValue;
    }

    protected void givenCommandLineArgument(String commandLineArgument) {
        Mockito.when(commandLine.getOptionValue(getOptionValue().getOption().getOpt())).thenReturn(commandLineArgument);
        Mockito.when(commandLine.hasOption(getOptionValue().getOption().getOpt())).thenReturn(true);
    }

    protected void givenCommandLineArguments(String... commandLineArguments) {
        Mockito.when(commandLine.getOptionValues(getOptionValue().getOption().getOpt())).thenReturn(commandLineArguments);
        Mockito.when(commandLine.hasOption(getOptionValue().getOption().getOpt())).thenReturn(true);
    }

    protected CommandLine getCommandLine() {
        return commandLine;
    }

    protected void thenShouldReturn(K value) {
        Assert.assertEquals(value, getOptionValue().getValue());
    }

    protected void thenShouldPassValidation() {
        try {
            getOptionValue().validate();
        } catch (ValidationException e) {
            Assert.fail("Validation failed, exception thrown: " + e.getMessage());
        }
    }

    protected void thenShouldThrowValidationExceptionOnValidation(String expectedMessage) {
        try {
            getOptionValue().validate();
        } catch (ValidationException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Exception not thrown!");
    }
}
