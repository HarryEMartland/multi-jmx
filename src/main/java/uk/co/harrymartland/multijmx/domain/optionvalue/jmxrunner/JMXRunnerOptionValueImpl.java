package uk.co.harrymartland.multijmx.domain.optionvalue.jmxrunner;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connection.ConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.util.List;
import java.util.stream.Collectors;

public class JMXRunnerOptionValueImpl extends AbstractOptionValue<List<RemoteJmxRunner>> implements JMXRunnerOptionValue {

    private UserNameOptionValue userNameOptionValue;
    private PasswordOptionValue passwordOptionValue;
    private SignatureOptionValue signatureOptionValue;
    private ObjectOptionValue objectOptionValue;
    private ConnectionOptionValue connectionOptionValue;

    @Inject
    public JMXRunnerOptionValueImpl(CommandLineService commandLineService, UserNameOptionValue userNameOptionValue, PasswordOptionValue passwordOptionValue, SignatureOptionValue signatureOptionValue, ObjectOptionValue objectOptionValue, ConnectionOptionValue connectionOptionValue) {
        super(commandLineService);
        this.userNameOptionValue = userNameOptionValue;
        this.passwordOptionValue = passwordOptionValue;
        this.signatureOptionValue = signatureOptionValue;
        this.objectOptionValue = objectOptionValue;
        this.connectionOptionValue = connectionOptionValue;
    }

    @Override
    protected List<RemoteJmxRunner> lazyLoadValue() {
        return connectionOptionValue.getValue().stream()
                .map(this::createJmxRunner)
                .collect(Collectors.toList());
    }

    @Override
    protected Option lazyLoadOption() {
        return null;
    }

    private RemoteJmxRunner createJmxRunner(JMXConnection connection) {
        if (userNameOptionValue.getValue() != null && passwordOptionValue.getValue() != null) {
            return new PasswordJmxRunner(signatureOptionValue.getValue(), objectOptionValue.getValue(), connection, userNameOptionValue.getValue(), passwordOptionValue.getValue());
        } else {
            return new RemoteJmxRunner(signatureOptionValue.getValue(), objectOptionValue.getValue(), connection);
        }
    }

    @Override
    public String getArg() {
        return null;
    }
}
