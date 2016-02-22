package uk.co.harrymartland.multijmx.service.options;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connection.ConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg.ConnectionArgOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile.ConnectionFileOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.delimiter.DelimiterOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.help.HelpOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.jmxrunner.JMXRunnerOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.threadpool.ThreadPoolOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class OptionValueSingletonTest {

    @Inject
    private SingletonTest singletonTest;

    @Parameterized.Parameter
    public Class<OptionValue> classToCheck;
    private Injector injector;

    @Parameterized.Parameters
    public static List<Class<? extends OptionValue>> parameters() {
        return Arrays.asList(
                ConnectionOptionValue.class,
                ConnectionArgOptionValue.class,
                ConnectionFileOptionValue.class,
                DelimiterOptionValue.class,
                HelpOptionValue.class,
                JMXRunnerOptionValue.class,
                MaxThreadsOptionValue.class,
                ObjectOptionValue.class,
                OrderConnectionOptionValue.class,
                OrderValueOptionValue.class,
                PasswordOptionValue.class,
                ReverseOrderOptionValue.class,
                SignatureOptionValue.class,
                ThreadPoolOptionValue.class,
                UserNameOptionValue.class
        );
    }

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new ArgumentModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(SingletonTest.class);
            }
        }, new MultiJMXModule());
        injector.injectMembers(this);
    }

    @Test
    public void testThreadPoolOptionValueShouldBeSingleton() throws Exception {
        OptionValue instance = injector.getInstance(classToCheck);
        singletonTest.testSingleton(instance);
    }

    private static class SingletonTest {
        Set<OptionValue> optionValues;

        @Inject
        public SingletonTest(Set<OptionValue> optionValues) {
            this.optionValues = optionValues;
        }

        public void testSingleton(OptionValue optionValue) {
            Assert.assertTrue(optionValues.contains(optionValue));
        }
    }
}
