package uk.co.harrymartland.multijmx;

import java.io.Closeable;
import java.io.IOException;

public class ExceptionUtils {

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
}
