package uk.co.harrymartland.multijmx.service.commandline;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.service.options.OptionsService;

@Singleton
public class CommandLineServiceImpl implements CommandLineService {


    private CommandLine commandLine;
    private CommandLineParser commandLineParser;
    private OptionsService optionsService;

    @Inject
    public CommandLineServiceImpl(CommandLineParser commandLineParser, OptionsService optionsService) {
        this.commandLineParser = commandLineParser;
        this.optionsService = optionsService;
    }

    @Override
    public CommandLine get() {
        if (commandLine == null) {
            throw new IllegalStateException("Command line not set!");
        }
        return commandLine;
    }

    @Override
    public CommandLine create(String[] args) throws ParseException {
        if (commandLine != null) {
            throw new IllegalStateException("Command line already set!");
        }
        return commandLine = commandLineParser.parse(optionsService.getOptions(), args);
    }
}
