package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends EntryBookStorage, UserPrefsStorage, ArticleStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getListEntryBookFilePath();

    @Override
    Optional<ReadOnlyEntryBook> readListEntryBook() throws DataConversionException, IOException;

    @Override
    void saveListEntryBook(ReadOnlyEntryBook listEntryBook) throws IOException;

    @Override
    Optional<Path> addArticle(String url, byte[] articleContent) throws IOException;

}
