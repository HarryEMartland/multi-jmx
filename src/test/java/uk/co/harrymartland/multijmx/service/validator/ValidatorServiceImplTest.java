package uk.co.harrymartland.multijmx.service.validator;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class ValidatorServiceImplTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";

    @Inject
    private ValidatorService validatorService;
    @Inject
    private CommandLineService commandLineService;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new ArgumentModule(), new MultiJMXModule());
        injector.injectMembers(this);
    }

    @Test
    public void testShouldThrowExceptionWhenOrderByValueAndConnection() throws Exception {
        createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att1", "-v", "-c", "validConnection");
        assertExceptionThrown("Cannot order by connection and display");
    }

    @Test
    public void testShouldNotThrowExceptionWhenOneObjectAndMultipleAttributes() throws Exception {
        createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att1", "-a", "att2", "-a", "att3", "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldThrowExceptionWhenInvalidAttribute() throws Exception {
        createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att*", "validConnection");
        assertExceptionThrown("Name must not contain special characters: att*");
    }

    @Test
    public void testShouldThrowExceptionWhenInvalidMethod() throws Exception {
        createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att*()", "validConnection");
        assertExceptionThrown("Name must not contain special characters: att*");
    }

    @Test
    public void testShouldThrowExceptionWhenInvalidMethodWithArg() throws Exception {
        createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att*(1)", "validConnection");
        assertExceptionThrown("Name must not contain special characters: att*");
    }

    @Test
    public void testShouldNotThrowExceptionWhenSameNumberOfObjectsAndAttributes() throws Exception {
        createCommandLine("-a", "att1", "-a", "att3", "-a", "att2", "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldThrowExceptionWhenMoreAttributesThanObjects() throws Exception {
        createCommandLine("-a", "att1", "-a", "att1", "-a", "att1", "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "validConnection");
        assertExceptionThrown("Number of objects and signatures must match");
    }

    @Test
    public void testShouldThrowExceptionWhenMoreObjectsThanAttributes() throws Exception {
        createCommandLine("-a", "att1", "-a", "att2", "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "validConnection");
        assertExceptionThrown("Number of objects and signatures must match");
    }

    @Test
    public void testShouldNotThrowExceptionWithOneObjectAndOneAttribute() throws ParseException {
        createCommandLine("-a", "att", "-o", VALID_OBJECT_NAME, "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldAcceptMethodWithNoArgsAsAttribute() throws Exception {
        createCommandLine("-a", "att()", "-o", VALID_OBJECT_NAME, "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldAcceptMethodWithStringArgumentAsAttribute() throws Exception {
        createCommandLine("-a", "att(\"test\")", "-o", VALID_OBJECT_NAME, "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldAcceptMethodWithMultipleStringArgumentAsAttribute() throws Exception {
        createCommandLine("-a", "att(\"test\",\"test2\")", "-o", VALID_OBJECT_NAME, "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldAcceptMethodWithIntegerArgumentAsAttribute() throws Exception {
        createCommandLine("-a", "att(4)", "-o", VALID_OBJECT_NAME, "validConnection");
        assertNoExceptionThrown();
    }

    @Test
    public void testShouldNotAcceptMethodWithInvalidClassAsAttribute() throws Exception {
        createCommandLine("-a", "att(new uk.co.harrymartland.Test())", "-o", VALID_OBJECT_NAME, "validConnection");
        assertExceptionThrown("Incorrect argument parameter: new uk.co.harrymartland.Test()");
    }


    private void createCommandLine(String... args) throws ParseException {
        commandLineService.create(args);
    }

    private void assertNoExceptionThrown() {
        try {
            validatorService.validate();
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void assertExceptionThrown(String message) {
        try {
            validatorService.validate();
        } catch (ValidationException e) {
            Assert.assertEquals(message, e.getMessage());
            return;
        }
        Assert.fail("Exception not thrown");
    }
}