package uk.co.harrymartland.multijmx.domain.typeable;

public class IntType extends Typeable {

    public IntType(Integer object) {
        super(object);
    }

    @Override
    public String getType() {
        return "int";
    }
}
