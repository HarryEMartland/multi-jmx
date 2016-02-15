package uk.co.harrymartland.multijmx.domain.optionvalue.signature;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractMultiOptionValue;
import uk.co.harrymartland.multijmx.domain.typeable.ObjectType;
import uk.co.harrymartland.multijmx.domain.typeable.Typeable;
import uk.co.harrymartland.multijmx.domain.valueretriver.AttributeValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.SpelMethodValueRetriever;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class SignatureOptionValueImpl extends AbstractMultiOptionValue<List<JMXValueRetriever>> implements SignatureOptionValue {

    private static final Pattern NAME_MATCH = Pattern.compile("[a-zA-Z\\d]*");

    private ExpressionParser expressionParser;

    @Inject
    public SignatureOptionValueImpl(CommandLineService commandLineService, ExpressionParser expressionParser) {
        super(commandLineService);
        this.expressionParser = expressionParser;
    }

    @Override
    public String getArg() {
        return "a";
    }

    @Override
    protected boolean isRequired() {
        return true;
    }

    @Override
    protected String getRequiredError() {
        return "No signature provided";
    }

    @Override
    protected Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("signature")
                .argName("signature")
                .required()
                .hasArg()
                .desc("JMX signature to read from e.g. 'AvailableProcessors'")
                .build();
    }

    @Override
    public boolean validate() throws ValidationException {
        super.validate();
        for (String signature : getStringValues()) {
            if (signature.contains("(")) {
                validateMethod(signature);
            } else {
                validateName(signature);
            }
        }
        return true;
    }

    @Override
    public List<JMXValueRetriever> lazyLoadValue() {
        return Arrays.stream(getStringValues()).map(this::createRetriever).collect(Collectors.toList());
    }

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

    private void validateName(String name) throws ValidationException {
        if (!NAME_MATCH.matcher(name).matches()) {
            throw new ValidationException("Name must not contain special characters: " + name);
        }
    }

    private void validateMethod(final String signature) throws ValidationException {

        if (StringUtils.countMatches(signature, "(") != StringUtils.countMatches(signature, ")")
                || !StringUtils.endsWith(signature, ")")
                || StringUtils.startsWith(signature, "(")) {
            throw new ValidationException("Method signature should match methodName(arg1,arg2): " + signature);
        }

        final int firstBracket = signature.indexOf("(");

        final String methodName = signature.substring(0, firstBracket);
        final String parameters = signature.substring(firstBracket + 1, signature.length() - 1);

        validateName(methodName);

        if (StringUtils.isNotBlank(parameters)) {
            for (final String argString : parameters.split(",")) {
                validateArguments(argString);
            }
        }
    }

    private void validateArguments(String argString) throws ValidationException {
        try {
            expressionParser.parseExpression(argString).getValue();
        } catch (Exception e) {
            throw new ValidationException("Incorrect argument parameter: " + argString, e);
        }
    }
}
