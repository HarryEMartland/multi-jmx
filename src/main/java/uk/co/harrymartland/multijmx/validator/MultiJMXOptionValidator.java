package uk.co.harrymartland.multijmx.validator;

import org.apache.commons.cli.CommandLine;

public interface MultiJMXOptionValidator {

    CommandLine validate(CommandLine commandLine) throws ValidationException;
}
