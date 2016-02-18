package uk.co.harrymartland.multijmx.domain.optionvalue;

import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.ValidationException;

public interface OptionValue<T> {
    Option getOption();

    void validate() throws ValidationException;

    T getValue();

    int getNumberOfValues();

    String getArg();
}
