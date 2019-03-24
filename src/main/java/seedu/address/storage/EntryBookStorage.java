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
    Path getListEntryBookFilePath();

    /**
     * Returns EntryBook data as a {@link ReadOnlyEntryBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEntryBook> readListEntryBook() throws DataConversionException, IOException;

    /**
     * @see #getListEntryBookFilePath()
     */
    Optional<ReadOnlyEntryBook> readListEntryBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     * @param listEntryBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveListEntryBook(ReadOnlyEntryBook listEntryBook) throws IOException;

    /**
     * @see #saveListEntryBook(ReadOnlyEntryBook)
     */
    void saveListEntryBook(ReadOnlyEntryBook listEntryBook, Path filePath) throws IOException;

}
