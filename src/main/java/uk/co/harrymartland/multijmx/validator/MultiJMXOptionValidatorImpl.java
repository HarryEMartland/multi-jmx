package uk.co.harrymartland.multijmx.validator;

import uk.co.harrymartland.multijmx.domain.MultiJMXOptions;

public class MultiJMXOptionValidatorImpl implements MultiJMXOptionValidator {
    @Override
    public MultiJMXOptions validate(MultiJMXOptions multiJMXOptions) throws ValidationException {
        validateOrder(multiJMXOptions);
        return multiJMXOptions;
    }

    private void validateOrder(MultiJMXOptions multiJMXOptions) throws ValidationException {
        if (multiJMXOptions.isOrderDisplay() && multiJMXOptions.isOrderValue()) {
            throw new ValidationException("Cannot order by value and display");
        }
    }
}
