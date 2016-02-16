package uk.co.harrymartland.multijmx.domain.optionvalue.objectsignature;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.ObjectSignature;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectSignatureOptionValueImpl implements ObjectSignatureOptionValue {

    private ObjectOptionValue objectOptionValue;
    private SignatureOptionValue signatureOptionValue;

    @Inject
    public ObjectSignatureOptionValueImpl(ObjectOptionValue objectOptionValue, SignatureOptionValue signatureOptionValue) {
        this.objectOptionValue = objectOptionValue;
        this.signatureOptionValue = signatureOptionValue;
    }

    @Override
    public Option getOption() {
        return null;
    }

    @Override
    public boolean validate() throws ValidationException {
        if (objectOptionValue.getValue().size() > 1 && objectOptionValue.getValue().size() != signatureOptionValue.getValue().size()) {
            throw new ValidationException("Number of objects and signatures must match");
        }
        return true;
    }

    @Override
    public List<ObjectSignature> getValue() {
        Iterator<ObjectName> objectNameIterator = objectOptionValue.getValue().iterator();
        Iterator<JMXValueRetriever> signatureIterator = signatureOptionValue.getValue().iterator();
        ObjectName objectName = objectNameIterator.next();

        List<ObjectSignature> objectSignatures = new ArrayList<>(signatureOptionValue.getValue().size());
        while (signatureIterator.hasNext()) {
            JMXValueRetriever valueRetriever = signatureIterator.next();
            objectSignatures.add(new ObjectSignature(valueRetriever, objectName));
            if (objectNameIterator.hasNext()) {
                objectName = objectNameIterator.next();
            }
        }
        return objectSignatures;
    }

    @Override
    public int getNumberOfValues() {
        return signatureOptionValue.getNumberOfValues();
    }

    @Override
    public String getArg() {
        return null;
    }
}
