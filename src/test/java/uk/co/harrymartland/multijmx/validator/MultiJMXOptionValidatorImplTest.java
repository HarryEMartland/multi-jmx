package uk.co.harrymartland.multijmx.validator;

import org.junit.Assert;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiJMXOptionValidatorImplTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";

    private MultiJMXOptionValidator multiJMXOptionValidator = new MultiJMXOptionValidatorImpl();

    @Test
    public void testShouldThrowExceptionWhenOrderByValueAndConnection() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setOrderConnection(true);
        multiJMXOptions.setOrderValue(true);
        assertExceptionThrown(multiJMXOptions, "Cannot order by value and display");
    }

    @Test
    public void testShouldNotThrowExceptionWhenOneObjectAndMultipleAttributes() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setObjectNames(createObjectNameList(VALID_OBJECT_NAME));
        multiJMXOptions.setAttributes(Arrays.asList("att1", "att2", "att3"));
        assertNoExceptionThrown(multiJMXOptions);
        assertNoExceptionThrown(multiJMXOptions);
    }

    @Test
    public void testShouldNotThrowExceptionWhenSameNumberOfObjectsAndAttributes() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setObjectNames(createObjectNameList(VALID_OBJECT_NAME, VALID_OBJECT_NAME, VALID_OBJECT_NAME));
        multiJMXOptions.setAttributes(Arrays.asList("att1", "att2", "att3"));
        assertNoExceptionThrown(multiJMXOptions);
    }

    @Test
    public void testShouldThrowExceptionWhenMoreAttributesThanObjects() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setObjectNames(createObjectNameList(VALID_OBJECT_NAME, VALID_OBJECT_NAME));
        multiJMXOptions.setAttributes(Arrays.asList("att1", "att2", "att3"));
        assertExceptionThrown(multiJMXOptions, "Number of attributes and objects must match");
    }

    @Test
    public void testShouldThrowExceptionWhenMoreObjectsThanAttributes() throws Exception {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setObjectNames(createObjectNameList(VALID_OBJECT_NAME, VALID_OBJECT_NAME, VALID_OBJECT_NAME));
        multiJMXOptions.setAttributes(Arrays.asList("att1", "att2"));
        assertExceptionThrown(multiJMXOptions, "Number of attributes and objects must match");
    }

    @Test
    public void testShouldNotThrowExceptionWithOneObjectAndOneAttribute() {
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();
        multiJMXOptions.setAttributes(Collections.singletonList("att"));
        multiJMXOptions.setObjectNames(createObjectNameList(VALID_OBJECT_NAME));
        assertNoExceptionThrown(multiJMXOptions);
    }

    private List<ObjectName> createObjectNameList(String... objectNames) {
        return Arrays.asList(objectNames).stream().map(this::createObjectName).collect(Collectors.toList());
    }

    private ObjectName createObjectName(String string) {
        try {
            return ObjectName.getInstance(string);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertNoExceptionThrown(MultiJMXOptions multiJMXOptions) {
        try {
            multiJMXOptionValidator.validate(multiJMXOptions);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void assertExceptionThrown(MultiJMXOptions multiJMXOptions, String message) {
        try {
            multiJMXOptionValidator.validate(multiJMXOptions);
        } catch (ValidationException e) {
            Assert.assertEquals(message, e.getMessage());
            return;
        }
        Assert.fail("Exception not thrown");
    }
}