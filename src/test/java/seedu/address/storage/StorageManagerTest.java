package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static seedu.address.testutil.TypicalEntries.getTypicalAddressBook;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        JsonEntryBookStorage addressBookStorage = new JsonEntryBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        ArticleStorage articleStorage = new DataDirectoryArticleStorage(getTempFilePath("articles"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage, articleStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonEntryBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonEntryBookStorageTest} class.
         */
        EntryBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyEntryBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new EntryBook(retrieved));
    }

    @Test
    public void articleStorageReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link DataDirectoryArticleStorage} class.
         * More extensive testing of article saving/reading is done in {@link DataDirectoryArticleStorageTest} class.
         */
        String testUrl = "https://test.url";
        byte[] testContent = "test content".getBytes();
        storageManager.addArticle(testUrl, testContent);
        Path savedArticlePath = storageManager.getArticlePath(testUrl);
        assertArrayEquals(Files.readAllBytes(savedArticlePath), testContent);
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void getArticleDataDirectoryPath() {
        assertNotNull(storageManager.getArticleDataDirectoryPath());
    }

}
