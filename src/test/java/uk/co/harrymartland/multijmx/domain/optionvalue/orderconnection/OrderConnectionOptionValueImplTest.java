package uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection;

import org.apache.commons.cli.CommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class OrderConnectionOptionValueImplTest {

    private CommandLine commandLine = Mockito.mock(CommandLine.class);
    private CommandLineService commandLineService = Mockito.mock(CommandLineService.class);
    private OrderConnectionOptionValue orderConnectionOptionValue = new OrderConnectionOptionValueImpl(commandLineService);

    @Before
    public void setUp() {
        Mockito.when(commandLineService.get()).thenReturn(commandLine);

    }

    @Test
    public void testShouldNotThrowValidationExceptionWhenNoValueSet() throws Exception {
        try {
            orderConnectionOptionValue.validate();
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testShouldNotThrowExceptionWhenValueSet() throws Exception {
        Mockito.when(commandLine.hasOption("c")).thenReturn(true);
        try {
            orderConnectionOptionValue.validate();
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testShouldReturnTrueWhenValueSet() throws Exception {
        Mockito.when(commandLine.hasOption("c")).thenReturn(true);
        Assert.assertTrue(orderConnectionOptionValue.getValue());
    }

    @Test
    public void testShouldReturnFalseWhenValueSet() throws Exception {
        Mockito.when(commandLine.hasOption("c")).thenReturn(false);
        Assert.assertFalse(orderConnectionOptionValue.getValue());
    }
}