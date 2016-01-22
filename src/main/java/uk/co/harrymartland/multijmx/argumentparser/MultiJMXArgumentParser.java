package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;


public interface MultiJMXArgumentParser {

    String HELP_SHORT_OPTION = "h";
    String HELP_LONG_OPTION = "help";

    MultiJMXOptions parseArguments(CommandLine cmd) throws ParseException;

    Options getOptions();

}
