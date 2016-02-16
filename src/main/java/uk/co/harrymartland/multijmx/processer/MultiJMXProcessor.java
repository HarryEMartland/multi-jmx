package uk.co.harrymartland.multijmx.processer;

import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;

import java.util.stream.Stream;

public interface MultiJMXProcessor {

    Stream<JMXConnectionResponse> run();

}
