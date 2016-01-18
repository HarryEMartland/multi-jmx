package uk.co.harrymartland.multijmx.validator;

import org.junit.Assert;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

public class MultiJMXOptionValidatorImplTest {

    private MultiJMXOptionValidator multiJMXOptionValidator = new MultiJMXOptionValidatorImpl();

    @Test
    public void testShouldThrowExceptionWhenOrderByValueAndDisplay() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setOrderDisplay(true);
        multiJMXOptions.setOrderValue(true);
        assertExceptionThrown(multiJMXOptions, "Cannot order by value and display");

    }

    private void assertExceptionThrown(MultiJMXOptions multiJMXOptions, String message) {
        try {
            multiJMXOptionValidator.validate(multiJMXOptions);
        } catch (ValidationException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }
}