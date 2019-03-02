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
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) { }

    @Override
    public Path getAddressBookFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readAddressBook() {
        return Optional.empty();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readAddressBook(Path filePath) {
        return Optional.empty();
    }

    @Override
    public void saveAddressBook(ReadOnlyEntryBook addressBook) { }

    @Override
    public void saveAddressBook(ReadOnlyEntryBook addressBook, Path filePath) { }

}
