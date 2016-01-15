package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.management.ConnectorAddressLink;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXArgumentParserImpl implements MultiJMXArgumentParser {

    private Options options = null;

    @Override
    public MultiJMXOptions parseArguments(String[] args) throws ParseException {

        CommandLine cmd = new DefaultParser().parse(getOptions(), args);
        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();

        multiJMXOptions.setAttribute(cmd.getOptionValue("a"));
        multiJMXOptions.setMaxThreads(getMaxThreads(cmd));
        multiJMXOptions.setObjectName(createObjectName(cmd.getOptionValue("o")));
        multiJMXOptions.setOrderDisplay(cmd.hasOption("d"));
        multiJMXOptions.setOrderValue(cmd.hasOption("v"));
        multiJMXOptions.setReverseOrder(cmd.hasOption("r"));
        multiJMXOptions.setAttribute(cmd.getOptionValue("a"));
        multiJMXOptions.setPassword(cmd.getOptionValue("p"));
        multiJMXOptions.setUsername(cmd.getOptionValue("u"));
        multiJMXOptions.setUrls(createConnections(cmd));

        return multiJMXOptions;
    }

    private List<JMXConnection> createConnections(CommandLine cmd) {

        Stream<String> connectionStrings = cmd.getArgList().stream();

        if (cmd.hasOption("f")) {
            Stream<String> connectionsFromFile = loadConnectsFromFile(cmd.getOptionValue("f"));
            connectionStrings = Stream.concat(connectionsFromFile, connectionStrings);
        }

        return connectionStrings.map(this::createJMXConnection).collect(Collectors.toList());
    }

    private Stream<String> loadConnectsFromFile(String fileName) {
        try {
            return Files.lines(new File(fileName).toPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading connection file", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not find connection file", e);
        }
    }

    private ObjectName createObjectName(String objectName) {
        try {
            return new ObjectName(objectName);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Could not parse object name: " + objectName, e);
        }
    }

    private Integer getMaxThreads(CommandLine cmd) {
        if (cmd.hasOption("t")) {
            return Integer.parseInt(cmd.getOptionValue("t"));
        }
        return null;
    }

    @Override
    public Options getOptions() {
        if (options == null) {
            options = createOptions();
        }
        return options;
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption("v", "order-value", false, "Order the results by value");
        options.addOption("d", "order-display", false, "Order the results by display");
        options.addOption("r", "reverse-order", false, "Order the results in reverse");
        options.addOption("t", "max-threads", true, "Maximum number of threads (default unlimited)");
        options.addOption(Option.builder("o").longOpt("object-name").argName("object name").required().hasArg().desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'").build());
        options.addOption(Option.builder("a").longOpt("attribute").argName("attribute").required().hasArg().desc("JMX attribute to read from e.g. 'AvailableProcessors'").build());
        options.addOption("u", "username", true, "Username to connect to JMX server");
        options.addOption("p", "password", true, "Password to connect to JMX server");
        options.addOption("f", "file", true, "Read JMX connections from file");
        options.addOption(Option.builder(HELP_SHORT_OPTION).longOpt(HELP_LONG_OPTION).hasArg(false).desc("Desplay help").build());
        return options;
    }

    private JMXConnection createJMXConnection(String url) {
        try {
            if (NumberUtils.isParsable(url)) {
                String s = ConnectorAddressLink.importFrom(Integer.parseInt(url));
                if (StringUtils.isBlank(s)) {
                    throw new RuntimeException("No process found for id: " + url);
                }
                return new JMXConnection("Process: " + url, new JMXServiceURL(s));
            } else {
                JMXServiceURL jmxServiceURL = new JMXServiceURL(url);
                return new JMXConnection(jmxServiceURL.getHost(), jmxServiceURL);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("Url is not parsable as a number", e);
        }
    }
}
