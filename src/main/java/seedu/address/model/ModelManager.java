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
import javafx.beans.property.SimpleListProperty;
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

    private ModelContext context = ModelContext.CONTEXT_LIST;

    private final EntryBook listEntryBook;
    private final UserPrefs userPrefs;
    private final SimpleListProperty<Entry> displayedEntryList = new SimpleListProperty<>();
    private final FilteredList<Entry> filteredEntries;

    private final SimpleObjectProperty<Entry> selectedEntry = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<ViewMode> currentViewMode = new SimpleObjectProperty<>(ViewMode.BROWSER);
    private final SimpleObjectProperty<Exception> exception = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<CommandResult> commandResult = new SimpleObjectProperty<>();
    private final Storage storage;

    /**
     * Initializes a ModelManager with the given listEntryBook, userPrefs, and storage
     */
    public ModelManager(ReadOnlyEntryBook listEntryBook, ReadOnlyUserPrefs userPrefs, Storage storage) {
        super();
        requireAllNonNull(listEntryBook, userPrefs, storage);

        logger.fine("Initializing with list context address book: " + listEntryBook + " and user prefs " + userPrefs);

        this.listEntryBook = new EntryBook(listEntryBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.storage = storage;

        // Save the entry book to storage whenever it is modified.
        this.listEntryBook.addListener(this::saveToStorageListener);

        displayEntryBook(this.listEntryBook);
        filteredEntries = new FilteredList<>(this.displayedEntryList);
        filteredEntries.addListener(this::ensureSelectedEntryIsValid);
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
    public void setListEntryBook(ReadOnlyEntryBook listEntryBook) {
        this.listEntryBook.resetData(listEntryBook);
    }

    @Override
    public ReadOnlyEntryBook getListEntryBook() {
        return listEntryBook;
    }

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return listEntryBook.hasPerson(entry);
    }

    @Override
    public void deleteEntry(Entry target) {
        listEntryBook.removePerson(target);
    }

    @Override
    public void addEntry(Entry entry) {
        listEntryBook.addEntry(entry);
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

        listEntryBook.setPerson(target, editedEntry);
    }

    @Override
    public void clearEntryBook() {
        listEntryBook.clear();
    }

    @Override
    public void displayEntryBook(ReadOnlyEntryBook entryBook) {
        displayedEntryList.set(entryBook.getEntryList());
    }

    //=========== Storage ===================================================================================

    @Override
    public Storage getStorage() {
        return storage;
    }

    //=========== Filtered Entry List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the internal list of
     * {@code listEntryBook}
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
     * Ensures {@code selectedEntry} is a valid entry in {@code filteredEntries}.
     */
    private void ensureSelectedEntryIsValid(ListChangeListener.Change<? extends Entry> change) {
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
            storage.saveAddressBook(listEntryBook);
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

        boolean stateCheck = listEntryBook.equals(other.listEntryBook)
                && userPrefs.equals(other.userPrefs)
                && filteredEntries.equals(other.filteredEntries)
                && Objects.equals(selectedEntry.get(), other.selectedEntry.get())
                && Objects.equals(currentViewMode.get(), other.currentViewMode.get())
                && Objects.equals(commandResult.get(), other.commandResult.get());

        if (exception.get() == null && other.exception.get() == null) { // both don't have exceptions set
            return stateCheck;
        } else if (exception.get() != null && other.exception.get() != null) { // both have exceptions set
            return stateCheck
                    && exception.get().getClass().equals(other.exception.get().getClass())
                    && exception.get().getMessage().equals(other.exception.get().getMessage());
        } else { // not equal as one has exception set and the other does not
            return false;
        }
    }

    @Override
    public Model clone() {
        Model clonedModel = new ModelManager(this.listEntryBook, this.userPrefs, this.storage);
        clonedModel.setContext(this.getContext());
        return clonedModel;
    }

    @Override
    public void setContext(ModelContext context) {
        switch (context) {
        case CONTEXT_LIST:
            displayEntryBook(this.listEntryBook);
            break;
        case CONTEXT_ARCHIVE:
            // something else
            break;
        default:
        }
        this.context = context;
    }

    @Override
    public ModelContext getContext() {
        return context;
    }

    @Override
    public void archiveEntry(Entry target) {
        return;
    }

    @Override
    public void unarchiveEntry(Entry entry) {
        return;
    }
}
