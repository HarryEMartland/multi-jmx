package uk.co.harrymartland.multijmx.domain.typeable;

public abstract class Typeable {

    private Object object;

    public Typeable(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public abstract String getType();
}
