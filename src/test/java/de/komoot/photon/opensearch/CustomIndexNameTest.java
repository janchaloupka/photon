package de.komoot.photon.opensearch;

import de.komoot.photon.DatabaseProperties;
import de.komoot.photon.PhotonDoc;
import de.komoot.photon.TestServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The index name is configurable via the -index command line option. This test runs the
 * import path against a non-default index name and reads the document back, proving the
 * configured name is used for both writing and reading.
 */
class CustomIndexNameTest {
    private static final String CUSTOM_INDEX_NAME = "photon-custom-index";

    private TestServer testServer;

    @AfterEach
    void shutdown() {
        if (testServer != null) {
            testServer.stopTestServer();
        }
    }

    @Test
    void importAndQueryWithCustomIndexName(@TempDir Path dataDirectory) throws Exception {
        testServer = new TestServer(dataDirectory.toString(), "photon-test", CUSTOM_INDEX_NAME);
        final var dbProperties = new DatabaseProperties();
        testServer.reloadDBProperties(dbProperties);

        final var importer = testServer.createImporter(dbProperties);
        importer.add(List.of(new PhotonDoc("1234", "N", 1000, "place", "city")));
        importer.finish();

        assertThat(testServer.getByID("1234")).isNotNull();
    }
}
