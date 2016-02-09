package uk.co.harrymartland.multijmx.domain.typeable;

public class FloatType extends Typeable {

    public FloatType(Double object) {
        super(object);
    }

    @Override
    public String getType() {
        return "float";
    }
}
