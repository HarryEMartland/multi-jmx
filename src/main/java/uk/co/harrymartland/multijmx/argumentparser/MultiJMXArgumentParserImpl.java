package uk.co.harrymartland.multijmx.argumentparser;

import com.google.inject.Inject;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private ConnectionService connectionService;

    @Inject
    public MultiJMXArgumentParserImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

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
            throw new RuntimeException("Error reading connectionarg file", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not find connectionarg file", e);
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
        options.addOption(Option.builder(ORDER_BY_VALUE_ARG).longOpt("order-value").desc("Order the results by value").build());
        options.addOption(Option.builder(ORDER_BY_CONNECTION_ARG).longOpt("order-connectionarg").desc("Order the results by connectionarg").build());
        options.addOption(Option.builder(REVERSE_ORDER_ARG).longOpt("reverse-order").desc("Order the results in reverse").build());
        options.addOption(Option.builder(MAX_THREADS_ARG).longOpt("max-threads").hasArg().argName("number of threads").desc("Maximum number of threads (default unlimited)").build());
        options.addOption(Option.builder(OBJECT_NAME_ARG).longOpt("object-name").argName("object name").required().hasArg().desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'").build());
        options.addOption(Option.builder(SIGNATURE_ARG).longOpt("signature").argName("signature").required().hasArg().desc("JMX signature to read from e.g. 'AvailableProcessors'").build());
        options.addOption(Option.builder(USERNAME_ARG).longOpt("username").hasArg().argName("username").desc("Username to connect to JMX server").build());
        options.addOption(Option.builder(PASSWORD_ARG).longOpt("password").hasArg().argName("password").desc("Password to connect to JMX server").build());
        options.addOption(Option.builder(CONNECTIONS_FILE_ARG).longOpt("file").hasArg().argName("file path").desc("Read JMX connections from file").build());
        options.addOption(Option.builder(HELP_SHORT_OPTION).longOpt(HELP_LONG_OPTION).hasArg(false).desc("Display help").build());
        options.addOption(Option.builder(DELIMITER_ARG).longOpt("delimiter").hasArg().numberOfArgs(1).argName("delimiter").desc("Delimiter used to split results").build());
        return options;
    }

    private JMXConnection createJMXConnection(String url) {
        return connectionService.createConnection(url);
    }
}
