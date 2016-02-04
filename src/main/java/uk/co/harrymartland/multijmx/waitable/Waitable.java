package uk.co.harrymartland.multijmx.waitable;

import java.io.Closeable;

public interface Waitable {
    void await();
}
