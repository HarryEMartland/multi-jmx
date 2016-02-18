package uk.co.harrymartland.multijmx.domain.valueretriver;

import com.google.common.collect.ImmutableList;
import uk.co.harrymartland.multijmx.domain.typeable.Typeable;

import javax.management.*;
import java.io.IOException;
import java.util.List;

public class SpelMethodValueRetriever implements JMXValueRetriever {

    private String methodName;
    private ImmutableList<Typeable> args;

    public SpelMethodValueRetriever(String methodName, List<Typeable> args) {
        this.methodName = methodName;
        this.args = ImmutableList.copyOf(args);
    }

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection, ObjectName objectName) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.invoke(
                objectName,
                methodName,
                args.stream().map(Typeable::getObject).toArray(Object[]::new), getSignatures()
        );
    }

    public String[] getSignatures() {
        return args.stream()
                .map(Typeable::getType)
                .toArray(String[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpelMethodValueRetriever that = (SpelMethodValueRetriever) o;

        return methodName != null ? methodName.equals(that.methodName) : that.methodName == null
                && (args != null ? args.equals(that.args) : that.args == null);

    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + (args != null ? args.hashCode() : 0);
        return result;
    }
}
