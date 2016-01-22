package uk.co.harrymartland.multijmx.validator;

import org.apache.commons.cli.CommandLine;

public class MultiJMXOptionValidatorImpl implements MultiJMXOptionValidator {
    @Override
    public CommandLine validate(CommandLine commandLine) throws ValidationException {
        validateOrder(commandLine);
        validateObjectsAndAttributes(commandLine);//todo validate methods and implement methods
        return commandLine;
    }

    private void validateObjectsAndAttributes(CommandLine commandLine) throws ValidationException {
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
