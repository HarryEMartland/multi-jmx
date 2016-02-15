package uk.co.harrymartland.multijmx.service.file;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;

public class FileReaderServiceImplTest {

    private FileReaderService fileReaderService = new FileReaderServiceImpl();

    @Test
    public void testShouldReturnFalseWhenFileDoesNotExist() throws Exception {
        Assert.assertFalse(fileReaderService.exists(Paths.get("fake.file")));
    }

    @Test
    public void testShouldReturnTrueWhenFileExists() throws Exception {
        //noinspection ConstantConditions
        Assert.assertTrue(fileReaderService.exists(Paths.get(getClass().getClassLoader().getResource("testRMIUrls.csv").getPath())));
    }

    @Test
    public void testShouldReturnFileContents() throws Exception {
        //noinspection ConstantConditions
        Assert.assertEquals(Arrays.asList(
                "service:jmx:rmi://test1/jndi/rmi://:9999/jmxrmi",
                "service:jmx:rmi://test2/jndi/rmi://:9999/jmxrmi",
                "service:jmx:rmi://test3/jndi/rmi://:9999/jmxrmi"
        ), fileReaderService.readFromFile(Paths.get(getClass().getClassLoader().getResource("testRMIUrls.csv").getPath())));
    }
}