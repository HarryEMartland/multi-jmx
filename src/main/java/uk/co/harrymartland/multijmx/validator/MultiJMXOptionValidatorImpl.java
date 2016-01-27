package uk.co.harrymartland.multijmx.validator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import uk.co.harrymartland.multijmx.validator.argumentvaluetype.ArgumentValueType;

import java.util.Optional;

import static uk.co.harrymartland.multijmx.MethodStrUtils.*;

public class MultiJMXOptionValidatorImpl implements MultiJMXOptionValidator {

    @Override
    public CommandLine validate(CommandLine commandLine) throws ValidationException {
        validateOrder(commandLine);
        validateObjectsAndAttributesCount(commandLine);
        validateMethodsAndAttributes(commandLine);
        return commandLine;
    }

    private void validateMethodsAndAttributes(CommandLine commandLine) throws ValidationException {
        for (String attribute : commandLine.getOptionValues("a")) {
            validateMethodAttribute(attribute);
        }
    }

    private void validateMethodAttribute(final String attribute) throws ValidationException {

        final int firstBracket = attribute.indexOf("(");

        if (firstBracket != -1) {
            final String methodName = attribute.substring(0, firstBracket);
            final String parameters = attribute.substring(firstBracket + 1, attribute.length() - 1);

            if (StringUtils.isNotBlank(parameters)) {
                for (final String argString : parameters.split(",")) {
                    validateArguments(methodName, argString);
                }
            }
        }
    }

    private void validateArguments(String methodName, String argString) throws ValidationException {
        int classEndBracket = argString.lastIndexOf(")");
        if (classEndBracket == -1) {
            throw new ValidationException("Error no class found for method " + methodName);
        }
        final String className = expandClassName(argString.substring(1, classEndBracket));
        final String value = argString.substring(classEndBracket + 1, argString.length());

        if (StringUtils.isBlank(value)) {
            throw new ValidationException("Error no value found for method " + methodName + " class " + className);
        }
        Optional<Class> argumentClass = isValidClass(className);
        if (!argumentClass.isPresent()) {
            throw new ValidationException("Error cannot find class " + className);
        } else {
            ArgumentValueType argumentValueType;
            try {
                argumentValueType = findArgumentValueType(argumentClass.get());
            } catch (ClassNotFoundException e) {
                throw new ValidationException(e.getMessage(), e);
            }
            if (!argumentValueType.isValid(value)) {
                throw new ValidationException("Error cannot convert " + value + " to " + className);
            }
        }
    }

    private void validateObjectsAndAttributesCount(CommandLine commandLine) throws ValidationException {
        if (commandLine.getOptionValues("o").length > 1) {
            if (commandLine.getOptionValues("o").length != commandLine.getOptionValues("a").length) {
                throw new ValidationException("Number of attributes and objects must match");
            }
        }
    }

    private void validateOrder(CommandLine commandLine) throws ValidationException {
        if (commandLine.hasOption("c") && commandLine.hasOption("v")) {
            throw new ValidationException("Cannot order by connection and display");
        }
    }

}
