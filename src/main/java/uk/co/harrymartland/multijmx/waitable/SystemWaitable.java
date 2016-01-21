package uk.co.harrymartland.multijmx.waitable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemWaitable implements Waitable {

    BufferedReader reader = null;
    InputStreamReader inputStream = null;

    public SystemWaitable() {
        inputStream = new InputStreamReader(System.in);
        reader = new BufferedReader(inputStream);
    }

    @Override
    public void await() {
        try {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
        inputStream.close();
    }
}
