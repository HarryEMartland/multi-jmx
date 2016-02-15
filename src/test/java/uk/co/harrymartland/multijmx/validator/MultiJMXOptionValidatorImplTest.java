package uk.co.harrymartland.multijmx.validator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.service.connection.ConnectionServiceImpl;

public class MultiJMXOptionValidatorImplTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";

    private MultiJMXOptionValidator multiJMXOptionValidator = new MultiJMXOptionValidatorImpl(new SpelExpressionParser());
    private MultiJMXArgumentParser multiJMXArgumentParser = new MultiJMXArgumentParserImpl(new ConnectionServiceImpl());

    @Test
    public void testShouldThrowExceptionWhenOrderByValueAndConnection() throws Exception {
        assertExceptionThrown(createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att1", "-v", "-c"),
                "Cannot order by connectionarg and display");
    }

    @Test
    public void testShouldNotThrowExceptionWhenOneObjectAndMultipleAttributes() throws Exception {
        assertNoExceptionThrown(createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att1", "-a", "att2", "-a", "att3"));
    }

    @Test
    public void testShouldThrowExceptionWhenInvalidAttribute() throws Exception {
        assertExceptionThrown(createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att*"), "Name must not contain special characters: att*");
    }

    @Test
    public void testShouldThrowExceptionWhenInvalidMethod() throws Exception {
        assertExceptionThrown(createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att*()"), "Name must not contain special characters: att*");
    }

    @Test
    public void testShouldThrowExceptionWhenInvalidMethodWithArg() throws Exception {
        assertExceptionThrown(createCommandLine("-o", VALID_OBJECT_NAME, "-a", "att*(1)"), "Name must not contain special characters: att*");
    }

    @Test
    public void testShouldNotThrowExceptionWhenSameNumberOfObjectsAndAttributes() throws Exception {
        assertNoExceptionThrown(createCommandLine("-a", "att1", "-a", "att3", "-a", "att2", "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME));
    }

    @Test
    public void testShouldThrowExceptionWhenMoreAttributesThanObjects() throws Exception {
        assertExceptionThrown(createCommandLine("-a", "att1", "-a", "att1", "-a", "att1", "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME),
                "Number of attributes and objects must match");
    }

    @Test
    public void testShouldThrowExceptionWhenMoreObjectsThanAttributes() throws Exception {
        assertExceptionThrown(createCommandLine("-a", "att1", "-a", "att2", "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME, "-o", VALID_OBJECT_NAME),
                "Number of attributes and objects must match");
    }

    @Test
    public void testShouldNotThrowExceptionWithOneObjectAndOneAttribute() throws ParseException {
        assertNoExceptionThrown(createCommandLine("-a", "att", "-o", VALID_OBJECT_NAME));
    }

    @Test
    public void testShouldAcceptMethodWithNoArgsAsAttribute() throws Exception {
        assertNoExceptionThrown(createCommandLine("-a", "att()", "-o", VALID_OBJECT_NAME));
    }

    @Test
    public void testShouldAcceptMethodWithStringArgumentAsAttribute() throws Exception {
        assertNoExceptionThrown(createCommandLine("-a", "att(\"test\")", "-o", VALID_OBJECT_NAME));
    }

    @Test
    public void testShouldAcceptMethodWithMultipleStringArgumentAsAttribute() throws Exception {
        assertNoExceptionThrown(createCommandLine("-a", "att(\"test\",\"test2\")", "-o", VALID_OBJECT_NAME));
    }

    @Test
    public void testShouldAcceptMethodWithIntegerArgumentAsAttribute() throws Exception {
        assertNoExceptionThrown(createCommandLine("-a", "att(4)", "-o", VALID_OBJECT_NAME));
    }

    @Test
    public void testShouldNotAcceptMethodWithInvalidClassAsAttribute() throws Exception {
        assertExceptionThrown(createCommandLine("-a", "att(new uk.co.harrymartland.Test())", "-o", VALID_OBJECT_NAME), "Incorrect argument parameter: new uk.co.harrymartland.Test()");
    }


    private CommandLine createCommandLine(String... args) throws ParseException {
        return new DefaultParser().parse(multiJMXArgumentParser.getOptions(), args);
    }

    private void assertNoExceptionThrown(CommandLine commandLine) {
        try {
            multiJMXOptionValidator.validate(commandLine);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void assertExceptionThrown(CommandLine multiJMXOptions, String message) {
        try {
            multiJMXOptionValidator.validate(multiJMXOptions);
        } catch (ValidationException e) {
            Assert.assertEquals(message, e.getMessage());
            return;
        }
        Assert.fail("Exception not thrown");
    }
}