package uk.co.harrymartland.multijmx.service.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public interface CommandLineService {

    CommandLine get();

    CommandLine create(String[] args) throws ParseException;

}
