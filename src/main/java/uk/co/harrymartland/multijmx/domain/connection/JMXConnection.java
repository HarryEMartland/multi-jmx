package uk.co.harrymartland.multijmx.domain.connection;

import javax.management.remote.JMXServiceURL;

public class JMXConnection {

    private String display;
    private JMXServiceURL jmxServiceURL;

    public JMXConnection(String display, JMXServiceURL jmxServiceURL) {
        this.display = display;
        this.jmxServiceURL = jmxServiceURL;
    }

    public String getDisplay() {
        return display;
    }

    public JMXServiceURL getJmxServiceURL() {
        return jmxServiceURL;
    }
}
