package uk.co.harrymartland.multijmx.argumentparser;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.management.ConnectorAddressLink;
import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

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
        multiJMXOptions.setAttribute(cmd.getOptionValue("a"));
        multiJMXOptions.setPassword(cmd.getOptionValue("p"));
        multiJMXOptions.setUsername(cmd.getOptionValue("u"));
        multiJMXOptions.setUrls(createUrls(cmd));

        return multiJMXOptions;
    }

    private List<JMXServiceURL> createUrls(CommandLine cmd) {
        return cmd.getArgList().stream().map(this::createJMXServiceURL).collect(Collectors.toList());
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
        options.addOption("t", "max-threads", true, "Maximum number of threads (default unlimited)");
        options.addOption(Option.builder("o").longOpt("object-name").argName("object name").required().hasArg().desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'").build());
        options.addOption(Option.builder("a").longOpt("attribute").argName("attribute").required().hasArg().desc("JMX attribute to read from e.g. 'AvailableProcessors'").build());
        options.addOption("u", "username", true, "Username to connect to JMX server");
        options.addOption("p", "password", true, "Password to connect to JMX server");
        options.addOption(Option.builder(HELP_SHORT_OPTION).longOpt(HELP_LONG_OPTION).hasArg(false).desc("Desplay help").build());
        return options;
    }

    private JMXServiceURL createJMXServiceURL(String url) {
        try {
            if (NumberUtils.isParsable(url)) {
                String s = ConnectorAddressLink.importFrom(Integer.parseInt(url));
                if (StringUtils.isBlank(s)) {
                    throw new RuntimeException("No process found for id: " + url);
                }
                return new JMXServiceURL(s);
            } else {
                return new JMXServiceURL(url);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("Url is not parsable as a number", e);
        }
    }
}
