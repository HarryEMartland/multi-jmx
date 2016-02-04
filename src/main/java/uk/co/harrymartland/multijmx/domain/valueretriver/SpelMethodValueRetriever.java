package uk.co.harrymartland.multijmx.domain.valueretriver;

import uk.co.harrymartland.multijmx.domain.typeable.Typeable;

import javax.management.*;
import java.io.IOException;
import java.util.Arrays;

public class SpelMethodValueRetriever implements JMXValueRetriever {

    private ObjectName objectName;
    private String methodName;
    private Typeable[] args;

    public SpelMethodValueRetriever(ObjectName objectName, String methodName, Typeable[] args) {
        this.objectName = objectName;
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.invoke(objectName, methodName, Arrays.stream(args).map(Typeable::getObject).toArray(Object[]::new), getSignatures());
    }

    public String[] getSignatures() {
        return Arrays.stream(args).map(Typeable::getType).toArray(String[]::new);
    }
}
