package uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.math.NumberUtils;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class MaxThreadsOptionValueImpl extends AbstractSingleOptionValue<Integer> implements MaxThreadsOptionValue {

    @Inject
    public MaxThreadsOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Max threads specified twice";
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        if (hasOption() && !NumberUtils.isParsable(getStringValue())) {
            throw new ValidationException("Could not parse max threads: " + getStringValue());
        }
    }

    @Override
    public String getArg() {
        return "t";
    }

    @Override
    public Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("max-threads")
                .hasArg()
                .argName("number of threads")
                .desc("Maximum number of threads (default unlimited)")
                .build();
    }

    @Override
    public Integer lazyLoadValue() {
        return Integer.parseInt(getStringValue());
    }
}
