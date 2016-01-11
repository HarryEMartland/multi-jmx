package uk.co.harrymartland.multijmx;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.management.ConnectorAddressLink;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class MultiJMX {

    public void run(String[] args) throws ParseException {

        CommandLine cmd = new DefaultParser().parse(createOptions(), args);

        ExecutorService executorService = createExecutorService(cmd.getOptionValue("t"));

        Stream<JMXResponse> objectStream = Arrays.asList(cmd.getArgs()).stream()
                .map(url -> requestObject(executorService, url,
                        cmd.getOptionValue("u"), cmd.getOptionValue("u"),
                        createObjectName(cmd.getOptionValue("o")), cmd.getOptionValue("a")))
                .map(this::getObject);

        sort(objectStream, cmd.hasOption("v"), cmd.hasOption("d"));
        objectStream.forEach(this::display);

        executorService.shutdownNow();
    }

    private void sort(Stream<JMXResponse> objectStream, boolean orderValue, boolean orderDisplay) {
        if (orderDisplay && orderValue) {
            throw new RuntimeException("Cannot order by value and display");
        } else if (orderDisplay) {
            objectStream.sorted(new JMXResponse.DisplayComparator());
        } else if (orderValue) {
            objectStream.sorted(new JMXResponse.ValueComparator());
        }
    }

    private Options createOptions() {
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

    private void display(JMXResponse jmxResponse) {
        System.out.printf("%s\t%s\n", jmxResponse.getDisplay(), jmxResponse.getValue());
    }

    private ExecutorService createExecutorService(String maxInt) {
        if (StringUtils.isNotBlank(maxInt) && NumberUtils.isParsable(maxInt)) {
            return Executors.newFixedThreadPool(Integer.parseInt(maxInt));
        } else {
            return Executors.newCachedThreadPool();
        }
    }

    private ObjectName createObjectName(String objectName) {
        try {
            return new ObjectName(objectName);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Invalid Object Name", e);
        }
    }

    private <T> T getObject(Future<T> objectFuture) {
        try {
            return objectFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();//todo logger
            return null;
        }
    }

    private Future<JMXResponse> requestObject(ExecutorService executorService, String url, String username, String password, ObjectName objectName, String attribute) {
        return executorService.submit(createJmxRunner(url, username, password, objectName, attribute));
    }

    private JMXServiceURL createJMXServiceURL(String url) {
        try {
            if (NumberUtils.isParsable(url)) {
                return new JMXServiceURL(ConnectorAddressLink.importFrom(Integer.parseInt(url)));
            } else {
                return new JMXServiceURL(url);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url", e);
        } catch (IOException e) {
            throw new RuntimeException("Url is not parsable as a number", e);
        }
    }

    private RemoteJmxRunner createJmxRunner(String url, String username, String password, ObjectName objectName, String attribute) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            return new PasswordJmxRunner(createJMXServiceURL(url), objectName, attribute, username, password, "Process: " + url);
        } else {
            JMXServiceURL jmxServiceURL = createJMXServiceURL(url);
            return new RemoteJmxRunner(jmxServiceURL, objectName, attribute, getDisplay(url));
        }
    }

    private String getDisplay(String url) {
        if (NumberUtils.isDigits(url)) {
            return "Process: " + url;
        } else {
            return url;
        }
    }
}
