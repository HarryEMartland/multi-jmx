package uk.co.harrymartland.multijmx;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.JMXValueResult;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.optionvalue.delimiter.DelimiterOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.help.HelpOptionValue;
import uk.co.harrymartland.multijmx.module.ArgumentModule;
import uk.co.harrymartland.multijmx.module.MultiJMXModule;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessor;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.service.lifecycle.LifeCycleService;
import uk.co.harrymartland.multijmx.service.options.OptionsService;
import uk.co.harrymartland.multijmx.service.validator.ValidatorService;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.Writer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


    private OptionsService optionsService;
    private MultiJMXProcessor multiJMXProcessor;
    private Writer writer;
    private Waitable waitable;
    private CommandLineService commandLineService;
    private ValidatorService validatorService;
    private DelimiterOptionValue delimiterOptionValue;
    private HelpOptionValue helpOptionValue;

    @Inject
    public Main(OptionsService optionsService, MultiJMXProcessor multiJMXProcessor, Writer writer, Waitable waitable,
                CommandLineService commandLineService, ValidatorService validatorService,
                DelimiterOptionValue delimiterOptionValue, HelpOptionValue helpOptionValue) {
        this.optionsService = optionsService;

        this.multiJMXProcessor = multiJMXProcessor;
        this.writer = writer;
        this.waitable = waitable;
        this.commandLineService = commandLineService;
        this.validatorService = validatorService;
        this.delimiterOptionValue = delimiterOptionValue;
        this.helpOptionValue = helpOptionValue;
    }

    public static void main(String[] args) throws ParseException, ValidationException {
        Injector injector = Guice.createInjector(new MultiJMXModule(), new ArgumentModule());
        Main main = injector.getInstance(Main.class);
        LifeCycleService lifeCycleService = injector.getInstance(LifeCycleService.class);
        try {
            lifeCycleService.birthAll();
            main.run(args);
        } finally {
            lifeCycleService.killAll();
        }
    }

    public void run(String[] args) throws ParseException, ValidationException {

        commandLineService.create(args);

        if (helpOptionValue.getValue()) {
            new HelpFormatter().printHelp("multi-jmx", optionsService.getOptions(), true);
        } else {

            validatorService.validate();

            List<Exception> errors = multiJMXProcessor.run()
                    .peek(jmxResponse -> display(jmxResponse, writer))
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


    private void display(JMXConnectionResponse jmxConnectionResponse, Writer writer) {
        String value;
        if (jmxConnectionResponse.isError()) {
            value = "ERROR (" + jmxConnectionResponse.getException().getMessage() + ")";
        } else {
            value = jmxConnectionResponse.getValue().stream().map(this::display).collect(Collectors.joining(delimiterOptionValue.getValue()));
        }
        writer.writeLine(jmxConnectionResponse.getDisplay() + delimiterOptionValue.getValue() + value);

    }

    private String display(JMXValueResult jmxValueResult) {
        if (jmxValueResult.isError()) {
            return jmxValueResult.getException().getMessage();
        } else {
            return jmxValueResult.getValue().toString();
        }
    }

}
