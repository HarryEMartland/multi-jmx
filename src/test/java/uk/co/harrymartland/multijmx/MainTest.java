package uk.co.harrymartland.multijmx;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessor;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionServiceImpl;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;
import uk.co.harrymartland.multijmx.service.connector.ConnectorServiceImpl;
import uk.co.harrymartland.multijmx.service.file.FileReaderService;
import uk.co.harrymartland.multijmx.service.file.FileReaderServiceImpl;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.Writer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static uk.co.harrymartland.multijmx.ExceptionUtils.getStackTrace;

public class MainTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";
    private final String[] VALID_ARGS = {"-o", VALID_OBJECT_NAME, "-a", "att", "-d", " ", "validConnection"};

    @Inject
    private Main main;
    private RetreivableWriter retreivableWriter = new RetreivableWriter();
    private List<JMXConnectionResponse> mockResponses = Collections.emptyList();

    @Before
    public void setUp() {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ExpressionParser.class).to(SpelExpressionParser.class);
                bind(MultiJMXProcessor.class).toInstance(() -> mockResponses.stream());
                bind(Writer.class).toInstance(retreivableWriter);
                bind(ConnectionService.class).to(ConnectionServiceImpl.class);
                bind(Waitable.class).to(NoWait.class);
                bind(ConnectorService.class).to(ConnectorServiceImpl.class);
                bind(FileReaderService.class).to(FileReaderServiceImpl.class);
            }
        }, new ArgumentModule()).injectMembers(this);

    }

    @Test
    public void testShouldDisplayResult() throws Exception {
        mockResponses = Collections.singletonList(createConnectionResponse("connection", createValueResult(3)));
        main.run(VALID_ARGS);
        Assert.assertEquals("connection 3\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayResultExceptionAndResult() throws Exception {
        Exception exception = new Exception("exception message");
        mockResponses = Collections.singletonList(createConnectionResponse("connection", createValueResult(4), createValueResult(exception)));
        main.run(VALID_ARGS);
        Assert.assertEquals("connection 4 exception message\nErrors have occurred (1)\nPress enter to see next stack trace\n" + getStackTrace(exception) + "\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayMultipleResults() throws Exception {
        mockResponses = Collections.singletonList(createConnectionResponse("connection", createValueResult(3), createValueResult(4)));
        main.run(VALID_ARGS);
        Assert.assertEquals("connection 3 4\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayResultsForMultipleConnections() throws Exception {
        mockResponses = Arrays.asList(
                createConnectionResponse("connection", createValueResult(3)),
                createConnectionResponse("connection2", createValueResult(4)));

        main.run(VALID_ARGS);

        Assert.assertEquals("connection 3\nconnection2 4\n", retreivableWriter.getValue());
    }

    @Test
    public void testShouldDisplayConnectionException() throws Exception {
        Exception exception = new Exception("exception message");
        mockResponses = Collections.singletonList(createConnectionResponse("connection", exception));
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

}