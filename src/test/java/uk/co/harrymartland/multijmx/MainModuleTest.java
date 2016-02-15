package uk.co.harrymartland.multijmx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;

public class MainModuleTest {

    @Test
    public void testShouldInjectSuccessfully() throws Exception {
        Injector injector = Guice.createInjector(new MultiJMXModule(), new ArgumentModule());
    }
}