package uk.co.harrymartland.multijmx.service.options;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg.ConnectionArgOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile.ConnectionFileOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.delimiter.DelimiterOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.help.HelpOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionServiceImpl;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;
import uk.co.harrymartland.multijmx.service.connector.ConnectorServiceImpl;
import uk.co.harrymartland.multijmx.service.file.FileReaderService;
import uk.co.harrymartland.multijmx.service.file.FileReaderServiceImpl;

public class OptionsServiceImplTest {

    @Inject
    private OptionsService optionsService;
    @Inject
    private SignatureOptionValue signatureOptionValue;
    @Inject
    private OrderValueOptionValue orderValueOptionValue;
    @Inject
    private ObjectOptionValue objectOptionValue;
    @Inject
    private OrderConnectionOptionValue orderConnectionOptionValue;
    @Inject
    private ReverseOrderOptionValue reverseOrderOptionValue;
    @Inject
    private MaxThreadsOptionValue maxThreadsOptionValue;
    @Inject
    private UserNameOptionValue userNameOptionValue;
    @Inject
    private PasswordOptionValue passwordOptionValue;
    @Inject
    private DelimiterOptionValue delimiterOptionValue;
    @Inject
    private ConnectionArgOptionValue connectionArgOptionValue;
    @Inject
    private ConnectionFileOptionValue connectionFileOptionValue;
    @Inject
    private HelpOptionValue helpOptionValue;


    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new ArgumentModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(ExpressionParser.class).to(SpelExpressionParser.class);
                bind(ConnectionService.class).to(ConnectionServiceImpl.class);
                bind(FileReaderService.class).to(FileReaderServiceImpl.class);
                bind(ConnectorService.class).to(ConnectorServiceImpl.class);
            }
        });
        injector.injectMembers(this);
    }

    @Test
    public void testShouldHaveOrderValueOption() {
        testOptionExists(orderValueOptionValue, "v");
    }

    @Test
    public void testShouldHaveOrderConnectionOption() {
        testOptionExists(orderConnectionOptionValue, "c");
    }

    @Test
    public void testShouldHaveReverseOrderOption() {
        testOptionExists(reverseOrderOptionValue, "r");
    }

    @Test
    public void testShouldHaveObjectOptionValue() throws Exception {
        testOptionExists(objectOptionValue, "o");
    }

    @Test
    public void testShouldHaveSignatureOptionValue() throws Exception {
        testOptionExists(signatureOptionValue, "a");
    }

    @Test
    public void testShouldHaveMaxThreadsOptionValue() throws Exception {
        testOptionExists(maxThreadsOptionValue, "t");
    }

    @Test
    public void testShouldHaveUserNameOptionValue() throws Exception {
        testOptionExists(userNameOptionValue, "u");
    }

    @Test
    public void testShouldHavePasswordOptionValue() throws Exception {
        testOptionExists(passwordOptionValue, "p");
    }

    @Test
    public void testShouldHaveDelimiterOptionValue() throws Exception {
        testOptionExists(delimiterOptionValue, "d");
    }

    @Test
    public void testShouldHaveConnectionFileOptionValue() throws Exception {
        testOptionExists(connectionFileOptionValue, "f");
    }

    @Test
    public void testShouldHaveHelpOptionValue() throws Exception {
        testOptionExists(helpOptionValue, "h");
    }

    @Test
    public void testShouldNotHaveConnectionOptionValue() throws Exception {
        for (Option option : optionsService.getOptions().getOptions()) {
            Assert.assertNotEquals(connectionArgOptionValue.getOption(), option);
        }
    }

    @Test
    public void testShouldNotHaveUnknownOption() {
        Assert.assertNull(optionsService.getOptions().getOption("z"));
    }

    private void testOptionExists(OptionValue optionValue, String arg) {
        Option serviceOption = optionsService.getOptions().getOption(arg);
        Assert.assertNotNull("Injected value should not be null", optionValue);
        Assert.assertNotNull("Service value should not be null", serviceOption);
    }
}