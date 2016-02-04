package uk.co.harrymartland.multijmx.service.valueretriever;

import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.ObjectName;

public interface ValueRetrieverService {

    JMXValueRetriever createRetriever(ObjectName objectName, String signature);

}
