package uk.co.harrymartland.multijmx.domain.optionvalue;

import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.validator.ValidationException;

public interface OptionValue<T> {
    Option getOption();

    boolean validate() throws ValidationException;

    T getValue();

    int getNumberOfValues();

    String getArg();
}
