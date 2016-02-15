package uk.co.harrymartland.multijmx.domain.valueretriver;

import uk.co.harrymartland.multijmx.domain.typeable.Typeable;

import javax.management.*;
import java.io.IOException;
import java.util.Arrays;

public class SpelMethodValueRetriever implements JMXValueRetriever {

    private String methodName;
    private Typeable[] args;

    public SpelMethodValueRetriever(String methodName, Typeable[] args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection, ObjectName objectName) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.invoke(
                objectName,
                methodName,
                Arrays.stream(args).map(Typeable::getObject).toArray(Object[]::new), getSignatures()
        );
    }

    public String[] getSignatures() {
        return Arrays.stream(args)
                .map(Typeable::getType)
                .toArray(String[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpelMethodValueRetriever that = (SpelMethodValueRetriever) o;
        return methodName != null ? methodName.equals(that.methodName) : that.methodName == null && Arrays.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }
}
