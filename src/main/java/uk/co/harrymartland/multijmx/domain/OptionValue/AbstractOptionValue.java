package uk.co.harrymartland.multijmx.domain.OptionValue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.util.Optional;

public abstract class AbstractOptionValue<T> implements OptionValue<T> {

    private CommandLineService commandLineService;
    private T value;
    private Option option;

    public AbstractOptionValue(CommandLineService commandLineService) {
        this.commandLineService = commandLineService;
    }

    protected CommandLine getCommandLine() {
        return commandLineService.get();
    }

    protected abstract String getArg();

    protected boolean hasOption() {
        return getCommandLine().hasOption(getArg());
    }

    protected abstract T lazyLoadValue();

    protected int getNumberOfValues() {
        return Optional.ofNullable(getCommandLine().getOptionValues(getArg()))
                .map(strings -> strings.length)
                .orElse(0);
    }

    protected abstract Option lazyLoadOption();

    protected boolean isRequired() {
        return false;
    }

    protected String getRequiredError() {
        return null;
    }


    @Override
    public boolean validate() throws ValidationException {
        if (isRequired()) {
            if (getNumberOfValues() < 1) {
                throw new ValidationException(getRequiredError());
            }
        }
        return true;
    }

    @Override
    public Option getOption() {
        if (option == null) {
            option = lazyLoadOption();
        }
        return option;
    }

    @Override
    public final T getValue() {
        if (value == null) {
            value = lazyLoadValue();
        }
        return value;
    }
}
