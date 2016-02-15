package uk.co.harrymartland.multijmx.validator;

import com.google.inject.Inject;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;

import java.util.regex.Pattern;

import static uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl.*;

public class MultiJMXOptionValidatorImpl implements MultiJMXOptionValidator {

    ExpressionParser expressionParser;

    @Inject
    public MultiJMXOptionValidatorImpl(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public CommandLine validate(CommandLine commandLine) throws ValidationException {
        validateOrder(commandLine);
        validateObjectsAndAttributesCount(commandLine);
        validateMethodsAndAttributes(commandLine);
        return commandLine;
    }

    private void validateMethodsAndAttributes(CommandLine commandLine) throws ValidationException {
        for (String attribute : commandLine.getOptionValues(SIGNATURE_ARG)) {
            if (attribute.contains("(")) {
                validateMethod(attribute);
            } else {
                validateName(attribute);
            }
        }
    }

    private static final Pattern NAME_MATCH = Pattern.compile("[a-zA-Z\\d]*");

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

    private void validateObjectsAndAttributesCount(CommandLine commandLine) throws ValidationException {
        if (commandLine.getOptionValues(OBJECT_NAME_ARG).length > 1) {
            if (commandLine.getOptionValues(OBJECT_NAME_ARG).length != commandLine.getOptionValues(SIGNATURE_ARG).length) {
                throw new ValidationException("Number of attributes and objects must match");
            }
        }
    }

    private void validateOrder(CommandLine commandLine) throws ValidationException {
        if (commandLine.hasOption(ORDER_BY_CONNECTION_ARG) && commandLine.hasOption(ORDER_BY_VALUE_ARG)) {
            throw new ValidationException("Cannot order by connectionarg and display");
        }
    }

}
