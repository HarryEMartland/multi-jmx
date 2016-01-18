package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.domain.JMXResponse;
import uk.co.harrymartland.multijmx.processer.MultiJAEProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidator;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidatorImpl;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser.HELP_LONG_OPTION;
import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser.HELP_SHORT_OPTION;

public class Main {

    public static void main(String[] args) throws ParseException, ValidationException {
        new Main().run(new MultiJMXArgumentParserImpl(), new MultiJMXProcessorImpl(), new MultiJMXOptionValidatorImpl(), args);
    }

    public void run(MultiJMXArgumentParser multiJMXArgumentParser, MultiJAEProcessor multiJMXProcessor,
                    MultiJMXOptionValidator multiJMXOptionValidator, String[] args) throws ParseException, ValidationException {

        if (isDisplayHelp(args)) {
            new HelpFormatter().printHelp("multi-jmx", multiJMXArgumentParser.getOptions(), true);
        }
        if (args.length > 0) {
            List<JMXResponse> errors = multiJMXProcessor.run(multiJMXOptionValidator.validate(multiJMXArgumentParser.parseArguments(args)))
                    .peek(this::display)
                    .filter(JMXResponse::isError)
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {

                displayErrors(errors);
            }
        }
    }

    private void displayErrors(List<JMXResponse> errors) {
        BufferedReader reader = null;
        InputStreamReader inputStream = null;
        try {
            System.out.println("errors have occurred (" + errors.size() + ")");
            inputStream = new InputStreamReader(System.in);
            reader = new BufferedReader(inputStream);
            for (JMXResponse error : errors) {
                System.out.println("Press enter to see next stack trace");
                reader.readLine();
                error.getException().printStackTrace();
            }
        } catch (IOException e) {
            ExceptionUtils.eat(e);
        } finally {
            ExceptionUtils.closeQuietly(reader);
            ExceptionUtils.closeQuietly(inputStream);
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
