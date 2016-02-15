package uk.co.harrymartland.multijmx.domain.optionvalue.signature;

import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import java.util.List;

public interface SignatureOptionValue extends OptionValue<List<JMXValueRetriever>> {
}
