package uk.co.harrymartland.multijmx.validator.argumentvaluetype;

public class StringArgumentValueType implements ArgumentValueType<String> {
    @Override
    public boolean isValid(String argument) {
        return true;
    }

    @Override
    public Class<String> forClass() {
        return String.class;
    }

    @Override
    public String parse(String argument) {
        return argument;
    }
}
