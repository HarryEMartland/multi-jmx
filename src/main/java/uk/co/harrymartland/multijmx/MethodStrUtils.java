package uk.co.harrymartland.multijmx;

import org.apache.commons.lang3.StringUtils;

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

}
