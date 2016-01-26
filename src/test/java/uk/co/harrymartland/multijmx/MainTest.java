package uk.co.harrymartland.multijmx;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidator;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidatorImpl;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.Writer;

import java.io.IOException;

import static uk.co.harrymartland.multijmx.ExceptionUtils.getStackTrace;

public class MainTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";
    private final String[] VALID_ARGS = {"-o", VALID_OBJECT_NAME, "-a", "att"};

    @Inject
    private Main main;
    private RetreivableWriter retreivableWriter = new RetreivableWriter();

    @Before
    public void setUp() throws Exception {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MultiJMXProcessor.class).to(MultiJMXProcessorImpl.class);
                bind(MultiJMXOptionValidator.class).to(MultiJMXOptionValidatorImpl.class);
                bind(MultiJMXProcessor.class).to(MultiJMXProcessorImpl.class);
                bind(Writer.class).toInstance(retreivableWriter);
                bind(MultiJMXArgumentParser.class).to(MultiJMXArgumentParserImpl.class);
                bind(Waitable.class).to(NoWait.class);
            }
        }).injectMembers(this);

    }

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
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        main.run(VALID_ARGS);

        Assert.assertEquals("connection 3\n", retreivableWriter.getValue());

    }

    @Test
    public void testShouldDisplayResultExceptionAndResult() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");
        Exception exception = new Exception("exception message");
        main.run(VALID_ARGS);

        Assert.assertEquals("connection 4 exception message\nErrors have occurred (1)\nPress enter to see next stack trace\n" + getStackTrace(exception) + "\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayMultipleResults() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");
        main.run(VALID_ARGS);

        Assert.assertEquals("connection 3 4\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayResultsForMultipleConnections() throws Exception {
        RetreivableWriter retreivableWriter = new RetreivableWriter();
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");

        main.run(VALID_ARGS);

        Assert.assertEquals("connection 3\nconnection2 4\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayConnectionException() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setDelimiter(" ");
        Exception exception = new Exception("exception message");

        main.run(VALID_ARGS);

        Assert.assertEquals("connection ERROR (exception message)\nErrors have occurred (1)\nPress enter to see next stack trace\n" + getStackTrace(exception) + "\n",
                retreivableWriter.getValue());
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

}