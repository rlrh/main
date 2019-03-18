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

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;
import com.google.common.io.Files;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.model.util.Candidate;
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
    public boolean hasEntryEqualTo(Entry entry) {
        requireNonNull(entry);
        return entryBook.hasEntryEqualTo(entry);
    }

    @Override
    public void deleteEntry(Entry target) {
        entryBook.removePerson(target);
    }

    @Override
    public Entry addEntry(Entry entry) {

        // Initial data
        final Title title = entry.getTitle();
        final Description description = entry.getDescription();
        final boolean noTitleOrDescription = title.isEmpty() || description.isEmpty();
        final String urlString = entry.getLink().value;

        // Candidates to replace empty title and description
        Candidate<Title> candidateTitle = new Candidate<>(new Title("Untitled"), (String s) -> {
            try {
                return Optional.of(ParserUtil.parseTitle(Optional.of(s)));
            } catch (ParseException pe) {
                logger.warning("Failed to replace title candidate. " + pe.getMessage());
                return Optional.empty();
            }
        });
        Candidate<Description> candidateDescription = new Candidate<>(new Description("No description"), (String s) -> {
            try {
                return Optional.of(ParserUtil.parseDescription(Optional.of(s)));
            } catch (ParseException pe) {
                logger.warning("Failed to replace description candidate. " + pe.getMessage());
                return Optional.empty();
            }
        });

        // First try - extract candidates just from URL
        if (noTitleOrDescription) {
            try {
                URL url = new URL(urlString);
                String baseName = Files.getNameWithoutExtension(url.getPath())
                        .replaceAll("\n", "") // remove newline chars
                        .replaceAll("\r", "") // remove carriage return chars
                        .replaceAll("[^a-zA-Z0-9]+", " ") // replace special chars with spaces
                        .trim();
                candidateTitle.tryout(WordUtils.capitalizeFully(baseName)); // title 3rd choice - cleaned up base name
                candidateDescription.tryout(url.getHost().trim()); // desc 4th choice - host name
            } catch (MalformedURLException mue) {
                // Skip if URL is malformed
                logger.warning("Malformed URL: " + urlString);
            }
        }

        try {

            // Download article content to local storage
            byte[] articleContent = Network.fetchAsBytes(urlString);
            storage.addArticle(urlString, articleContent);

            if (noTitleOrDescription) {

                // Second try - extract candidates by parsing through Jsoup
                String html = new String(articleContent);
                Document document = Jsoup.parse(html);
                candidateTitle.tryout(document.title().trim()); // title 2nd choice - document title element
                candidateDescription.tryout(StringUtil.getFirstNWords(document.body().text(), 24)
                       .trim()); // desc 3rd choice - first N words of raw document body text

                // Third try - extract candidates by processing through Crux
                Article article = ArticleExtractor.with(urlString, document)
                        .extractMetadata()
                        .extractContent()
                        .article();
                candidateTitle.tryout(article.title.trim()); // title 1st choice - extract title
                candidateDescription
                        .tryout(StringUtil.getFirstNWords(article.document.text(), 24)
                                .trim()) // desc 2nd choice - first N words of cleaned-up document body text
                        .tryout(article.description.trim()); // desc 1st choice - extract description

            }

        } catch (IOException ioe) {
            // Do nothing if fail to fetch the page
            logger.warning("Failed to fetch URL: " + urlString);
        }

        // Add updated entry to entry book
        Entry updatedEntry = new Entry(
                title.isEmpty() ? candidateTitle.get() : title, // replace title if empty
                description.isEmpty() ? candidateDescription.get() : description, // replace description if empty
                entry.getLink(),
                entry.getAddress(),
                entry.getTags()
        );
        entryBook.addPerson(updatedEntry);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_PERSONS);

        // Return defensive copy of updated entry
        return new Entry(
                updatedEntry.getTitle(),
                updatedEntry.getDescription(),
                updatedEntry.getLink(),
                updatedEntry.getAddress(),
                updatedEntry.getTags()
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
