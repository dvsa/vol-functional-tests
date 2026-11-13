package org.dvsa.testing.framework.Utils.Generic;

import activesupport.driver.Browser;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UniversalActionsTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @After
    public void clearDriver() {
        Browser.removeLocalDriverThread();
    }

    @Test
    public void waitsForCsvAndDownloadsItIntoTheRequestedDirectory() throws IOException {
        DownloadDriver driver = new DownloadDriver(null);
        TestBrowser.use(driver);
        Path directory = temporaryFolder.getRoot().toPath().resolve("download");

        Path csv = UniversalActions.downloadCsv(directory);

        assertTrue("Should retry while only incomplete or non-CSV files are available", driver.checks > 1);
        assertEquals(directory.resolve("vehicles.CSV"), csv);
        assertEquals("Vehicle registration mark,Make / model\nAB12CDE,Coach\n", Files.readString(csv));
    }

    @Test
    public void propagatesDownloadFailure() {
        IOException failure = new IOException("Download failed");
        TestBrowser.use(new DownloadDriver(failure));

        IOException thrown = assertThrows(IOException.class,
                () -> UniversalActions.downloadCsv(temporaryFolder.getRoot().toPath()));

        assertSame(failure, thrown);
    }

    private static class TestBrowser extends Browser {
        static void use(WebDriver driver) {
            threadLocalDriver.set(driver);
        }
    }

    private static class DownloadDriver extends RemoteWebDriver {
        private final IOException failure;
        private int checks;

        private DownloadDriver(IOException failure) {
            this.failure = failure;
        }

        @Override
        public List<String> getDownloadableFiles() {
            return ++checks == 1
                    ? List.of("notes.txt", "vehicles.csv.crdownload")
                    : List.of("notes.txt", "vehicles.CSV");
        }

        @Override
        public void downloadFile(String fileName, Path targetLocation) throws IOException {
            if (failure != null) {
                throw failure;
            }
            Files.writeString(targetLocation.resolve(fileName),
                    "Vehicle registration mark,Make / model\nAB12CDE,Coach\n");
        }
    }
}
