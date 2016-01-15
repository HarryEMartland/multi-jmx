package uk.co.harrymartland.multijmx.processer;

import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

import java.util.stream.Stream;

public interface MultiJAEProcessor {

    Stream<JMXResponse> run(MultiJMXOptions multiJMXOptions);

}
