package seedu.address.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.rules.TemporaryFolder;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.storage.JsonEntryBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the middle index of the entry in the {@code model}'s entry list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getFilteredPersonList().size() / 2);
    }

    /**
     * Returns the last index of the entry in the {@code model}'s entry list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getFilteredPersonList().size());
    }

    /**
     * Returns the entry in the {@code model}'s entry list at {@code index}.
     */
    public static Entry getPerson(Model model, Index index) {
        return model.getFilteredPersonList().get(index.getZeroBased());
    }

    /**
     * Creates a temporary Storage
     */
    public static Storage makeTempStorage() throws IOException {
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        JsonEntryBookStorage addressBookStorage = new JsonEntryBookStorage(temporaryFolder.newFile().toPath());
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.newFile().toPath());
        return new StorageManager(addressBookStorage, userPrefsStorage);
    }
}
