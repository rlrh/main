package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.EntryBook;
import seedu.address.testutil.TypicalEntries;

public class JsonSerializableEntryBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableEntryBookTest");
    private static final Path TYPICAL_ENTRIES_FILE = TEST_DATA_FOLDER.resolve("typicalEntryEntryBook.json");
    private static final Path INVALID_ENTRY_FILE = TEST_DATA_FOLDER.resolve("invalidEntryEntryBook.json");
    private static final Path DUPLICATE_ENTRY_FILE = TEST_DATA_FOLDER.resolve("duplicateEntryEntryBook.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalEntriesFile_success() throws Exception {
        JsonSerializableEntryBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_ENTRIES_FILE,
                JsonSerializableEntryBook.class).get();
        EntryBook entryBookFromFile = dataFromFile.toModelType();
        EntryBook typicalEntriesEntryBook = TypicalEntries.getTypicalListEntryBook();
        assertEquals(entryBookFromFile, typicalEntriesEntryBook);
    }

    @Test
    public void toModelType_invalidEntryFile_throwsIllegalValueException() throws Exception {
        JsonSerializableEntryBook dataFromFile = JsonUtil.readJsonFile(INVALID_ENTRY_FILE,
                JsonSerializableEntryBook.class).get();
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateEntries_throwsIllegalValueException() throws Exception {
        JsonSerializableEntryBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_ENTRY_FILE,
                JsonSerializableEntryBook.class).get();
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(JsonSerializableEntryBook.MESSAGE_DUPLICATE_ENTRY);
        dataFromFile.toModelType();
    }

}
