package uk.co.harrymartland.multijmx.domain;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class JMXConnectionResponse implements Errorable<List<JMXValueResult>> {

    private String display;
    private List<JMXValueResult> values;
    private Exception exception;
    private boolean isError = false;

    public JMXConnectionResponse(String display, List<JMXValueResult> values) {
        this.display = display;
        this.values = values;
    }

    public JMXConnectionResponse(String display, Exception exception) {
        this(display, Collections.singletonList(new JMXValueResult(exception.getMessage())));
        this.exception = exception;
        this.isError = true;
    }

    public String getDisplay() {
        return display;
    }

    @Override
    public List<JMXValueResult> getValue() {
        return values;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public boolean isError() {
        return isError;
    }

    @Override
    public String toString() {
        return "JMXConnectionResponse{" +
                "display='" + display + '\'' +
                ", values=" + values +
                ", exception=" + exception +
                ", isError=" + isError +
                '}';
    }

    private static <T extends Errorable> int compareErrorable(T o1, T o2, BiFunction<T, T, Integer> compare) {
        if (!o1.isError() && !o2.isError()) {
            return compare.apply(o1, o2);
        } else if (o1.isError() && o2.isError()) {
            return 0;
        } else if (o1.isError()) {
            return -1;
        } else {
            return 1;
        }
    }

    public static abstract class AbstractComparator implements Comparator<JMXConnectionResponse> {
        private boolean reversed = false;

        @Override
        public final int compare(JMXConnectionResponse o1, JMXConnectionResponse o2) {
            return compareErrorable(o1, o2, (errorable, errorable2) -> {
                if (reversed) {
                    return doCompare(errorable2, errorable);
                } else {
                    return doCompare(errorable, errorable2);
                }
            });
        }

        @Override
        public Comparator<JMXConnectionResponse> reversed() {
            this.reversed = !this.reversed;
            return this;
        }

        protected abstract int doCompare(JMXConnectionResponse o1, JMXConnectionResponse o2);
    }

    public static class ValueComparator extends AbstractComparator {
        @Override
        public int doCompare(JMXConnectionResponse o1, JMXConnectionResponse o2) {
            Iterator<JMXValueResult> iterator1 = o1.getValue().iterator();
            Iterator<JMXValueResult> iterator2 = o2.getValue().iterator();

            while (iterator1.hasNext() && iterator2.hasNext()) {
                int comparison = compareErrorable(iterator1.next(), iterator2.next(),
                        (comparableErrorable, comparableErrorable2) ->
                                ObjectUtils.compare(comparableErrorable.getValue(), comparableErrorable2.getValue()));

                if (comparison != 0) {
                    return comparison;
                }
            }

            return 0;
        }
    }

    public static class DisplayComparator extends AbstractComparator {
        @Override
        public int doCompare(JMXConnectionResponse o1, JMXConnectionResponse o2) {
            return ObjectUtils.compare(o1.getDisplay(), o2.getDisplay());
        }
    }
}
