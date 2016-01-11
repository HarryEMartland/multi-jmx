package uk.co.harrymartland.multijmx.jmxrunner;

import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Map;

public class PasswordJmxRunner extends RemoteJmxRunner {

    private final String username;
    private final String password;

    public PasswordJmxRunner(JMXServiceURL url, ObjectName objectName, String attribute, String username, String password, String display) {
        super(url, objectName, attribute, display);
        this.username = username;
        this.password = password;
    }

    @Override
    protected Map<String, ?> getEnv() {
        Map<String, String[]> env = new HashMap<>();
        String[] credentials = {username, password};
        env.put(JMXConnector.CREDENTIALS, credentials);
        return env;
    }

}
