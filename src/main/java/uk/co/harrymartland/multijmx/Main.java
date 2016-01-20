package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.processer.MultiJAEProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidator;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidatorImpl;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
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
            MultiJMXOptions jmxOptions = multiJMXOptionValidator.validate(multiJMXArgumentParser.parseArguments(args));
            List<JMXConnectionResponse> errors = multiJMXProcessor.run(jmxOptions)
                    .peek(jmxResponse -> display(jmxResponse, jmxOptions.getDelimiter()))
                    .filter(JMXConnectionResponse::isError)
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {

                displayErrors(errors);
            }
        }
    }

    //todo output value errors
    private void displayErrors(List<JMXConnectionResponse> errors) {
        BufferedReader reader = null;
        InputStreamReader inputStream = null;
        try {
            System.out.println("errors have occurred (" + errors.size() + ")");
            inputStream = new InputStreamReader(System.in);
            reader = new BufferedReader(inputStream);
            for (JMXConnectionResponse error : errors) {
                System.out.println("Press enter to see next stack trace");
                reader.readLine();
                error.getException().printStackTrace();
            }
        } catch (IOException e) {
            ExceptionUtils.eat(e);
        } finally {
            ExceptionUtils.closeQuietly(reader, inputStream);
        }
    }


    private void display(JMXConnectionResponse jmxConnectionResponse, String delimiter) {
        System.out.print(jmxConnectionResponse.getDisplay() + delimiter);
        if (jmxConnectionResponse.isError()) {
            System.out.println("ERROR (" + jmxConnectionResponse.getException().getMessage() + ")");
        } else {
            System.out.println(jmxConnectionResponse.getValue().stream().map(Objects::toString).collect(Collectors.joining(delimiter)));
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
