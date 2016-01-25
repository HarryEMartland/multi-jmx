package uk.co.harrymartland.multijmx.domain.valueretriver;

import javax.management.*;
import java.io.IOException;

public class AttributeValueRetriever implements JMXValueRetriever {

    private ObjectName objectName;
    private String attribute;


    public AttributeValueRetriever(ObjectName objectName, String attribute) {
        this.objectName = objectName;
        this.attribute = attribute;
    }

    @Override
    public Comparable getValue(MBeanServerConnection mBeanServerConnection) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        return (Comparable) mBeanServerConnection.getAttribute(objectName, attribute);
    }
}
