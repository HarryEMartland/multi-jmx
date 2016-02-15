package uk.co.harrymartland.multijmx.domain.OptionValue.signature;

import uk.co.harrymartland.multijmx.domain.OptionValue.OptionValue;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import java.util.List;

public interface SignatureOptionValue extends OptionValue<List<JMXValueRetriever>> {
}
