package uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads;

import org.junit.Test;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractSingleOptionValueTest;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

public class MaxThreadsOptionValueImplTest extends AbstractSingleOptionValueTest<MaxThreadsOptionValueImpl, Integer> {

    @Override
    protected MaxThreadsOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new MaxThreadsOptionValueImpl(commandLineService);
    }

    @Test
    public void testShouldNotThrowExceptionWhenNotSet() throws Exception {
        thenShouldPassValidation();
    }

    @Test
    public void testShouldNotThrowExceptionWhenValidInt() throws Exception {
        givenCommandLineArgument("1");
        thenShouldPassValidation();
    }

    @Test
    public void testShouldThrowValidationExceptionWhenInvalidInteger() throws Exception {
        givenCommandLineArgument("*");

    }

    @Test
    public void testShouldReturnIntegerWhenGetValueIsCalled() throws Exception {
        givenCommandLineArgument("3");
        thenShouldReturn(3);
    }

    @Override
    protected String getMoreThanOneArgumentError() {
        return "Max threads specified twice";
    }

    //todo connectionarg option value
    //todo connectionarg has to be one
}