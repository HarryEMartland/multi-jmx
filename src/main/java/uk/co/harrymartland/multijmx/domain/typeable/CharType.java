package uk.co.harrymartland.multijmx.domain.typeable;

public class CharType extends Typeable {

    public CharType(Character object) {
        super(object);
    }

    @Override
    public String getType() {
        return "char";
    }
}
