package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.processer.MultiJAEProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;

public class Main {

    public static void main(String[] args) throws ParseException {
        new Main().run(new MultiJMXArgumentParserImpl(), new MultiJMXProcessorImpl(), args);
    }

    public void run(MultiJMXArgumentParser multiJMXArgumentParser, MultiJAEProcessor multiJMXProcessor, String[] args) throws ParseException {
        multiJMXProcessor.run(multiJMXArgumentParser.parseArguments(args));
    }

}
