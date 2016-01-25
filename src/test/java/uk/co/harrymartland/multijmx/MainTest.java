package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.processer.MultiJAEProcessor;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidator;
import uk.co.harrymartland.multijmx.validator.ValidationException;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.Writer;

import java.io.IOException;
import java.util.Arrays;

import static uk.co.harrymartland.multijmx.ExceptionUtils.getStackTrace;

public class MainTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";
    private final String[] VALID_ARGS = {"-o", VALID_OBJECT_NAME, "-a", "att"};

    private Main main = new Main();

    @Test
    public void testIsDisplayHelpNoArguments() throws Exception {
        Assert.assertTrue(main.isDisplayHelp(new String[0]));
    }

    @Test
    public void testIsDisplayHelpHelpShortOption() throws Exception {
        Assert.assertTrue(main.isDisplayHelp(new String[]{"-h"}));
    }

    @Test
    public void testIsDisplayHelpHelpLongOption() throws Exception {
        Assert.assertTrue(main.isDisplayHelp(new String[]{"-help"}));
    }

    @Test
    public void testIsDisplayHelpAttributeOption() throws Exception {
        Assert.assertFalse(main.isDisplayHelp(new String[]{"-a", "attribute"}));
    }

    @Test
    public void testIsDisplayHelpAttributeAndHelpOption() throws Exception {
        Assert.assertTrue(main.isDisplayHelp(new String[]{"-a", "attribute", "-h"}));
    }

    @Test
    public void testIsDisplayHelpHelpAndAttributeOption() throws Exception {
        Assert.assertTrue(main.isDisplayHelp(new String[]{"-h", "-a", "attribute"}));
    }

    @Test
    public void testShouldDisplayResult() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        new Main().run(createMultiArgumentParser(multiJMXOptions),
                createMultiJmxProcessor(
                        createConnectionResponse("connection", createValueResult(3))
                ), new NoValidationValidator(), retreivableWriter, new NoWait(), VALID_ARGS);

        Assert.assertEquals("connection 3\n", retreivableWriter.getValue());

    }

    @Test
    public void testShouldDisplayResultExceptionAndResult() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        Exception exception = new Exception("exception message");


        new Main().run(createMultiArgumentParser(multiJMXOptions),
                createMultiJmxProcessor(
                        createConnectionResponse("connection", createValueResult(4), createValueResult(exception))
                ), new NoValidationValidator(), retreivableWriter, new NoWait(), VALID_ARGS);

        Assert.assertEquals("connection 4 exception message\nErrors have occurred (1)\nPress enter to see next stack trace\n" + getStackTrace(exception) + "\n", retreivableWriter.getValue());

    }

    @Test
    public void testShouldDisplayMultipleResults() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        new Main().run(createMultiArgumentParser(multiJMXOptions),
                createMultiJmxProcessor(
                        createConnectionResponse("connection", createValueResult(3), createValueResult(4))
                ), new NoValidationValidator(), retreivableWriter, new NoWait(), VALID_ARGS);

        Assert.assertEquals("connection 3 4\n", retreivableWriter.getValue());

    }

    @Test
    public void testShouldDisplayResultsForMultipleConnections() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        new Main().run(createMultiArgumentParser(multiJMXOptions),
                createMultiJmxProcessor(
                        createConnectionResponse("connection", createValueResult(3)), createConnectionResponse("connection2", createValueResult(4))
                ), new NoValidationValidator(), retreivableWriter, new NoWait(), VALID_ARGS);

        Assert.assertEquals("connection 3\nconnection2 4\n", retreivableWriter.getValue());

    }

    @Test
    public void testShouldDisplayConnectionException() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        Exception exception = new Exception("exception message");

        new Main().run(createMultiArgumentParser(multiJMXOptions),
                createMultiJmxProcessor(
                        createConnectionResponse("connection", exception)
                ), new NoValidationValidator(), retreivableWriter, new NoWait(), VALID_ARGS);

        Assert.assertEquals("connection ERROR (exception message)\nErrors have occurred (1)\nPress enter to see next stack trace\n" + getStackTrace(exception) + "\n",
                retreivableWriter.getValue());

    }

    private static class NoValidationValidator implements MultiJMXOptionValidator {
        @Override
        public CommandLine validate(CommandLine commandLine) throws ValidationException {
            return commandLine;
        }
    }

    private static class RetreivableWriter implements Writer {
        private StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void writeLine(String output) {
            stringBuilder.append(output).append("\n");
        }

        public String getValue() {
            return stringBuilder.toString();
        }
    }

    private static class NoWait implements Waitable {

        @Override
        public void await() {
            //do nothing
        }

        @Override
        public void close() throws IOException {
            //to nothing
        }
    }

    private JMXValueResult createValueResult(Comparable value) {
        return new JMXValueResult(value);
    }

    private JMXValueResult createValueResult(Exception exception) {
        return new JMXValueResult(exception);
    }

    private JMXConnectionResponse createConnectionResponse(String display, JMXValueResult... jmxValueResults) {
        return new JMXConnectionResponse(display, Arrays.asList(jmxValueResults));
    }

    private JMXConnectionResponse createConnectionResponse(String display, Exception exception) {
        return new JMXConnectionResponse(display, exception);
    }

    private MultiJAEProcessor createMultiJmxProcessor(JMXConnectionResponse... jmxConnectionResponses) {
        return multiJMXOptions -> Arrays.asList(jmxConnectionResponses).stream();
    }

    private MultiJMXArgumentParser createMultiArgumentParser(final MultiJMXOptions multiJMXOptions) {
        return new MultiJMXArgumentParserImpl() {
            @Override
            public MultiJMXOptions parseArguments(CommandLine cmd) throws ParseException {
                return multiJMXOptions;
            }
        };
    }
}