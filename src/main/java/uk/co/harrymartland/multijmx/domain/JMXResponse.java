package uk.co.harrymartland.multijmx.domain;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;

public class JMXResponse {

    private String display;
    private Comparable value;
    private Exception exception;
    private boolean isError = false;

    public JMXResponse(String display, Comparable value) {
        this.display = display;
        this.value = value;
    }

    public JMXResponse(String display, Exception exception) {
        this(display, exception.getMessage());
        this.exception = exception;
        this.isError = true;
    }

    public String getDisplay() {
        return display;
    }

    public Comparable getValue() {
        return value;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isError() {
        return isError;
    }

    @Override
    public String toString() {
        return "JMXResponse{" +
                "display='" + display + '\'' +
                ", value=" + value +
                ", exception=" + exception +
                ", isError=" + isError +
                '}';
    }

    private static abstract class AbstractComparator implements Comparator<JMXResponse> {
        private boolean reversed = false;

        @Override
        public final int compare(JMXResponse o1, JMXResponse o2) {
            if (!o1.isError && !o2.isError) {
                if (reversed) {
                    return doCompare(o2, o1);
                } else {
                    return doCompare(o1, o2);
                }
            } else if (o1.isError && o2.isError) {
                return 0;
            } else if (o1.isError) {
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        public Comparator<JMXResponse> reversed() {
            return this;
        }

        protected abstract int doCompare(JMXResponse o1, JMXResponse o2);
    }

    public static class ValueComparator extends AbstractComparator {
        @Override
        public int doCompare(JMXResponse o1, JMXResponse o2) {
            return ObjectUtils.compare(o1.getValue(), o2.getValue());
        }
    }

    public static class DisplayComparator extends AbstractComparator {
        @Override
        public int doCompare(JMXResponse o1, JMXResponse o2) {
            return ObjectUtils.compare(o1.getDisplay(), o2.getDisplay());
        }
    }
}
