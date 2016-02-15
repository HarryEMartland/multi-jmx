package uk.co.harrymartland.multijmx.jmxrunner;

import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordJmxRunner extends RemoteJmxRunner {

    private final String username;
    private final String password;

    public PasswordJmxRunner(List<JMXValueRetriever> attributes, List<ObjectName> objectNames, JMXConnection jmxConnection, String username, String password) {
        super(attributes, objectNames, jmxConnection);
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
