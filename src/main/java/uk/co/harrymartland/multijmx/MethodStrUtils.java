package uk.co.harrymartland.multijmx;

import org.apache.commons.lang3.StringUtils;
import uk.co.harrymartland.multijmx.validator.argumentvaluetype.ArgumentValueType;
import uk.co.harrymartland.multijmx.validator.argumentvaluetype.IntegerArgumentValueType;

import java.util.Optional;

public class MethodStrUtils {

    public static String expandClassName(final String className) {
        if (StringUtils.contains(className, ".")) {
            return className;
        }
        return "java.lang." + className;
    }

    public static Optional<Class> isValidClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static ArgumentValueType findArgumentValueType(Class clazz) throws ClassNotFoundException {
        return IntegerArgumentValueType.ARGUMENT_VALUE_TYPES.stream()
                .filter(argumentValueType -> argumentValueType.forClass().equals(clazz))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("No Argument value type found for type " + clazz.getCanonicalName()));
    }
}
