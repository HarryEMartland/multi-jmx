package uk.co.harrymartland.multijmx.domain.OptionValue;

import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.validator.ValidationException;

public interface OptionValue<T> {
    Option getOption();

    void validate() throws ValidationException;

    T getValue();
}
