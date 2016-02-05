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

    private static final String DELIMITER_DEFAULT = "\t";
    public static final String DELIMITER_ARG = "d";
    public static final String USERNAME_ARG = "u";
    public static final String PASSWORD_ARG = "p";
    public static final String REVERSE_ORDER_ARG = "r";
    public static final String ORDER_BY_VALUE_ARG = "v";
    public static final String ORDER_BY_CONNECTION_ARG = "c";
    public static final String OBJECT_NAME_ARG = "o";
    public static final String SIGNATURE_ARG = "a";
    public static final String CONNECTIONS_FILE_ARG = "f";
    public static final String MAX_THREADS_ARG = "t";

    private Options options = null;

    @Override
    public MultiJMXOptions parseArguments(CommandLine cmd) throws ParseException {

        MultiJMXOptions multiJMXOptions = new MultiJMXOptions();

        multiJMXOptions.setSignatures(Arrays.asList(cmd.getOptionValues(SIGNATURE_ARG)));
        multiJMXOptions.setMaxThreads(getMaxThreads(cmd));
        multiJMXOptions.setObjectNames(createObjectNames(cmd.getOptionValues(OBJECT_NAME_ARG)));
        multiJMXOptions.setOrderConnection(cmd.hasOption(ORDER_BY_CONNECTION_ARG));
        multiJMXOptions.setOrderValue(cmd.hasOption(ORDER_BY_VALUE_ARG));
        multiJMXOptions.setReverseOrder(cmd.hasOption(REVERSE_ORDER_ARG));
        multiJMXOptions.setPassword(cmd.getOptionValue(PASSWORD_ARG));
        multiJMXOptions.setUsername(cmd.getOptionValue(USERNAME_ARG));
        multiJMXOptions.setUrls(createConnections(cmd));
        multiJMXOptions.setDelimiter(cmd.getOptionValue(DELIMITER_ARG, DELIMITER_DEFAULT));

        return multiJMXOptions;
    }

    private List<ObjectName> createObjectNames(String[] options) {
        return Arrays.asList(options).stream().map(this::createObjectName).collect(Collectors.toList());
    }

    private List<JMXConnection> createConnections(CommandLine cmd) {

        Stream<String> connectionStrings = cmd.getArgList().stream();

        if (cmd.hasOption(CONNECTIONS_FILE_ARG)) {
            Stream<String> connectionsFromFile = loadConnectsFromFile(cmd.getOptionValue(CONNECTIONS_FILE_ARG));
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
        if (cmd.hasOption(MAX_THREADS_ARG)) {
            return Integer.parseInt(cmd.getOptionValue(MAX_THREADS_ARG));
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
        options.addOption(ORDER_BY_VALUE_ARG, "order-value", false, "Order the results by value");
        options.addOption(ORDER_BY_CONNECTION_ARG, "order-connection", false, "Order the results by connection");
        options.addOption(REVERSE_ORDER_ARG, "reverse-order", false, "Order the results in reverse");
        options.addOption(MAX_THREADS_ARG, "max-threads", true, "Maximum number of threads (default unlimited)");
        options.addOption(Option.builder(OBJECT_NAME_ARG).longOpt("object-name").argName("object name").required().hasArg().desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'").build());
        options.addOption(Option.builder(SIGNATURE_ARG).longOpt("attribute").argName("attribute").required().hasArg().desc("JMX attribute to read from e.g. 'AvailableProcessors'").build());
        options.addOption(USERNAME_ARG, "username", true, "Username to connect to JMX server");
        options.addOption(PASSWORD_ARG, "password", true, "Password to connect to JMX server");
        options.addOption(CONNECTIONS_FILE_ARG, "file", true, "Read JMX connections from file");
        options.addOption(Option.builder(HELP_SHORT_OPTION).longOpt(HELP_LONG_OPTION).hasArg(false).desc("Display help").build());
        options.addOption(Option.builder(DELIMITER_ARG).longOpt("delimiter").hasArg().numberOfArgs(1).desc("Delimiter used to split results").build());
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
