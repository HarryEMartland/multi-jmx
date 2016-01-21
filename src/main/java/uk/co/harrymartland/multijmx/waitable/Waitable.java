package uk.co.harrymartland.multijmx.waitable;

import java.io.Closeable;

public interface Waitable extends Closeable {
    void await();
}
