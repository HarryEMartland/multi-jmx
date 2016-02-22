package uk.co.harrymartland.multijmx.service.lifecycle;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.co.harrymartland.multijmx.domain.LifeCycleAble;
import uk.co.harrymartland.multijmx.domain.optionvalue.threadpool.ThreadPoolOptionValue;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;
import uk.co.harrymartland.multijmx.waitable.SystemWaitable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class LifeCycleSingletonTest {

    @Inject
    private SingletonTest singletonTest;

    @Parameterized.Parameter
    public Class<LifeCycleAble> classToCheck;
    private Injector injector;

    @Parameterized.Parameters
    public static List<Class<? extends LifeCycleAble>> parameters() {
        return Arrays.asList(
                ThreadPoolOptionValue.class,
                SystemWaitable.class
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
        LifeCycleAble instance = injector.getInstance(classToCheck);
        singletonTest.testSingleton(instance);
    }

    private static class SingletonTest {
        Set<LifeCycleAble> optionValues;

        @Inject
        public SingletonTest(Set<LifeCycleAble> optionValues) {
            this.optionValues = optionValues;
        }

        public void testSingleton(LifeCycleAble optionValue) {
            Assert.assertTrue(optionValue.getClass().getSimpleName() + " is not a singleton", optionValues.contains(optionValue));
        }
    }
}
