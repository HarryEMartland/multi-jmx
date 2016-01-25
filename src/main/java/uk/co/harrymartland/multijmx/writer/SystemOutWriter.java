package uk.co.harrymartland.multijmx.writer;

public class SystemOutWriter implements Writer {
    @Override
    public void writeLine(String output) {
        System.out.println(output);
    }
}
