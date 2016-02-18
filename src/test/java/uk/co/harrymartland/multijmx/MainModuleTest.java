package uk.co.harrymartland.multijmx;

import com.google.inject.Guice;
import org.junit.Test;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;

public class MainModuleTest {

    @Test
    public void testShouldInjectSuccessfully() throws Exception {
        Guice.createInjector(new MultiJMXModule(), new ArgumentModule());
    }
}