package uk.co.harrymartland.multijmx.domain.typeable;

public class LongType extends Typeable {
    public LongType(Long object) {
        super(object);
    }

    @Override
    public String getType() {
        return "long";
    }
}
