package uk.co.harrymartland.multijmx.Writer;

public class SystemOutWriter implements Writer {
    @Override
    public void writeLine(String output) {
        System.out.println(output);
    }
}
