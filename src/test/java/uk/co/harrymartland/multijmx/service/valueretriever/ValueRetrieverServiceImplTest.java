package uk.co.harrymartland.multijmx.service.valueretriever;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.domain.typeable.*;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class ValueRetrieverServiceImplTest {

    private ValueRetrieverService valueRetrieverService = new ValueRetrieverServiceImpl(new SpelExpressionParser());
    private MBeanServerConnection mBeanServerConnection = Mockito.mock(MBeanServerConnection.class);

    @Test
    public void testShouldReturnAttributeValueRetrieverWhenNoBraces() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("signature");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).getAttribute(objectName, "signature");
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithNoArgs() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method()");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method", new Object[]{}, new String[]{});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithIntegers() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(1,2,3)");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)},
                new String[]{"java.lang.Integer", "java.lang.Integer", "java.lang.Integer"});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithBoolean() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(new " + BooleanType.class.getCanonicalName() + "(true))");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Boolean.TRUE},
                new String[]{"boolean"});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithChar() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(new " + CharType.class.getCanonicalName() + "('c'))");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Character.valueOf('c')},
                new String[]{"char"});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithDouble() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(new " + DoubleType.class.getCanonicalName() + "(56.9))");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Double.valueOf(56.9)},
                new String[]{"double"});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithFloat() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(new " + FloatType.class.getCanonicalName() + "(83.6))");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Double.valueOf(83.6)},
                new String[]{"float"});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithNull() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(new " + NullType.class.getCanonicalName() + "(T(" + String.class.getCanonicalName() + ")))");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{null},
                new String[]{String.class.getCanonicalName()});
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodWithInts() throws Exception {
        ObjectName objectName = new ObjectName("");
        JMXValueRetriever actual = valueRetrieverService.createRetriever("method(new " + IntType.class.getCanonicalName() + "(1),new " + IntType.class.getCanonicalName() + "(2),new " + IntType.class.getCanonicalName() + "(3))");
        actual.getValue(mBeanServerConnection, objectName);
        Mockito.verify(mBeanServerConnection, Mockito.times(1)).invoke(objectName, "method",
                new Object[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)},
                new String[]{"int", "int", "int"});
    }
}