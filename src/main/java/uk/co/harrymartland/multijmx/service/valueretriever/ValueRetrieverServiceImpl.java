package uk.co.harrymartland.multijmx.service.valueretriever;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import uk.co.harrymartland.multijmx.domain.typeable.ObjectType;
import uk.co.harrymartland.multijmx.domain.typeable.Typeable;
import uk.co.harrymartland.multijmx.domain.valueretriver.AttributeValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.SpelMethodValueRetriever;

public class ValueRetrieverServiceImpl implements ValueRetrieverService {

    private ExpressionParser expressionParser;

    @Inject
    public ValueRetrieverServiceImpl(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public JMXValueRetriever createRetriever(String signature) {
        if (signature.contains("(")) {
            return createMethodValueRetriever(signature);
        } else {
            return createAttributeValueRetriever(signature);
        }
    }

    private JMXValueRetriever createAttributeValueRetriever(String signature) {
        return new AttributeValueRetriever(signature);
    }

    private JMXValueRetriever createMethodValueRetriever(String signature) {
        final int firstOpenBracket = signature.indexOf("(");
        final String methodName = signature.substring(0, firstOpenBracket);
        final String argumentsString = signature.substring(firstOpenBracket + 1, signature.length() - 1);
        final String[] arguments = StringUtils.split(argumentsString, ",");
        final Typeable[] argumentValues = new Typeable[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            Object returnedValue = expressionParser.parseExpression(argument).getValue();
            if (returnedValue instanceof Typeable) {
                argumentValues[i] = (Typeable) returnedValue;
            } else {
                argumentValues[i] = new ObjectType(returnedValue);
            }
        }
        return new SpelMethodValueRetriever(methodName, argumentValues);
    }
}
