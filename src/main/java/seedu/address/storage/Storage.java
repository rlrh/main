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
public interface Storage extends UserPrefsStorage, ArticleStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Optional<Path> addArticle(String url, byte[] articleContent) throws IOException;

    Path getListEntryBookFilePath();

    Optional<ReadOnlyEntryBook> readListEntryBook() throws DataConversionException, IOException;

    void saveListEntryBook(ReadOnlyEntryBook listEntryBook) throws IOException;

    Path getArchivesEntryBookFilePath();

    Optional<ReadOnlyEntryBook> readArchivesEntryBook() throws DataConversionException, IOException;

    void saveArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook) throws IOException;

    Path getFeedsEntryBookFilePath();

    Optional<ReadOnlyEntryBook> readFeedsEntryBook() throws DataConversionException, IOException;

    void saveFeedsEntryBook(ReadOnlyEntryBook feedsEntryBook) throws IOException;
}
