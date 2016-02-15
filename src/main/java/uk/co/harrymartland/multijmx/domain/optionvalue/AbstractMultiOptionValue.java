package uk.co.harrymartland.multijmx.domain.optionvalue;

import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public abstract class AbstractMultiOptionValue<T> extends AbstractOptionValue<T> {

    public AbstractMultiOptionValue(CommandLineService commandLineService) {
        super(commandLineService);
    }

    protected String[] getStringValues() {
        return getCommandLine().getOptionValues(getArg());
    }
}
