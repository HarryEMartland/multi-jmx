package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;

public class MultiJMXArgumentParserImplTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";

    private final MultiJMXArgumentParser multiJMXArgumentParser = new MultiJMXArgumentParserImpl();

    @org.junit.Test
    public void testShouldRequireObjectNameAndAttributeShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(arguments("-o " + VALID_OBJECT_NAME + " -a attribute"));
        Assert.assertEquals(new ObjectName(VALID_OBJECT_NAME), multiJMXOptions.getObjectName());
        Assert.assertEquals("attribute", multiJMXOptions.getAttribute());
    }

    @org.junit.Test
    public void testShouldRequireObjectNameAndAttributeLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(arguments("-object-name " + VALID_OBJECT_NAME + " -attribute attribute"));
        Assert.assertEquals(new ObjectName(VALID_OBJECT_NAME), multiJMXOptions.getObjectName());
        Assert.assertEquals("attribute", multiJMXOptions.getAttribute());
    }

    @org.junit.Test(expected = ParseException.class)
    public void testShouldThrowExceptionWhenAttributeNotPassed() throws Exception {
        multiJMXArgumentParser.parseArguments(arguments("-object-name " + VALID_OBJECT_NAME));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testShouldThrowExceptionWhenObjectNameNotPassed() throws Exception {
        multiJMXArgumentParser.parseArguments(arguments("-attribute attribute"));
    }

    @Test
    public void testShouldReturnMaxThreadsShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-t 6"));
        Assert.assertEquals(new Integer(6), multiJMXOptions.getMaxThreads());
    }

    @Test
    public void testShouldReturnMaxThreadsLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-max-threads 6"));
        Assert.assertEquals(new Integer(6), multiJMXOptions.getMaxThreads());
    }

    @Test
    public void testShouldReturnOrderValueShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-v"));
        Assert.assertTrue(multiJMXOptions.isOrderValue());
        Assert.assertTrue(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnOrderValueLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-order-value"));
        Assert.assertTrue(multiJMXOptions.isOrderValue());
        Assert.assertTrue(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnOrderDisplayShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-d"));
        Assert.assertTrue(multiJMXOptions.isOrderDisplay());
        Assert.assertTrue(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnOrderDisplayLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-order-display"));
        Assert.assertTrue(multiJMXOptions.isOrderDisplay());
        Assert.assertTrue(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnUserNamePasswordAndAuthenticationRequiredShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-u username -p password"));
        Assert.assertEquals("username", multiJMXOptions.getUsername());
        Assert.assertEquals("password", multiJMXOptions.getPassword());
        Assert.assertTrue(multiJMXOptions.isAuthenticationRequired());
    }

    @Test
    public void testShouldReturnUserNamePasswordAndAuthenticationRequiredLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-username username -password password"));
        Assert.assertEquals("username", multiJMXOptions.getUsername());
        Assert.assertEquals("password", multiJMXOptions.getPassword());
        Assert.assertTrue(multiJMXOptions.isAuthenticationRequired());
    }

    @Test
    public void testUrlGenerationRMI() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi"));
        Assert.assertEquals(1, multiJMXOptions.getUrls().size());
        Assert.assertEquals(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi"), multiJMXOptions.getUrls().get(0));
    }

    @Test
    public void testUrlGenerationRMIMultiple() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi service:jmx:rmi:///jndi/rmi://:9999/jmxrmi"));
        Assert.assertEquals(2, multiJMXOptions.getUrls().size());
        Assert.assertEquals(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi"), multiJMXOptions.getUrls().get(0));
        Assert.assertEquals(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi"), multiJMXOptions.getUrls().get(1));
    }

    @Test
    public void testUrlGenerationProcessId() throws Exception {
        //todo
    }

    private String[] addToWorkingArgumentString(String toAdd) {
        return arguments("-o " + VALID_OBJECT_NAME + " -a attribute " + toAdd);
    }

    private String[] arguments(String string) {
        return StringUtils.split(string, " ");
    }
}