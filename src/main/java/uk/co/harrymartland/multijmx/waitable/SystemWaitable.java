package uk.co.harrymartland.multijmx.waitable;

import com.google.inject.Singleton;
import uk.co.harrymartland.multijmx.ExceptionUtils;
import uk.co.harrymartland.multijmx.domain.LifeCycleAble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Singleton
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
        inputStream = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        reader = new BufferedReader(inputStream);
    }

    @Override
    public void die() {
        ExceptionUtils.closeQuietly(reader);
        ExceptionUtils.closeQuietly(inputStream);
    }
}
