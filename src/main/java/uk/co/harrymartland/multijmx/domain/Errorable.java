package uk.co.harrymartland.multijmx.domain;

public interface Errorable<T> {
    boolean isError();

    T getValue();
}
