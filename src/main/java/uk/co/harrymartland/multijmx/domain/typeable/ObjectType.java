package uk.co.harrymartland.multijmx.domain.typeable;

public class ObjectType extends Typeable {

    public ObjectType(Object object) {
        super(object);
    }

    @Override
    public String getType() {
        return getObject().getClass().getCanonicalName();
    }
}
