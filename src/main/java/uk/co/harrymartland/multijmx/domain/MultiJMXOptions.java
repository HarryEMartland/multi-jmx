package uk.co.harrymartland.multijmx.domain;

import org.apache.commons.lang3.StringUtils;
import uk.co.harrymartland.multijmx.domain.connection.JMXConnection;

import javax.management.ObjectName;
import java.util.List;

public class MultiJMXOptions {

    private boolean orderValue;
    private boolean orderConnection;
    private boolean reverseOrder;
    private Integer maxThreads;
    private List<ObjectName> objectNames;
    private List<String> attributes;
    private String username;
    private String password;
    private List<JMXConnection> urls;
    private String delimiter;

    public boolean isOrderValue() {
        return orderValue;
    }

    public void setOrderValue(boolean orderValue) {
        this.orderValue = orderValue;
    }

    public boolean isOrderConnection() {
        return orderConnection;
    }

    public void setOrderConnection(boolean orderDisplay) {
        this.orderConnection = orderDisplay;
    }

    public boolean isReverseOrder() {
        return reverseOrder;
    }

    public void setReverseOrder(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    public boolean isOrdered() {
        return isOrderConnection() || isOrderValue();
    }

    public boolean isThreadsLimited() {
        return maxThreads != null;
    }

    public Integer getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }

    public List<ObjectName> getObjectNames() {
        return objectNames;
    }

    public void setObjectNames(List<ObjectName> objectNames) {
        this.objectNames = objectNames;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticationRequired() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }

    public List<JMXConnection> getUrls() {
        return urls;
    }

    public void setUrls(List<JMXConnection> urls) {
        this.urls = urls;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
