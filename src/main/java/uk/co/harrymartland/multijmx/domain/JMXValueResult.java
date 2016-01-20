package uk.co.harrymartland.multijmx.domain;

public class JMXValueResult implements Errorable<Comparable> {

    private Exception exception;
    private Comparable value;

    public JMXValueResult(Exception exception) {
        this.exception = exception;
    }

    public JMXValueResult(Comparable value) {
        this.value = value;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public Comparable getValue() {
        return value;
    }

    @Override
    public boolean isError() {
        return exception != null;
    }
}
