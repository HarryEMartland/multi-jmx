package uk.co.harrymartland.multijmx.service.validator;

import uk.co.harrymartland.multijmx.validator.ValidationException;

public interface ValidatorService {
    void validate() throws ValidationException;
}
