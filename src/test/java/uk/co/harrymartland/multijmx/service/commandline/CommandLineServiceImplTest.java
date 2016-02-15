package uk.co.harrymartland.multijmx.service.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.service.options.OptionsServiceImpl;

import java.util.Collections;

public class CommandLineServiceImplTest {

    private CommandLineService commandLineService;

    @Before
    public void setUp() throws Exception {
        CommandLineParser commandLineParser = Mockito.mock(CommandLineParser.class);
        Mockito.when(commandLineParser.parse(Mockito.any(Options.class), Mockito.any(String[].class))).thenReturn(Mockito.mock(CommandLine.class));
        commandLineService = new CommandLineServiceImpl(commandLineParser, new OptionsServiceImpl(Collections.emptySet()));
    }

    @Test
    public void testShouldThrowExceptionWhenGettingWhenCommandLineNotSet() throws Exception {
        try {
            commandLineService.get();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Command line not set!", e.getMessage());
            return;
        }
        Assert.fail("No exception thrown");
    }

    @Test
    public void testShouldThrowExceptionWhenCommandLineAlreadySet() throws Exception {

        commandLineService.create(new String[]{});
        try {
            commandLineService.create(new String[]{});
        } catch (IllegalStateException e) {
            Assert.assertEquals("Command line already set!", e.getMessage());
            return;
        }
        Assert.fail("No exception thrown");
    }
}