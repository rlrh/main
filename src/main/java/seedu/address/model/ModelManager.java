package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.network.Network;
import seedu.address.storage.Storage;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedEntryBook versionedEntryBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Entry> filteredEntries;

    private final SimpleObjectProperty<Entry> selectedEntry = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Exception> exception = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<CommandResult> commandResult = new SimpleObjectProperty<>();
    private final Storage storage;

    /**
     * Initializes a ModelManager with the given addressBook, userPrefs, and storage
     */
    public ModelManager(ReadOnlyEntryBook addressBook, ReadOnlyUserPrefs userPrefs, Storage storage) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedEntryBook = new VersionedEntryBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredEntries = new FilteredList<>(versionedEntryBook.getPersonList());
        filteredEntries.addListener(this::ensureSelectedPersonIsValid);

        this.storage = storage;

        // Save the entry book to storage whenever it is modified.
        versionedEntryBook.addListener(this::saveToStorageListener);
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getEntryBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setEntryBookFilePath(Path entryBookFilePath) {
        requireNonNull(entryBookFilePath);
        userPrefs.setAddressBookFilePath(entryBookFilePath);
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        return userPrefs.getArticleDataDirectoryPath();
    }

    @Override
    public void setArticleDataDirectoryPath(Path articleDataDirectoryPath) {
        requireNonNull(articleDataDirectoryPath);
        userPrefs.setArticleDataDirectoryPath(articleDataDirectoryPath);
    }


    //=========== EntryBook ================================================================================

    @Override
    public void setEntryBook(ReadOnlyEntryBook entryBook) {
        versionedEntryBook.resetData(entryBook);
    }

    @Override
    public ReadOnlyEntryBook getEntryBook() {
        return versionedEntryBook;
    }

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return versionedEntryBook.hasPerson(entry);
    }

    @Override
    public void deleteEntry(Entry target) {
        versionedEntryBook.removePerson(target);
    }

    @Override
    public void addEntry(Entry entry) {
        versionedEntryBook.addPerson(entry);
        try {
            byte[] articleContent = Network.fetchAsBytes(entry.getLink().value);
            storage.addArticle(entry.getLink().value, articleContent);
        } catch (IOException ioe) {
            // Do nothing if we fail to fetch the page.
        }
        updateFilteredEntryList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setEntry(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);

        versionedEntryBook.setPerson(target, editedEntry);
    }

    @Override
    public void clearEntryBook() {
        versionedEntryBook.clear();
    }

    //=========== Storage ===================================================================================

    @Override
    public Storage getStorage() {
        return storage;
    }

    //=========== Filtered Entry List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the internal list of
     * {@code versionedEntryBook}
     */
    @Override
    public ObservableList<Entry> getFilteredEntryList() {
        return filteredEntries;
    }

    @Override
    public void updateFilteredEntryList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoEntryBook() {
        return versionedEntryBook.canUndo();
    }

    @Override
    public boolean canRedoEntryBook() {
        return versionedEntryBook.canRedo();
    }

    @Override
    public void undoEntryBook() {
        versionedEntryBook.undo();
    }

    @Override
    public void redoEntryBook() {
        versionedEntryBook.redo();
    }

    @Override
    public void commitEntryBook() {
        versionedEntryBook.commit();
    }

    //=========== Selected entry ===========================================================================

    @Override
    public ReadOnlyProperty<Entry> selectedEntryProperty() {
        return selectedEntry;
    }

    @Override
    public Entry getSelectedEntry() {
        return selectedEntry.getValue();
    }

    @Override
    public void setSelectedEntry(Entry entry) {
        if (entry != null && !filteredEntries.contains(entry)) {
            throw new EntryNotFoundException();
        }
        selectedEntry.setValue(entry);
    }

    //=========== Exception propagation ===========================================================================

    @Override
    public ReadOnlyProperty<Exception> exceptionProperty() {
        return exception;
    }

    @Override
    public Exception getException() {
        return exception.getValue();
    }

    @Override
    public void setException(Exception exceptionToBePropagated) {
        exception.setValue(exceptionToBePropagated);
    }

    //=========== Command result ===========================================================================

    @Override
    public ReadOnlyProperty<CommandResult> commandResultProperty() {
        return commandResult;
    }

    @Override
    public CommandResult getCommandResult() {
        return commandResult.getValue();
    }

    @Override
    public void setCommandResult(CommandResult result) {
        commandResult.setValue(result);
    }

    /**
     * Ensures {@code selectedEntry} is a valid entry in {@code filteredEntries}.
     */
    private void ensureSelectedPersonIsValid(ListChangeListener.Change<? extends Entry> change) {
        while (change.next()) {
            if (selectedEntry.getValue() == null) {
                // null is always a valid selected entry, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedEntry.getValue());
            if (wasSelectedPersonReplaced) {
                // Update selectedEntry to its new value.
                int index = change.getRemoved().indexOf(selectedEntry.getValue());
                selectedEntry.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson -> selectedEntry.getValue().isSameEntry(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the entry that came before it in the list,
                // or clear the selection if there is no such entry.
                selectedEntry.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    /**
     * Ensures that storage is updated whenever entry book is modified.
     */
    private void saveToStorageListener(Observable observable) {
        logger.info("Address book modified, saving to file.");
        try {
            storage.saveAddressBook(versionedEntryBook);
        } catch (IOException ioe) {
            setException(new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe));
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedEntryBook.equals(other.versionedEntryBook)
                && userPrefs.equals(other.userPrefs)
                && filteredEntries.equals(other.filteredEntries)
                && Objects.equals(selectedEntry.get(), other.selectedEntry.get());
    }

    @Override
    public Model clone() {
        return new ModelManager(this.versionedEntryBook, this.userPrefs, this.storage);
    }

}
