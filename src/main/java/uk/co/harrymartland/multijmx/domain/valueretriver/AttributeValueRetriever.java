package uk.co.harrymartland.multijmx.domain.valueretriver;

import javax.management.*;
import java.io.IOException;

public class AttributeValueRetriever implements JMXValueRetriever {

    private String attribute;

    public AttributeValueRetriever(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection, ObjectName objectName) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.getAttribute(objectName, attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValueRetriever that = (AttributeValueRetriever) o;
        return attribute != null ? attribute.equals(that.attribute) : that.attribute == null;
    }

    @Override
    public int hashCode() {
        return attribute != null ? attribute.hashCode() : 0;
    }
}
