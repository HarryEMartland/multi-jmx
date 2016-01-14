package uk.co.harrymartland.multijmx;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

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

}