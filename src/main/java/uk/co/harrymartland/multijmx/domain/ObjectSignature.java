package uk.co.harrymartland.multijmx.domain;

import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.ObjectName;

public class ObjectSignature {

    private JMXValueRetriever jmxValueRetriever;
    private ObjectName objectName;

    public ObjectSignature(JMXValueRetriever jmxValueRetriever, ObjectName objectName) {
        this.jmxValueRetriever = jmxValueRetriever;
        this.objectName = objectName;
    }

    public JMXValueRetriever getJmxValueRetriever() {
        return jmxValueRetriever;
    }

    public ObjectName getObjectName() {
        return objectName;
    }
}
