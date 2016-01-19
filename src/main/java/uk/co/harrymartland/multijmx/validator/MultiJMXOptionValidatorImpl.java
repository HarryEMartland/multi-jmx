package uk.co.harrymartland.multijmx.validator;

import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

public class MultiJMXOptionValidatorImpl implements MultiJMXOptionValidator {
    @Override
    public MultiJMXOptions validate(MultiJMXOptions multiJMXOptions) throws ValidationException {
        validateOrder(multiJMXOptions);
        validateObjectsAndAttributes(multiJMXOptions);
        return multiJMXOptions;
    }

    private void validateObjectsAndAttributes(MultiJMXOptions multiJMXOptions) throws ValidationException {
        if (multiJMXOptions.getObjectNames().size() > 1) {
            if (multiJMXOptions.getObjectNames().size() != multiJMXOptions.getAttributes().size()) {
                throw new ValidationException("Number of attributes and objects must match");
            }
        }
    }

    private void validateOrder(MultiJMXOptions multiJMXOptions) throws ValidationException {
        if (multiJMXOptions.isOrderConnection() && multiJMXOptions.isOrderValue()) {
            throw new ValidationException("Cannot order by connection and display");
        }
    }
}
