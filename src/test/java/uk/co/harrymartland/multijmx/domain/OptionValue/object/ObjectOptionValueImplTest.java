package uk.co.harrymartland.multijmx.domain.OptionValue.object;

import org.junit.Assert;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.OptionValue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import javax.management.ObjectName;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectOptionValueImplTest extends AbstractOptionValueTest<ObjectOptionValueImpl, List<ObjectName>> {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";
    private final String IN_VALID_OBJECT_NAME = "java.lang/type=OperatingSystem";

    @Override
    protected ObjectOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new ObjectOptionValueImpl(commandLineService);
    }

    @Override
    protected String getRequiredValidation() {
        return "No object name provided";
    }

    @Test
    public void testShouldNotThrowValidationExceptionWhenObjectNameIsValid() throws Exception {
        givenCommandLineArguments(VALID_OBJECT_NAME);
        try {
            getOptionValue().validate();
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testShouldNotThrowValidationExceptionWhenMultipleObjectNamesAreValid() throws Exception {
        givenCommandLineArguments(VALID_OBJECT_NAME, VALID_OBJECT_NAME);
        try {
            getOptionValue().validate();
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testShouldThrowValidationExceptionWhenObjectNameIsInValid() throws Exception {
        givenCommandLineArguments(IN_VALID_OBJECT_NAME);
        try {
            getOptionValue().validate();
        } catch (ValidationException e) {
            Assert.assertEquals("Could not parse object name: " + IN_VALID_OBJECT_NAME, e.getMessage());
            return;
        }
        Assert.fail("No Exception thrown");
    }

    @Test
    public void testShouldThrowValidationExceptionWhenSecondObjectNameIsInValid() throws Exception {
        givenCommandLineArguments(VALID_OBJECT_NAME, IN_VALID_OBJECT_NAME);
        try {
            getOptionValue().validate();
        } catch (ValidationException e) {
            Assert.assertEquals("Could not parse object name: " + IN_VALID_OBJECT_NAME, e.getMessage());
            return;
        }
        Assert.fail("No Exception thrown");
    }

    @Test
    public void testShouldReturnObjectNameWhenGetValueIsCalled() throws Exception {
        givenCommandLineArguments(VALID_OBJECT_NAME);
        thenShouldReturn(Collections.singletonList(new ObjectName(VALID_OBJECT_NAME)));
    }

    @Test
    public void testShouldReturnObjectNamesWhenGetValueIsCalled() throws Exception {
        givenCommandLineArguments(VALID_OBJECT_NAME, VALID_OBJECT_NAME);
        Assert.assertEquals(Arrays.asList(new ObjectName(VALID_OBJECT_NAME), new ObjectName(VALID_OBJECT_NAME)), getOptionValue().getValue());
    }
}