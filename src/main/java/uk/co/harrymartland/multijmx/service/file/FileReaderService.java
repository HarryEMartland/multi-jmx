package uk.co.harrymartland.multijmx.service.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileReaderService {

    List<String> readFromFile(Path file) throws IOException;

    boolean exists(Path file);

}
