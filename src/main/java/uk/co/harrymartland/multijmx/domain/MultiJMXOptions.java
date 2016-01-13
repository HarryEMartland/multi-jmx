package uk.co.harrymartland.multijmx.domain;

import org.apache.commons.lang3.StringUtils;

import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.util.List;

public class MultiJMXOptions {

    private boolean orderValue;
    private boolean orderDisplay;
    private Integer maxThreads;
    private ObjectName objectName;
    private String attribute;
    private String username;
    private String password;
    private List<JMXServiceURL> urls;

    public boolean isOrderValue() {
        return orderValue;
    }

    public void setOrderValue(boolean orderValue) {
        this.orderValue = orderValue;
    }

    public boolean isOrderDisplay() {
        return orderDisplay;
    }

    public void setOrderDisplay(boolean orderDisplay) {
        this.orderDisplay = orderDisplay;
    }

    public boolean isOrdered() {
        return isOrderDisplay() || isOrderValue();
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

    public ObjectName getObjectName() {
        return objectName;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
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

    public List<JMXServiceURL> getUrls() {
        return urls;
    }

    public void setUrls(List<JMXServiceURL> urls) {
        this.urls = urls;
    }
}
