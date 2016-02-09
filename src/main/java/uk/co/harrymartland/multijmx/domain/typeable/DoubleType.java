package uk.co.harrymartland.multijmx.domain.typeable;

public class DoubleType extends Typeable {

    public DoubleType(Double object) {
        super(object);
    }

    @Override
    public String getType() {
        return "double";
    }
}
