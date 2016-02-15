package uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

@Singleton
public class OrderValueOptionValueImpl extends AbstractSingleOptionValue<Boolean> implements OrderValueOptionValue {

    @Inject
    public OrderValueOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    protected String getMultipleArgumentError() {
        return "Order value specified twice";
    }

    @Override
    public Option lazyLoadOption() {
        return Option.builder(getArg()).longOpt("order-value").desc("Order the results by value").build();
    }

    @Override
    protected String getArg() {
        return "v";
    }

    @Override
    public Boolean lazyLoadValue() {
        return hasOption();
    }
}
