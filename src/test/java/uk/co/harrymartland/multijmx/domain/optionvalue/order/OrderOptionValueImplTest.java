package uk.co.harrymartland.multijmx.domain.optionvalue.order;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import uk.co.harrymartland.multijmx.domain.JMXConnectionResponse;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.lang.reflect.Field;
import java.util.Comparator;

public class OrderOptionValueImplTest extends AbstractOptionValueTest<OrderOptionValueImpl, Comparator<JMXConnectionResponse>> {


    private OrderValueOptionValue orderValueOptionValue = Mockito.mock(OrderValueOptionValue.class);
    private OrderConnectionOptionValue orderConnectionOptionValue = Mockito.mock(OrderConnectionOptionValue.class);
    private ReverseOrderOptionValue reverseOrderOptionValue = Mockito.mock(ReverseOrderOptionValue.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.when(orderValueOptionValue.getValue()).thenReturn(false);
        Mockito.when(orderConnectionOptionValue.getValue()).thenReturn(false);
        Mockito.when(reverseOrderOptionValue.getValue()).thenReturn(false);
    }

    @Override
    protected OrderOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new OrderOptionValueImpl(commandLineService, orderConnectionOptionValue, orderValueOptionValue, reverseOrderOptionValue);
    }

    @Test
    public void testShouldReturnOrderValueComparatorWhenIsOrderByValue() throws Exception {
        setOrderByValue();
        thenShouldReturnValueComparator();
        thenShouldPassValidation();
    }

    @Test
    public void testShouldReturnNullWhenNoOrder() throws Exception {
        Assert.assertNull(getOptionValue().getValue());
        thenShouldPassValidation();
    }

    @Test
    public void testShouldReturnOrderConnectionWhenOrderByConnection() throws Exception {
        setOrderByConnection();
        thenShouldReturnConnectionComparator();
        thenShouldPassValidation();
    }

    @Test
    public void testShouldBeReversedAndValueComparatorWhenReversedAndOrderByValue() throws Exception {
        setOrderByValue();
        setReversedOrder();
        thenShouldReturnValueComparator();
        thenShouldBeReversed();
        thenShouldPassValidation();
    }

    @Test
    public void testShouldThrowValidationExceptionWhenOrderValueAndConnection() throws Exception {
        setOrderByValue();
        setOrderByConnection();
        thenShouldThrowValidationExceptionOnValidation("Cannot order by connection and display");
    }

    @Test
    public void testShouldBeReversedAndConnectionOrderWhenReversedAndOrderByConnection() throws Exception {
        setOrderByConnection();
        setReversedOrder();
        thenShouldReturnConnectionComparator();
        thenShouldBeReversed();
    }

    private void thenShouldBeReversed() {
        Field reversedField = ReflectionUtils.findField(JMXConnectionResponse.AbstractComparator.class, "reversed");
        ReflectionUtils.makeAccessible(reversedField);
        Boolean reversed = (Boolean) ReflectionUtils.getField(reversedField, getOptionValue().getValue());
        Assert.assertTrue(reversed);
    }

    private void thenShouldReturnConnectionComparator() {
        Assert.assertTrue(getOptionValue().getValue() instanceof JMXConnectionResponse.DisplayComparator);
    }

    private void thenShouldReturnValueComparator() {
        Assert.assertTrue(getOptionValue().getValue() instanceof JMXConnectionResponse.ValueComparator);
    }

    private void setOrderByValue() {
        Mockito.when(orderValueOptionValue.getValue()).thenReturn(true);
    }

    private void setOrderByConnection() {
        Mockito.when(orderConnectionOptionValue.getValue()).thenReturn(true);
    }

    private void setReversedOrder() {
        Mockito.when(reverseOrderOptionValue.getValue()).thenReturn(true);
    }
}