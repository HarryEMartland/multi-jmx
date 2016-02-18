package uk.co.harrymartland.multijmx;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.function.Consumer;

public class ExceptionUtils {


    public static <T> Consumer<T> eat(Consumer<T> function) {
        return t -> {
            try {
                function.accept(t);
            } catch (Exception e) {
                eat(e);
            }
        };
    }

    public static void closeQuietly(Closeable... closeables) {
        Arrays.asList(closeables).forEach(ExceptionUtils::closeQuietly);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                eat(e);
            }
        }
    }

    public static void eat(@SuppressWarnings("UnusedParameters") Exception e) {
        //om nom nom
    }

    public static String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}
