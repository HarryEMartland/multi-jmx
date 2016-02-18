package uk.co.harrymartland.multijmx.domain.optionvalue.order;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.util.Comparator;

public class OrderOptionValueImpl extends AbstractOptionValue<Comparator<JMXConnectionResponse>> implements OrderOptionValue {

    private OrderConnectionOptionValue orderConnectionOptionValue;
    private OrderValueOptionValue orderValueOptionValue;
    private ReverseOrderOptionValue reverseOrderOptionValue;

    @Inject
    public OrderOptionValueImpl(CommandLineService commandLineService, OrderConnectionOptionValue orderConnectionOptionValue,
                                OrderValueOptionValue orderValueOptionValue, ReverseOrderOptionValue reverseOrderOptionValue) {
        super(commandLineService);
        this.orderConnectionOptionValue = orderConnectionOptionValue;
        this.orderValueOptionValue = orderValueOptionValue;
        this.reverseOrderOptionValue = reverseOrderOptionValue;
    }

    @Override
    protected Comparator<JMXConnectionResponse> lazyLoadValue() {
        Comparator<JMXConnectionResponse> comparator = null;

        if (orderConnectionOptionValue.getValue()) {
            comparator = new JMXConnectionResponse.DisplayComparator();
        }
        if (orderValueOptionValue.getValue()) {
            comparator = new JMXConnectionResponse.ValueComparator();
        }
        if (comparator != null && reverseOrderOptionValue.getValue()) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        if (orderConnectionOptionValue.getValue() && orderValueOptionValue.getValue()) {
            throw new ValidationException("Cannot order by connection and display");
        }
    }

    @Override
    protected Option lazyLoadOption() {
        return null;
    }

    @Override
    public String getArg() {
        return null;
    }
}
