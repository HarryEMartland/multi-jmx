package uk.co.harrymartland.multijmx.domain.valueretriver;

import javax.management.*;
import java.io.IOException;

public class MethodValueRetriever implements JMXValueRetriever {

    private ObjectName objectName;
    private String methodName;
    private String[] parameterTypes;
    private Object[] parameterValues;

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.invoke(objectName, methodName, parameterValues, parameterTypes);
    }
}
