package uk.co.harrymartland.multijmx.service.valueretriever;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class ValueRetrieverServiceImplTest {

    private ValueRetrieverService valueRetrieverService = new ValueRetrieverServiceImpl(new SpelExpressionParser());
    private MBeanServerConnection mBeanServerConnection = Mockito.mock(MBeanServerConnection.class);

    @Test
    public void testShouldReturnAttributeValueRetrieverWhenNoBraces() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever(objectName, "attribute");
        actual.getValue(mBeanServerConnection);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).getAttribute(objectName, "attribute");
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithNoArgs() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever(objectName, "method()");
        actual.getValue(mBeanServerConnection);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method", new Object[]{}, new String[]{});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithIntegers() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever(objectName, "method(1,2,3)");
        actual.getValue(mBeanServerConnection);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)},
                new String[]{"java.lang.Integer", "java.lang.Integer", "java.lang.Integer"});
    }

    //todo write documentation
    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithInts() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever(objectName, "method(new uk.co.harrymartland.multijmx.domain.typeable.IntType(1),new uk.co.harrymartland.multijmx.domain.typeable.IntType(2),new uk.co.harrymartland.multijmx.domain.typeable.IntType(3))");
        actual.getValue(mBeanServerConnection);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)},
                new String[]{"int", "int", "int"});
    }
}