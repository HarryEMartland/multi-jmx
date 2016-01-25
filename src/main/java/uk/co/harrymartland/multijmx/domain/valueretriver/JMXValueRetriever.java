package uk.co.harrymartland.multijmx.domain.valueretriver;

import javax.management.*;
import java.io.IOException;

public interface JMXValueRetriever {
    Comparable getValue(MBeanServerConnection mBeanServerConnection) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException;
}
