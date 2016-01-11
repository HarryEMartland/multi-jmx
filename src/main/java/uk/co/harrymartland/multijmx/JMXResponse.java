package uk.co.harrymartland.multijmx;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;

public class JMXResponse {
    private String display;
    private String value;

    public JMXResponse(String display, String value) {
        this.display = display;
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public String getValue() {
        return value;
    }

    public static class ValueComparator implements Comparator<JMXResponse> {
        @Override
        public int compare(JMXResponse o1, JMXResponse o2) {
            return ObjectUtils.compare(o1.getValue(), o2.getValue());
        }
    }

    public static class DisplayComparator implements Comparator<JMXResponse> {
        @Override
        public int compare(JMXResponse o1, JMXResponse o2) {
            return ObjectUtils.compare(o1.getDisplay(), o2.getDisplay());
        }
    }
}
