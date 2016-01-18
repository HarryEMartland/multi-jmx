package uk.co.harrymartland.multijmx.validator;

import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

public interface MultiJMXOptionValidator {

    MultiJMXOptions validate(MultiJMXOptions multiJMXOptions) throws ValidationException;
}
