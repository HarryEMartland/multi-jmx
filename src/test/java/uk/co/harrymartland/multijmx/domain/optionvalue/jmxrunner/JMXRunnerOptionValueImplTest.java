package uk.co.harrymartland.multijmx.domain.optionvalue.jmxrunner;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.domain.optionvalue.connection.ConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.jmxrunner.PasswordJmxRunner;
import uk.co.harrymartland.multijmx.jmxrunner.RemoteJmxRunner;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JMXRunnerOptionValueImplTest extends AbstractOptionValueTest<JMXRunnerOptionValueImpl, List<RemoteJmxRunner>> {

    private UserNameOptionValue userNameOptionValue = Mockito.mock(UserNameOptionValue.class);
    private PasswordOptionValue passwordOptionValue = Mockito.mock(PasswordOptionValue.class);
    private SignatureOptionValue signatureOptionValue = Mockito.mock(SignatureOptionValue.class);
    private ObjectOptionValue objectOptionValue = Mockito.mock(ObjectOptionValue.class);
    private ConnectionOptionValue connectionOptionValue = Mockito.mock(ConnectionOptionValue.class);//look into using annotations

    @Test
    public void testShouldReturnPasswordJMXRunnersWithUsernameAndPasswordSetWhenUsernameAndPasswordAreProvider() throws Exception {
        givenUserName("username");
        givenPassword("password");
        givenConnectionOptionValues(1);
        thenShouldHaveUsernameAndPasswordSet();
        thenShouldReturnRunnersOfType(PasswordJmxRunner.class, 1);
    }

    @Test
    public void testShouldReturnMultiplePasswordJMXRunnersWithUsernameAndPasswordSetWhenUsernameAndPasswordAreProvider() throws Exception {
        givenUserName("username");
        givenPassword("password");
        givenConnectionOptionValues(4);
        thenShouldHaveUsernameAndPasswordSet();
        thenShouldReturnRunnersOfType(PasswordJmxRunner.class, 4);
    }

    private void thenShouldHaveUsernameAndPasswordSet() {
        getOptionValue().getValue().stream().forEach(remoteJmxRunner -> {
            Assert.assertEquals("username", reflectionGet(remoteJmxRunner, "username"));
            Assert.assertEquals("password", reflectionGet(remoteJmxRunner, "password"));
        });
    }

    private <T> T reflectionGet(Object object, String fieldName) {
        Field field = ReflectionUtils.findField(object.getClass(), fieldName);
        ReflectionUtils.makeAccessible(field);
        //noinspection unchecked
        return (T) ReflectionUtils.getField(field, object);
    }

    @Test
    public void testShouldReturnRemoteJmxRunnersWhenNoUserNameOrPasswordSet() throws Exception {
        givenConnectionOptionValues(1);
        thenShouldReturnRunnersOfType(RemoteJmxRunner.class, 1);
        thenShouldPassValidation();
    }

    @Test
    public void testShouldReturnMultipleRemoteJmxRunnersWhenNoUserNameOrPasswordSet() throws Exception {
        givenConnectionOptionValues(4);
        thenShouldReturnRunnersOfType(RemoteJmxRunner.class, 4);
        thenShouldPassValidation();
    }

    private void givenConnectionOptionValues(int count) {
        List<JMXConnection> toReturn = Stream.generate(() -> Mockito.mock(JMXConnection.class)).limit(count).collect(Collectors.toList());
        Mockito.when(connectionOptionValue.getValue()).thenReturn(toReturn);
    }

    private void thenShouldReturnRunnersOfType(Class remoteJmxRunnerClass, int expectedSize) {
        Assert.assertEquals(expectedSize, getOptionValue().getValue().size());
        getOptionValue().getValue().stream().forEach(remoteJmxRunner -> Assert.assertTrue(remoteJmxRunner.getClass().equals(remoteJmxRunnerClass)));
    }

    private void givenUserName(String username) {
        Mockito.when(userNameOptionValue.getValue()).thenReturn(username);
    }

    private void givenPassword(String password) {
        Mockito.when(passwordOptionValue.getValue()).thenReturn(password);
    }

    @Override
    protected JMXRunnerOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new JMXRunnerOptionValueImpl(commandLineService, userNameOptionValue, passwordOptionValue, signatureOptionValue, objectOptionValue, connectionOptionValue);
    }
}