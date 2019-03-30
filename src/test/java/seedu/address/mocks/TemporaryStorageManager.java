package seedu.address.mocks;

import java.io.IOException;

import org.junit.rules.TemporaryFolder;

import seedu.address.storage.DataDirectoryArticleStorage;
import seedu.address.storage.JsonEntryBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/**
 * A mock for Storage for ease of creating objects in tests
 */
public class TemporaryStorageManager extends StorageManager {

    public TemporaryStorageManager(TemporaryFolder temporaryFolder) throws IOException {
        super(
                new JsonEntryBookStorage(temporaryFolder.newFile().toPath()),
                new JsonEntryBookStorage(temporaryFolder.newFile().toPath()),
                new JsonUserPrefsStorage(temporaryFolder.newFile().toPath()),
                new DataDirectoryArticleStorage(temporaryFolder.newFolder().toPath())
        );
    }

}
