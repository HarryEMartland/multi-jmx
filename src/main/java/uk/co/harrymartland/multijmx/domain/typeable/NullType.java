package uk.co.harrymartland.multijmx.domain.typeable;

public class NullType extends Typeable {

    private String type;

    public NullType(Class clazz) {
        super(null);
        type = clazz.getCanonicalName();
    }

    @Override
    public String getType() {
        return type;
    }
}
