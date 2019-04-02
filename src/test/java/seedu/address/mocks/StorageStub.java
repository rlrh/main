package seedu.address.mocks;

import java.nio.file.Path;
import java.util.Optional;

import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

/**
 * A mock for Storage for ease of creating objects in tests
 */
public class StorageStub implements Storage {

    @Override
    public Path getUserPrefsFilePath() {
        return null;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() {
        return Optional.empty();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
        // Do nothing
    }

    @Override
    public Path getListEntryBookFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readListEntryBook() {
        return Optional.empty();
    }

    @Override
    public void saveListEntryBook(ReadOnlyEntryBook listEntryBook) {
        // Do nothing
    }

    @Override
    public Path getArchivesEntryBookFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readArchivesEntryBook() {
        return Optional.empty();
    }

    @Override
    public void saveArchivesEntryBook(ReadOnlyEntryBook listEntryBook) {
        // Do nothing
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        return null;
    }

    @Override
    public void deleteArticle(String url) {
        // Do nothing
    }

    @Override
    public Optional<Path> addArticle(String url, byte[] content) {
        return Optional.empty();
    }

    @Override
    public Path getArticlePath(String url) {
        return null;
    }

    @Override
    public Optional<Path> getOfflineLink(String url) {
        return Optional.empty();
    }
}
