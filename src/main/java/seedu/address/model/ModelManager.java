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
import seedu.address.ui.ViewMode;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedEntryBook versionedEntryBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Entry> filteredEntries;

    private final SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<ViewMode> currentViewMode = new SimpleObjectProperty<>();
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

        currentViewMode.set(ViewMode.BROWSER);

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
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
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
    public void setAddressBook(ReadOnlyEntryBook addressBook) {
        versionedEntryBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyEntryBook getAddressBook() {
        return versionedEntryBook;
    }

    @Override
    public boolean hasPerson(Entry entry) {
        requireNonNull(entry);
        return versionedEntryBook.hasPerson(entry);
    }

    @Override
    public void deletePerson(Entry target) {
        versionedEntryBook.removePerson(target);
    }

    @Override
    public void addPerson(Entry entry) {
        versionedEntryBook.addPerson(entry);
        try {
            byte[] articleContent = Network.fetchAsBytes(entry.getLink().value);
            storage.addArticle(entry.getLink().value, articleContent);
        } catch (IOException ioe) {
            // Do nothing if we fail to fetch the page.
        }
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Entry target, Entry editedEntry) {
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
    public ObservableList<Entry> getFilteredPersonList() {
        return filteredEntries;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedEntryBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedEntryBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedEntryBook.undo();
    }

    @Override
    public void redoAddressBook() {
        versionedEntryBook.redo();
    }

    @Override
    public void commitAddressBook() {
        versionedEntryBook.commit();
    }

    //=========== Selected entry ===========================================================================

    @Override
    public ReadOnlyProperty<Entry> selectedPersonProperty() {
        return selectedPerson;
    }

    @Override
    public Entry getSelectedPerson() {
        return selectedPerson.getValue();
    }

    @Override
    public void setSelectedPerson(Entry entry) {
        if (entry != null && !filteredEntries.contains(entry)) {
            throw new EntryNotFoundException();
        }
        selectedPerson.setValue(entry);
    }

    //=========== View mode ===========================================================================

    @Override
    public ReadOnlyProperty<ViewMode> viewModeProperty() {
        return currentViewMode;
    }

    @Override
    public ViewMode getViewMode() {
        return currentViewMode.getValue();
    }

    @Override
    public void setViewMode(ViewMode viewMode) {
        currentViewMode.setValue(viewMode);
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
     * Ensures {@code selectedPerson} is a valid entry in {@code filteredEntries}.
     */
    private void ensureSelectedPersonIsValid(ListChangeListener.Change<? extends Entry> change) {
        while (change.next()) {
            if (selectedPerson.getValue() == null) {
                // null is always a valid selected entry, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedPerson.getValue());
            if (wasSelectedPersonReplaced) {
                // Update selectedPerson to its new value.
                int index = change.getRemoved().indexOf(selectedPerson.getValue());
                selectedPerson.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson -> selectedPerson.getValue().isSamePerson(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the entry that came before it in the list,
                // or clear the selection if there is no such entry.
                selectedPerson.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
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
                && Objects.equals(selectedPerson.get(), other.selectedPerson.get());
    }

    @Override
    public Model clone() {
        return new ModelManager(this.versionedEntryBook, this.userPrefs, this.storage);
    }

}
