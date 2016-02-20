package uk.co.harrymartland.multijmx.jmxrunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.harrymartland.multijmx.ExceptionUtils;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJmxRunnerTest {

    private List<JMXValueRetriever> jmxValueRetrievers;
    private List<ObjectName> objectNames;
    private JMXConnection jmxConnection;
    private ConnectorService connectorService = createConnectorService();
    private Map<String, ?> envMap;

    @Test
    public void testShouldCreateResponseWhenOneObjectAndOneSignatureIsPassed() throws Exception {
        ObjectName objectName = createObjectName();
        JMXValueRetriever valueRetriever = createValueRetriever(45);
        givenObjectNames(objectName);
        givenValidConnection(createConnection("display1"));
        givenValueRetrievers(valueRetriever);
        thenShouldReturnResponseWithValues(null, 45);
        shouldHaveCalledValueRetreiverWithObject(valueRetriever, objectName);
    }

    private void shouldHaveCalledValueRetreiverWithObject(JMXValueRetriever valueRetriever, ObjectName objectName) {
        try {
            Mockito.verify(valueRetriever, Mockito.times(1)).getValue(Mockito.any(MBeanServerConnection.class), Mockito.eq(objectName));
        } catch (AttributeNotFoundException | MBeanException | InstanceNotFoundException | IOException | ReflectionException e) {
            ExceptionUtils.eat(e);
        }
    }

    @Test
    public void testShouldCreateResponseWhenMultipleValuesWhenMultipleValueRetrieversArePassedWithOneObjectName() throws Exception {
        ObjectName objectName = createObjectName();
        JMXValueRetriever valueRetriever1 = createValueRetriever(45);
        JMXValueRetriever valueRetriever2 = createValueRetriever(56);
        givenObjectNames(objectName);
        givenValidConnection(createConnection("display1"));
        givenValueRetrievers(valueRetriever1, valueRetriever2);
        thenShouldReturnResponseWithValues(null, 45, 56);
        shouldHaveCalledValueRetreiverWithObject(valueRetriever1, objectName);
        shouldHaveCalledValueRetreiverWithObject(valueRetriever2, objectName);
    }

    @Test
    public void testShouldCreateResponseWhenMultipleValuesWhenMultipleValueRetrieversArePassedWithMultipleObjectNames() throws Exception {
        ObjectName objectName1 = createObjectName();
        ObjectName objectName2 = createObjectName();
        JMXValueRetriever valueRetriever1 = createValueRetriever(45);
        JMXValueRetriever valueRetriever2 = createValueRetriever(56);
        givenObjectNames(objectName1, objectName2);
        givenValidConnection(createConnection("display1"));
        givenValueRetrievers(valueRetriever1, valueRetriever2);
        thenShouldReturnResponseWithValues(null, 45, 56);
        shouldHaveCalledValueRetreiverWithObject(valueRetriever1, objectName1);
        shouldHaveCalledValueRetreiverWithObject(valueRetriever2, objectName2);
    }

    private JMXValueRetriever createValueRetriever(Comparable value) {
        JMXValueRetriever mock = Mockito.mock(JMXValueRetriever.class);
        try {
            Mockito.when(mock.getValue(Mockito.any(MBeanServerConnection.class), Mockito.any(ObjectName.class))).thenReturn(value);
        } catch (AttributeNotFoundException | MBeanException | InstanceNotFoundException | ReflectionException | IOException e) {
            ExceptionUtils.eat(e);
        }
        return mock;
    }

    private JMXConnection createConnection(String display) {
        JMXConnection mock = Mockito.mock(JMXConnection.class);
        Mockito.when(mock.getDisplay()).thenReturn(display);
        Mockito.when(mock.getJmxServiceURL()).thenReturn(Mockito.mock(JMXServiceURL.class));
        return mock;
    }

    private ConnectorService createConnectorService() {
        ConnectorService mock = Mockito.mock(ConnectorService.class);
        try {
            JMXConnector jmxConnector = createJmxConnector();
            Mockito.when(mock.connect(Mockito.any(), Mockito.any())).thenReturn(jmxConnector);
        } catch (IOException e) {
            ExceptionUtils.eat(e);
        }
        return mock;
    }

    private JMXConnector createJmxConnector() {
        JMXConnector mock = Mockito.mock(JMXConnector.class);
        try {
            Mockito.when(mock.getMBeanServerConnection()).thenReturn(Mockito.mock(MBeanServerConnection.class));
        } catch (IOException e) {
            ExceptionUtils.eat(e);
        }
        return mock;
    }

    private ObjectName createObjectName() {
        return Mockito.mock(ObjectName.class);
    }

    private void givenValueRetrievers(JMXValueRetriever... valueRetrievers) {
        this.jmxValueRetrievers = Arrays.asList(valueRetrievers);
    }

    private void givenValidConnection(JMXConnection connection) {
        this.jmxConnection = connection;
    }

    private void givenObjectNames(ObjectName... objectNames) {
        this.objectNames = Arrays.asList(objectNames);
    }

    private void thenShouldReturnResponseWithValues(Exception e, Object... values) throws Exception {
        JMXConnectionResponse call = createJmxRunner().call();

        if (e != null) {
            Assert.assertEquals(e, call.getException());
        }

        Assert.assertEquals(values.length, call.getValue().size());

        Iterator<Object> expectedIterator = Arrays.asList(values).iterator();
        Iterator<JMXValueResult> iterator = call.getValue().iterator();

        while (expectedIterator.hasNext() && iterator.hasNext()) {
            Object expected = expectedIterator.next();
            JMXValueResult actual = iterator.next();
            if (expected instanceof Exception) {
                Assert.assertEquals(expected, actual.getException());
            } else {
                Assert.assertEquals(expected, actual.getValue());
            }
        }

    }

    private AbstractJmxRunner createJmxRunner() {
        return new AbstractJmxRunner(jmxValueRetrievers, objectNames, jmxConnection, connectorService) {
            @Override
            protected Map<String, ?> getEnv() {
                return envMap;
            }
        };
    }

}