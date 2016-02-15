package uk.co.harrymartland.multijmx.service.validator;

import com.google.inject.Inject;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;
import uk.co.harrymartland.multijmx.validator.ValidationException;

import java.util.Set;

public class ValidatorServiceImpl implements ValidatorService {

    private Set<OptionValue> optionValues;

    @Inject
    public ValidatorServiceImpl(Set<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }

    @Override
    public void validate() throws ValidationException {
        for (OptionValue optionValue : optionValues) {
            optionValue.validate();
        }
    }
}
