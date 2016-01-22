package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiJMXArgumentParserImpl implements MultiJMXArgumentParser {

    private static final String DELEMITER_DEFAULT = "\t";

    private Options options = null;

    @Override
    public MultiJMXOptions parseArguments(CommandLine cmd) throws ParseException {

        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();

        multiJMXOptions.setAttributes(Arrays.asList(cmd.getOptionValues("a")));
        multiJMXOptions.setMaxThreads(getMaxThreads(cmd));
        multiJMXOptions.setObjectNames(createObjectNames(cmd.getOptionValues("o")));
        multiJMXOptions.setOrderConnection(cmd.hasOption("c"));
        multiJMXOptions.setOrderValue(cmd.hasOption("v"));
        multiJMXOptions.setReverseOrder(cmd.hasOption("r"));
        multiJMXOptions.setPassword(cmd.getOptionValue("p"));
        multiJMXOptions.setUsername(cmd.getOptionValue("u"));
        multiJMXOptions.setUrls(createConnections(cmd));
        multiJMXOptions.setDelimiter(cmd.getOptionValue("d", DELEMITER_DEFAULT));

        return multiJMXOptions;
    }

    private List<ObjectName> createObjectNames(String[] options) {
        return Arrays.asList(options).stream().map(this::createObjectName).collect(Collectors.toList());
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
        options.addOption("c", "order-connection", false, "Order the results by connection");
        options.addOption("r", "reverse-order", false, "Order the results in reverse");
        options.addOption("t", "max-threads", true, "Maximum number of threads (default unlimited)");
        options.addOption(Option.builder("o").longOpt("object-name").argName("object name").required().hasArg().desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'").build());
        options.addOption(Option.builder("a").longOpt("attribute").argName("attribute").required().hasArg().desc("JMX attribute to read from e.g. 'AvailableProcessors'").build());
        options.addOption("u", "username", true, "Username to connect to JMX server");
        options.addOption("p", "password", true, "Password to connect to JMX server");
        options.addOption("f", "file", true, "Read JMX connections from file");
        options.addOption(Option.builder(HELP_SHORT_OPTION).longOpt(HELP_LONG_OPTION).hasArg(false).desc("Desplay help").build());
        options.addOption(Option.builder("d").longOpt("delimiter").hasArg().numberOfArgs(1).desc("Delemiter used to split results").build());
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
