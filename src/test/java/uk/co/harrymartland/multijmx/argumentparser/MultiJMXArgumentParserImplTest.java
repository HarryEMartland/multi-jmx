package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

public class MultiJMXArgumentParserImplTest {

    private final String VALID_OBJECT_NAME = "java.lang:type=OperatingSystem";

    private final MultiJMXArgumentParser multiJMXArgumentParser = new MultiJMXArgumentParserImpl();

    @org.junit.Test
    public void testShouldRequireObjectNameAndAttributeShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(arguments("-o " + VALID_OBJECT_NAME + " -a attribute"));
        Assert.assertEquals(Collections.singletonList(new ObjectName(VALID_OBJECT_NAME)), multiJMXOptions.getObjectNames());
        Assert.assertEquals(Collections.singletonList("attribute"), multiJMXOptions.getSignatures());
    }

    @org.junit.Test
    public void testShouldReturnMultipleAttributesAndObjectsShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(arguments("-o " + VALID_OBJECT_NAME + " -a attribute -o " + VALID_OBJECT_NAME + " -a attribute -o " + VALID_OBJECT_NAME + " -a attribute"));
        Assert.assertEquals(Arrays.asList(new ObjectName(VALID_OBJECT_NAME), new ObjectName(VALID_OBJECT_NAME), new ObjectName(VALID_OBJECT_NAME)), multiJMXOptions.getObjectNames());
        Assert.assertEquals(Arrays.asList("attribute", "attribute", "attribute"), multiJMXOptions.getSignatures());
    }

    @org.junit.Test
    public void testShouldReturnMultipleAttributesAndObjectsLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(arguments("-object-name " + VALID_OBJECT_NAME + " -attribute attribute -object-name " + VALID_OBJECT_NAME + " -attribute attribute -object-name " + VALID_OBJECT_NAME + " -attribute attribute"));
        Assert.assertEquals(Arrays.asList(new ObjectName(VALID_OBJECT_NAME), new ObjectName(VALID_OBJECT_NAME), new ObjectName(VALID_OBJECT_NAME)), multiJMXOptions.getObjectNames());
        Assert.assertEquals(Arrays.asList("attribute", "attribute", "attribute"), multiJMXOptions.getSignatures());
    }

    @org.junit.Test
    public void testShouldRequireObjectNameAndAttributeLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(arguments("-object-name " + VALID_OBJECT_NAME + " -attribute attribute"));
        Assert.assertEquals(Collections.singletonList(new ObjectName(VALID_OBJECT_NAME)), multiJMXOptions.getObjectNames());
        Assert.assertEquals(Collections.singletonList("attribute"), multiJMXOptions.getSignatures());
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
    public void testShouldReturnOrderConnectionShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-c"));
        Assert.assertTrue(multiJMXOptions.isOrderConnection());
        Assert.assertTrue(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnOrderConnectionLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-order-connection"));
        Assert.assertTrue(multiJMXOptions.isOrderConnection());
        Assert.assertTrue(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnReverseOrderShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-r"));
        Assert.assertTrue(multiJMXOptions.isReverseOrder());
        Assert.assertFalse(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnReverseOrderLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-reverse-order"));
        Assert.assertTrue(multiJMXOptions.isReverseOrder());
        Assert.assertFalse(multiJMXOptions.isOrdered());
    }

    @Test
    public void testShouldReturnDefaultDelemiter() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString(""));
        Assert.assertEquals("\t", multiJMXOptions.getDelimiter());
    }

    @Test
    public void testShouldReturnDelimiterShortName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-d ,"));
        Assert.assertEquals(",", multiJMXOptions.getDelimiter());
    }

    @Test
    public void testShouldReturnDelimiterLongName() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-delimiter ,"));
        Assert.assertEquals(",", multiJMXOptions.getDelimiter());
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
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("service:jmx:rmi://test/jndi/rmi://:9999/jmxrmi"));
        Assert.assertEquals(1, multiJMXOptions.getUrls().size());
        assertJMXConnection(new JMXConnection("test", new JMXServiceURL("service:jmx:rmi://test/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(0));
    }

    @Test
    public void testUrlGenerationRMIMultiple() throws Exception {
        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("service:jmx:rmi://test1/jndi/rmi://:9999/jmxrmi service:jmx:rmi://test2/jndi/rmi://:9999/jmxrmi"));
        Assert.assertEquals(2, multiJMXOptions.getUrls().size());
        assertJMXConnection(new JMXConnection("test1", new JMXServiceURL("service:jmx:rmi://test1/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(0));
        assertJMXConnection(new JMXConnection("test2", new JMXServiceURL("service:jmx:rmi://test2/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(1));
    }

    @Test
    public void testUrlGenerationProcessId() throws Exception {
        //todo test get process id url
    }

    @Test
    public void testShouldReadConnectionsFromFile() throws Exception {
        URL resource = getClass().getResource("/testRMIUrls.csv");

        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-f " + resource.getPath()));
        Assert.assertEquals(3, multiJMXOptions.getUrls().size());
        assertJMXConnection(new JMXConnection("test1", new JMXServiceURL("service:jmx:rmi://test1/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(0));
        assertJMXConnection(new JMXConnection("test2", new JMXServiceURL("service:jmx:rmi://test2/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(1));
        assertJMXConnection(new JMXConnection("test3", new JMXServiceURL("service:jmx:rmi://test3/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(2));
    }

    @Test
    public void testShouldReadConnectionsFromFileAndCMDLine() throws Exception {
        URL resource = getClass().getResource("/testRMIUrls.csv");

        MultiJMXOptions multiJMXOptions = multiJMXArgumentParser.parseArguments(addToWorkingArgumentString("-f " + resource.getPath() + " service:jmx:rmi://test4/jndi/rmi://:9999/jmxrmi"));
        Assert.assertEquals(4, multiJMXOptions.getUrls().size());
        assertJMXConnection(new JMXConnection("test1", new JMXServiceURL("service:jmx:rmi://test1/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(0));
        assertJMXConnection(new JMXConnection("test2", new JMXServiceURL("service:jmx:rmi://test2/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(1));
        assertJMXConnection(new JMXConnection("test3", new JMXServiceURL("service:jmx:rmi://test3/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(2));
        assertJMXConnection(new JMXConnection("test4", new JMXServiceURL("service:jmx:rmi://test4/jndi/rmi://:9999/jmxrmi")), multiJMXOptions.getUrls().get(3));
    }

    @Test
    public void testShouldReadProcessConnectionFromFile() throws Exception {
        //todo test get process id url from file
    }

    @Test
    public void testSouldReadProcessConnectionFromFileAndCMDLine() throws Exception {
        //todo test get process id url from file and cmd line
    }

    //todo tidy up and use array of strings rather than one string
    private CommandLine addToWorkingArgumentString(String toAdd) throws ParseException {
        return arguments("-o " + VALID_OBJECT_NAME + " -a attribute " + toAdd);
    }

    private CommandLine arguments(String string) throws ParseException {
        return new DefaultParser().parse(multiJMXArgumentParser.getOptions(), StringUtils.split(string, " "));
    }

    private void assertJMXConnection(JMXConnection actual, JMXConnection expected) {
        Assert.assertEquals(actual.getDisplay(), expected.getDisplay());
        Assert.assertEquals(actual.getJmxServiceURL(), expected.getJmxServiceURL());
    }
}