package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;

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
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;
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

    private final EntryBook entryBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Entry> filteredEntries;

    private final SimpleObjectProperty<Entry> selectedEntry = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<ViewMode> currentViewMode = new SimpleObjectProperty<>(ViewMode.BROWSER);
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

        entryBook = new EntryBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredEntries = new FilteredList<>(entryBook.getEntryList());
        filteredEntries.addListener(this::ensureSelectedPersonIsValid);

        this.storage = storage;

        // Save the entry book to storage whenever it is modified.
        entryBook.addListener(this::saveToStorageListener);
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
        this.entryBook.resetData(entryBook);
    }

    @Override
    public ReadOnlyEntryBook getEntryBook() {
        return entryBook;
    }

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return entryBook.hasPerson(entry);
    }

    @Override
    public void deleteEntry(Entry target) {
        entryBook.removePerson(target);
    }

    @Override
    public Entry addEntry(Entry entry) {

        // Represents a string that tries out replacement candidates and accepts suitable ones
        class CandidateString {

            private String value;

            private CandidateString(String initialValue) {
                this.value = initialValue;
            }

            private void tryout(String candidate) {
                requireNonNull(candidate);
                String trimmedCandidate = candidate.trim();
                if (!trimmedCandidate.isEmpty()) {
                    this.value = trimmedCandidate;
                }
            }

            @Override
            public String toString() {
                return this.value;
            }

        }

        // Initial data
        Title title = entry.getTitle();
        Description description = entry.getDescription();
        final String urlString = entry.getLink().value;
        final boolean noTitleOrDescription = title.isEmpty() || description.isEmpty();

        // Candidates to replace empty title and description
        CandidateString candidateTitle = new CandidateString("Untitled");
        CandidateString candidateDescription = new CandidateString("No description");

        // 2nd choice - extract candidates just from URL
        if (noTitleOrDescription) {
            try {
                URL url = new URL(urlString);
                candidateTitle.tryout(url
                        .getFile()
                        .replaceAll("[^a-zA-Z0-9]+"," ")
                        .trim()
                ); // second choice - cleaned up file name
                candidateDescription.tryout(url
                        .getHost()
                        .trim()
                ); // third choice - host name
            } catch (MalformedURLException mue) {
                // Do nothing if URL is malformed
                logger.warning("Malformed URL: " + urlString);
            }
        }

        try {

            // Download article content to local storage
            byte[] articleContent = Network.fetchAsBytes(urlString);
            storage.addArticle(urlString, articleContent);

            // 1st choice - extract candidates by processing through Crux
            if (noTitleOrDescription) {
                String rawHtml = new String(articleContent);
                Article article = ArticleExtractor.with(urlString, rawHtml)
                        .extractMetadata()
                        .extractContent()
                        .article();
                candidateTitle.tryout(article.title); // 1st choice - extract title
                candidateDescription.tryout(article
                        .document
                        .text()
                        .substring(0, Math.min(128, article.document.text().length()))
                ); // 2nd choice - first 128 chars of document body text
                candidateDescription.tryout(article.description); // 1st choice - extract description
            }

        } catch (IOException ioe) {
            // Do nothing if fail to fetch the page
            logger.warning("Failed to fetch URL: " + urlString);
        }

        // Replace empty title with best candidate
        try {
            if (title.isEmpty()) {
                title = ParserUtil.parseTitle(Optional.of(candidateTitle.toString()));
            }
        } catch (ParseException pe) {
            // Do not replace existing title if exception is thrown
            logger.warning("Failed to replace title. " + pe.getMessage());
        }

        // Replace empty description with best candidate
        try {
            if (description.isEmpty()) {
                description = ParserUtil.parseDescription(Optional.of(candidateDescription.toString()));
            }
        } catch (ParseException pe) {
            // Do not replace existing description if exception is thrown
            logger.warning("Failed to replace description. " + pe.getMessage());
        }

        // Update entry with best title and description candidates and add it to entry book
        Entry newEntry = new Entry(title, description, entry.getLink(), entry.getAddress(), entry.getTags());
        entryBook.addPerson(newEntry);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_PERSONS);

        // Return defensive copy of updated entry
        return new Entry(
                newEntry.getTitle(),
                newEntry.getDescription(),
                newEntry.getLink(),
                newEntry.getAddress(),
                newEntry.getTags()
        );
    }

    @Override
    public void setEntry(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);

        entryBook.setPerson(target, editedEntry);
    }

    @Override
    public void clearEntryBook() {
        entryBook.clear();
    }

    //=========== Storage ===================================================================================

    @Override
    public Storage getStorage() {
        return storage;
    }

    //=========== Filtered Entry List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the internal list of
     * {@code entryBook}
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
            storage.saveAddressBook(entryBook);
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

        boolean stateCheck = entryBook.equals(other.entryBook)
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
        return new ModelManager(this.entryBook, this.userPrefs, this.storage);
    }

}
