package uk.co.harrymartland.multijmx.domain.optionvalue;

import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public abstract class AbstractSingleOptionValue<T> extends AbstractOptionValue<T> {

    public AbstractSingleOptionValue(CommandLineService commandLineService) {
        super(commandLineService);
    }

    protected String getStringValue() {
        return getCommandLine().getOptionValue(getArg());
    }

    @Override
    public boolean validate() throws ValidationException {
        super.validate();
        if (getNumberOfValues() > 1) {
            throw new ValidationException(getMultipleArgumentError());
        }
        return true;
    }

    protected abstract String getMultipleArgumentError();

}
