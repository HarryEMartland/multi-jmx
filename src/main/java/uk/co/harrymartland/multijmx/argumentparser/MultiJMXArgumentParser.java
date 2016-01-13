package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;


public interface MultiJMXArgumentParser {

    MultiJMXOptions parseArguments(String[] args) throws ParseException;

}
