package uk.co.harrymartland.multijmx.validator.argumentvaluetype;

import org.apache.commons.lang3.math.NumberUtils;

public class IntegerArgumentValueType implements ArgumentValueType {
    @Override
    public boolean isValid(String argument) {
        return NumberUtils.isParsable(argument);
    }

    @Override
    public Class forClass() {
        return Integer.class;
    }

    @Override
    public Comparable parse(String argument) {
        return Integer.parseInt(argument);
    }
}
