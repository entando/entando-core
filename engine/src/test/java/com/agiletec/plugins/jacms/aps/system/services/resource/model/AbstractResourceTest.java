package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AbstractResourceTest {

    @Spy private TestResource resource;

    @Before
    public void setUp() {
        resource.resetFileExists();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullFilenameShouldReturnException() {
        resource.getUniqueBaseName(null);
    }

    @Test
    public void regularFileNameShouldReturnFileNameBase() {
        String originalName = "myfile.jpg";
        String baseName = "myfile";

        String uniqueBaseName = resource.getUniqueBaseName(originalName);

        assertEquals(baseName, uniqueBaseName);
    }

    @Test
    public void repeatedFileNameShouldReturnFileNameBase() {
        String originalName = "myfile.jpg";
        String baseName = "myfile";

        // First call
        String uniqueBaseName = resource.getUniqueBaseName(originalName);
        assertEquals(baseName, uniqueBaseName);

        // How many times we want the mock to say that file exists
        resource.setFileExistsCountdown(1);

        // Second call
        String secondBaseName = resource.getUniqueBaseName(originalName);
        assertEquals(baseName + "_1", secondBaseName);

        // With even more repeated files
        resource.setFileExistsCountdown(10);

        //
        String tenthName = resource.getUniqueBaseName(originalName);
        assertEquals(baseName + "_10", tenthName);
    }

    static abstract class TestResource extends AbstractResource {

        // How many times should reply that the file exists
        private int fileExistsCountdown = 0;

        @Override
        protected boolean exists(String instanceFileName) {
            boolean fileExists = fileExistsCountdown > 0;

            fileExistsCountdown --;

            return fileExists;
        }

        void setFileExistsCountdown(int fileExistsCountdown) {
            this.fileExistsCountdown = fileExistsCountdown;
        }

        void resetFileExists() {
            fileExistsCountdown = 0;
        }
    }
}