package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.HOON;
import static seedu.address.testutil.TypicalEntries.IDA;
import static seedu.address.testutil.TypicalEntries.getTypicalListEntryBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;

public class JsonEntryBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonEntryBookStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readEntryBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readEntryBook(null);
    }

    private java.util.Optional<ReadOnlyEntryBook> readEntryBook(String filePath) throws Exception {
        return new JsonEntryBookStorage(Paths.get(filePath)).readEntryBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readEntryBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readEntryBook("notJsonFormatEntryBook.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readEntryBook_invalidEntryEntryBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readEntryBook("invalidEntryEntryBook.json");
    }

    @Test
    public void readEntryBook_invalidAndValidEntryEntryBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readEntryBook("invalidAndValidEntryEntryBook.json");
    }

    @Test
    public void readAndSaveEntryBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempAddressBook.json");
        EntryBook original = getTypicalListEntryBook();
        JsonEntryBookStorage jsonEntryBookStorage = new JsonEntryBookStorage(filePath);

        // Save in new file and read back
        jsonEntryBookStorage.saveEntryBook(original, filePath);
        ReadOnlyEntryBook readBack = jsonEntryBookStorage.readEntryBook(filePath).get();
        assertEquals(original, new EntryBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addEntry(HOON);
        original.removeEntry(ALICE);
        jsonEntryBookStorage.saveEntryBook(original, filePath);
        readBack = jsonEntryBookStorage.readEntryBook(filePath).get();
        assertEquals(original, new EntryBook(readBack));

        // Save and read without specifying file path
        original.addEntry(IDA);
        jsonEntryBookStorage.saveEntryBook(original); // file path not specified
        readBack = jsonEntryBookStorage.readEntryBook().get(); // file path not specified
        assertEquals(original, new EntryBook(readBack));

    }

    @Test
    public void saveEntryBook_nullEntryBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveEntryBook(null, "SomeFile.json");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) {
        try {
            new JsonEntryBookStorage(Paths.get(filePath))
                    .saveEntryBook(entryBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveEntryBook_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveEntryBook(new EntryBook(), null);
    }
}
