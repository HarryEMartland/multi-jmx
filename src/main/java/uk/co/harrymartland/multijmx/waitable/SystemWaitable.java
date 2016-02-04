package uk.co.harrymartland.multijmx.waitable;

import uk.co.harrymartland.multijmx.ExceptionUtils;
import uk.co.harrymartland.multijmx.domain.lifecycle.LifeCycleAble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemWaitable implements Waitable, LifeCycleAble {

    BufferedReader reader = null;
    InputStreamReader inputStream = null;

    @Override
    public void await() {
        try {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void birth() {
        inputStream = new InputStreamReader(System.in);
        reader = new BufferedReader(inputStream);
    }

    @Override
    public void die() {
        ExceptionUtils.closeQuietly(reader);
        ExceptionUtils.closeQuietly(inputStream);
    }
}
