package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;


public interface MultiJMXArgumentParser {

    String HELP_SHORT_OPTION = "h";
    String HELP_LONG_OPTION = "help";

    MultiJMXOptions parseArguments(String[] args) throws ParseException;

    Options getOptions();

}
