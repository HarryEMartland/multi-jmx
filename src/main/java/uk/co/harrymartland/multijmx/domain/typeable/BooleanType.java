package uk.co.harrymartland.multijmx.domain.typeable;

public class BooleanType extends Typeable {

    public BooleanType(Boolean object) {
        super(object);
    }

    @Override
    public String getType() {
        return "boolean";
    }
}
