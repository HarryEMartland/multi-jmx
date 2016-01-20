package uk.co.harrymartland.multijmx.processer;

import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

import java.util.stream.Stream;

public interface MultiJAEProcessor {

    Stream<JMXConnectionResponse> run(MultiJMXOptions multiJMXOptions);

}
