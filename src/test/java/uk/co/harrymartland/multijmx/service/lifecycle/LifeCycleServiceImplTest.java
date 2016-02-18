package uk.co.harrymartland.multijmx.service.lifecycle;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.LifeCycleAble;
import uk.co.harrymartland.multijmx.domain.optionvalue.threadpool.ThreadPoolOptionValueImpl;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;
import uk.co.harrymartland.multijmx.waitable.SystemWaitable;

import java.util.Set;

public class LifeCycleServiceImplTest {

    @Inject
    private Set<LifeCycleAble> lifeCycleAbles;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new MultiJMXModule(), new ArgumentModule());
        injector.injectMembers(this);
    }

    @Test
    public void testLifecyclablesShouldContainSystemWaitable() throws Exception {
        shouldContainLifeCycleAbleOfType(SystemWaitable.class);
    }

    @Test
    public void testLifecyclablesShouldContainThreadPoolOptionValueImpl() throws Exception {
        shouldContainLifeCycleAbleOfType(ThreadPoolOptionValueImpl.class);
    }

    private void shouldContainLifeCycleAbleOfType(Class lifeCylclable) {
        Assert.assertEquals(1, lifeCycleAbles.stream().filter(i -> i.getClass().equals(lifeCylclable)).count());
    }
}