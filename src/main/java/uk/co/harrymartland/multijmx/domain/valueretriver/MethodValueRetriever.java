package uk.co.harrymartland.multijmx.domain.valueretriver;

import javax.management.*;
import java.io.IOException;

public class MethodValueRetriever implements JMXValueRetriever {

    private ObjectName objectName;
    private String methodName;
    private String[] parameterTypes;
    private Object[] parameterValues;

    public MethodValueRetriever(ObjectName objectName, String methodName, String[] parameterTypes, Object[] parameterValues) {
        this.objectName = objectName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterValues = parameterValues;
    }

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.invoke(objectName, methodName, parameterValues, parameterTypes);
    }
}
