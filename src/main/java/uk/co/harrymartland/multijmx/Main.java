package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.processer.MultiJAEProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;

import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser.HELP_LONG_OPTION;
import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser.HELP_SHORT_OPTION;

public class Main {

    public static void main(String[] args) throws ParseException {
        new Main().run(new MultiJMXArgumentParserImpl(), new MultiJMXProcessorImpl(), args);
    }

    public void run(MultiJMXArgumentParser multiJMXArgumentParser, MultiJAEProcessor multiJMXProcessor, String[] args) throws ParseException {
        if (isDisplayHelp(args)) {
            new HelpFormatter().printHelp("multi-jmx", multiJMXArgumentParser.getOptions(), true);
        }
        if (args.length > 0) {
            multiJMXProcessor.run(multiJMXArgumentParser.parseArguments(args)).forEach(this::display);
            //todo if errors give user option to show stack traces
        }
    }

    private void display(JMXResponse jmxResponse) {
        System.out.print(jmxResponse.getDisplay() + "\t");
        if (jmxResponse.isError()) {
            System.out.println("ERROR (" + jmxResponse.getException().getMessage() + ")");
        } else {
            System.out.println(jmxResponse.getValue());
        }
    }

    public boolean isDisplayHelp(String[] args) {
        return args.length == 0 || isHelpOption(args);
    }

    private boolean isHelpOption(String[] args) {
        for (String arg : args) {
            if (isHelpOption(arg)) {
                return true;
            }
        }
        return false;
    }

    private boolean isHelpOption(String arg) {
        return arg.equalsIgnoreCase("-" + HELP_SHORT_OPTION) || arg.equalsIgnoreCase("-" + HELP_LONG_OPTION);

    }

}
