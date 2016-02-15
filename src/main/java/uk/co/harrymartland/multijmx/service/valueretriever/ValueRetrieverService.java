package uk.co.harrymartland.multijmx.service.valueretriever;

import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

public interface ValueRetrieverService {

    JMXValueRetriever createRetriever(String signature);

}
