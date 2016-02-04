package uk.co.harrymartland.multijmx;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.lifecycle.LifeCycleController;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessor;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidator;
import uk.co.harrymartland.multijmx.validator.ValidationException;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.Writer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser.HELP_LONG_OPTION;
import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser.HELP_SHORT_OPTION;

public class Main {


    private MultiJMXArgumentParser multiJMXArgumentParser;
    private MultiJMXOptionValidator multiJMXOptionValidator;
    private MultiJMXProcessor multiJMXProcessor;
    private Writer writer;
    private Waitable waitable;

    @Inject
    public Main(MultiJMXArgumentParser multiJMXArgumentParser, MultiJMXOptionValidator multiJMXOptionValidator,
                MultiJMXProcessor multiJMXProcessor, Writer writer, Waitable waitable) {

        this.multiJMXArgumentParser = multiJMXArgumentParser;
        this.multiJMXOptionValidator = multiJMXOptionValidator;
        this.multiJMXProcessor = multiJMXProcessor;
        this.writer = writer;
        this.waitable = waitable;
    }

    public static void main(String[] args) throws ParseException, ValidationException {
        Injector injector = Guice.createInjector(new MultiJMXModule());
        Main main = injector.getInstance(Main.class);
        LifeCycleController lifeCycleController = injector.getInstance(LifeCycleController.class);
        try {
            lifeCycleController.birthAll();
            main.run(args);
        }finally {
            lifeCycleController.killAll();
        }
    }

    public void run(String[] args) throws ParseException, ValidationException {

        if (isDisplayHelp(args)) {
            new HelpFormatter().printHelp("multi-jmx", multiJMXArgumentParser.getOptions(), true);
        }
        if (args.length > 0) {

            CommandLine commandLine = new DefaultParser().parse(multiJMXArgumentParser.getOptions(), args);
            multiJMXOptionValidator.validate(commandLine);

            MultiJMXOptions jmxOptions = multiJMXArgumentParser.parseArguments(commandLine);
            List<Exception> errors = multiJMXProcessor.run(jmxOptions)
                    .peek(jmxResponse -> display(jmxResponse, jmxOptions.getDelimiter(), writer))
                    .flatMap(this::getExceptions)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {
                displayErrors(errors, writer, waitable);
            }
        }
    }

    private Stream<? extends Exception> getExceptions(JMXConnectionResponse jmxConnectionResponse) {
        return Stream.concat(Stream.of(jmxConnectionResponse.getException()), jmxConnectionResponse.getValue().stream().map(JMXValueResult::getException));
    }

    private void displayErrors(List<Exception> errors, Writer writer, Waitable waitable) {
        writer.writeLine("Errors have occurred (" + errors.size() + ")");
        for (Exception error : errors) {
            writer.writeLine("Press enter to see next stack trace");
            waitable.await();
            writer.writeLine(ExceptionUtils.getStackTrace(error));
        }
    }


    private void display(JMXConnectionResponse jmxConnectionResponse, String delimiter, Writer writer) {
        String value;
        if (jmxConnectionResponse.isError()) {
            value = "ERROR (" + jmxConnectionResponse.getException().getMessage() + ")";
        } else {
            value = jmxConnectionResponse.getValue().stream().map(this::display).collect(Collectors.joining(delimiter));
        }
        writer.writeLine(jmxConnectionResponse.getDisplay() + delimiter + value);

    }

    private String display(JMXValueResult jmxValueResult) {
        if (jmxValueResult.isError()) {
            return jmxValueResult.getException().getMessage();
        } else {
            return jmxValueResult.getValue().toString();
        }
    }

    public boolean isDisplayHelp(String[] args) {
        return args.length == 0 || isHelpOption(args);//todo write test?
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
