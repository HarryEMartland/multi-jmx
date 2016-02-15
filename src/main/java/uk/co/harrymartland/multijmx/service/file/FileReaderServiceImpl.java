package uk.co.harrymartland.multijmx.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderServiceImpl implements FileReaderService {
    @Override
    public List<String> readFromFile(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    //todo test
    @Override
    public boolean exists(Path file) {
        return Files.exists(file);
    }
}
