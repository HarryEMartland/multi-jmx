package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.management.ConnectorAddressLink;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws ParseException {

        CommandLine cmd = new DefaultParser().parse(createOptions(), args);
        MultiJMXOptions options = new MultiJMXOptions();
        options.setAttribute(cmd.getOptionValue("a"));
        options.setMaxThreads(getMaxThreads(cmd));
        options.setObjectName(createObjectName(cmd.getOptionValue("o")));
        options.setOrderDisplay(cmd.hasOption("d"));
        options.setOrderValue(cmd.hasOption("v"));
        options.setAttribute(cmd.getOptionValue("a"));
        options.setPassword(cmd.getOptionValue("p"));
        options.setUsername(cmd.getOptionValue("u"));
        options.setUrls(createUrls(cmd));

        new MultiJMX().run(options);
    }

    private static List<JMXServiceURL> createUrls(CommandLine cmd) {
        return cmd.getArgList().stream().map(Main::createJMXServiceURL).collect(Collectors.toList());
    }

    private static ObjectName createObjectName(String objectName) {
        try {
            return new ObjectName(objectName);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Could not parse object name", e);
        }
    }

    private static Integer getMaxThreads(CommandLine cmd) {
        if (cmd.hasOption("t")) {
            return Integer.parseInt(cmd.getOptionValue("t"));
        }
        return null;
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption("v", "order-value", false, "Order the results by value");
        options.addOption("d", "order-display", false, "Order the results by display");
        options.addOption("t", "max-threads", true, "Maximum number of threads (default unlimited)");
        options.addOption(Option.builder("o").argName("object-name").required().hasArg().desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'").build());
        options.addOption(Option.builder("a").argName("attribute").required().hasArg().desc("JMX attribute to read from e.g. 'AvailableProcessors'").build());
        options.addOption("u", "username", true, "Username to connect to JMX server");
        options.addOption("p", "password", true, "Password to connect to JMX server");
        return options;
    }

    private static JMXServiceURL createJMXServiceURL(String url) {
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
            throw new RuntimeException("Invalid url", e);
        } catch (IOException e) {
            throw new RuntimeException("Url is not parsable as a number", e);
        }
    }
}
