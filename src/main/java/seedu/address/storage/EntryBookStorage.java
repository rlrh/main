package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;

/**
 * Represents a storage for {@link EntryBook}.
 */
public interface EntryBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getEntryBookFilePath();

    /**
     * Returns EntryBook data as a {@link ReadOnlyEntryBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException;

    /**
     * @see #getEntryBookFilePath()
     */
    Optional<ReadOnlyEntryBook> readEntryBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     * @param entryBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveEntryBook(ReadOnlyEntryBook entryBook) throws IOException;

    /**
     * @see #saveEntryBook(ReadOnlyEntryBook)
     */
    void saveEntryBook(ReadOnlyEntryBook entryBook, Path filePath) throws IOException;

}
