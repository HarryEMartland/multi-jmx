package uk.co.harrymartland.multijmx.domain.optionvalue.objectsignature;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.harrymartland.multijmx.domain.ObjectSignature;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.ObjectName;
import java.util.Arrays;
import java.util.Iterator;

@RunWith(MockitoJUnitRunner.class)
public class ObjectSignatureOptionValueImplTest {

    @Mock
    private SignatureOptionValue signatureOptionValue;
    @Mock
    private ObjectOptionValue objectOptionValue;
    @InjectMocks
    private ObjectSignatureOptionValue objectSignatureOptionValue = new ObjectSignatureOptionValueImpl(objectOptionValue, signatureOptionValue);

    @Test
    public void testShouldReturnObjectSignaturePairWhenOneObjectAndOneSignature() throws Exception {
        ObjectName objectName = createObjectName();
        JMXValueRetriever valueRetriever = createValueRetriever();
        givenObjectNames(objectName);
        givenSignatures(valueRetriever);
        thenShouldReturn(new ObjectSignature(valueRetriever, objectName));
        shouldPassValidation();
    }

    @Test
    public void testShouldReturnObjectSignaturePairWithDuplicateObjectWhenOneObjectAndMultipleSignatures() throws Exception {
        ObjectName objectName = createObjectName();
        JMXValueRetriever valueRetriever = createValueRetriever();
        JMXValueRetriever valueRetriever2 = createValueRetriever();
        givenObjectNames(objectName);
        givenSignatures(valueRetriever, valueRetriever2);
        thenShouldReturn(new ObjectSignature(valueRetriever, objectName), new ObjectSignature(valueRetriever2, objectName));
        shouldPassValidation();
    }

    @Test
    public void testShouldReturnObjectSignaturePairWithTwoPairs() throws Exception {
        ObjectName objectName = createObjectName();
        ObjectName objectName2 = createObjectName();
        JMXValueRetriever valueRetriever = createValueRetriever();
        JMXValueRetriever valueRetriever2 = createValueRetriever();
        givenObjectNames(objectName, objectName2);
        givenSignatures(valueRetriever, valueRetriever2);
        thenShouldReturn(new ObjectSignature(valueRetriever, objectName), new ObjectSignature(valueRetriever2, objectName2));
        shouldPassValidation();
    }

    @Test
    public void testShouldFailValidationWhenMoreObjectsThanSignatures() throws Exception {
        givenSignatures(createValueRetriever());
        givenObjectNames(createObjectName(), createObjectName());
        shouldFailValidation("Number of objects and signatures must match");
    }

    private void shouldFailValidation(String message) {
        try {
            objectSignatureOptionValue.validate();
        } catch (ValidationException e) {
            Assert.assertEquals(message, e.getMessage());
            return;
        }
        Assert.fail("Exception not thrown");
    }

    private void shouldPassValidation() {
        try {
            objectSignatureOptionValue.validate();
        } catch (ValidationException e) {
            Assert.fail("Failed Validation: " + e.getMessage());
        }
    }

    private JMXValueRetriever createValueRetriever() {
        return Mockito.mock(JMXValueRetriever.class);
    }

    private ObjectName createObjectName() {
        return Mockito.mock(ObjectName.class);
    }

    private void thenShouldReturn(ObjectSignature... objectSignatures) {
        Assert.assertEquals("Returned count incorrect", objectSignatures.length, objectSignatureOptionValue.getValue().size());

        Iterator<ObjectSignature> expectedIterator = Arrays.asList(objectSignatures).iterator();
        Iterator<ObjectSignature> actualIterator = objectSignatureOptionValue.getValue().iterator();

        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            ObjectSignature expected = expectedIterator.next();
            ObjectSignature actual = actualIterator.next();
            Assert.assertEquals(expected.getJmxValueRetriever(), actual.getJmxValueRetriever());
            Assert.assertEquals(expected.getObjectName(), actual.getObjectName());
        }
    }

    private void givenSignatures(JMXValueRetriever... jmxValueRetrievers) {
        Mockito.when(signatureOptionValue.getValue()).thenReturn(Arrays.asList(jmxValueRetrievers));
    }

    private void givenObjectNames(ObjectName... objectNames) {
        Mockito.when(objectOptionValue.getValue()).thenReturn(Arrays.asList(objectNames));
    }
}