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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Typeable typeable = (Typeable) o;

        return object != null ? object.equals(typeable.object) : typeable.object == null;

    }

    @Override
    public int hashCode() {
        return object != null ? object.hashCode() : 0;
    }
}
