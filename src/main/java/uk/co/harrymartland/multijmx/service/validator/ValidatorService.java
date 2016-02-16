package uk.co.harrymartland.multijmx.service.validator;

import uk.co.harrymartland.multijmx.domain.ValidationException;

public interface ValidatorService {
    void validate() throws ValidationException;
}
