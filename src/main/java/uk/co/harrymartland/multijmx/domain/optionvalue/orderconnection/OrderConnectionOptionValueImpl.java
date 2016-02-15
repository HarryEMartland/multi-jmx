package uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class OrderConnectionOptionValueImpl extends AbstractSingleOptionValue<Boolean> implements OrderConnectionOptionValue {

    @Inject
    public OrderConnectionOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Order by connectionarg is specified twice";
    }

    @Override
    public Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("order-connectionarg")
                .desc("Order the results by connectionarg")
                .build();
    }

    @Override
    public String getArg() {
        return "c";
    }

    @Override
    public Boolean lazyLoadValue() {
        return hasOption();
    }
}
