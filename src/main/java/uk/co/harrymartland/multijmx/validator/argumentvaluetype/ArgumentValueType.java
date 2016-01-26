package uk.co.harrymartland.multijmx.validator.argumentvaluetype;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ArgumentValueType<T> {

    List<ArgumentValueType> ARGUMENT_VALUE_TYPES = Collections.unmodifiableList(Arrays.asList(
            new IntegerArgumentValueType(),
            new StringArgumentValueType()
    ));

    boolean isValid(String argument);

    Class<T> forClass();

    T parse(String argument);
}
