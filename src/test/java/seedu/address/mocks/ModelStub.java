package seedu.address.mocks;

import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.entry.Entry;
import seedu.address.storage.Storage;
import seedu.address.ui.ViewMode;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Storage getStorage() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteArticle(URL url) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Path> addArticle(URL url, byte[] articleContent) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getListEntryBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }


    @Override
    public void setListEntryBookFilePath(Path listEntryBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getArchivesEntryBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setArchivesEntryBookFilePath(Path archivesEntryBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setArticleDataDirectoryPath(Path articleDataDirectoryPath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasOfflineCopy(URL url) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<URL> getOfflineLink(URL url) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasEntry(Entry entry) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addListEntry(Entry entry, Optional<byte[]> articleContent) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setListEntryBook(ReadOnlyEntryBook listEntryBook) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasListEntry(Entry listEntry) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyEntryBook getListEntryBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteListEntry(Entry target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setListEntry(Entry target, Entry editedEntry) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void clearListEntryBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void refreshEntry(Entry entry, byte[] articleContent) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addArchivesEntry(Entry entry) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyEntryBook getArchivesEntryBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasArchivesEntry(Entry archiveEntry) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteArchivesEntry(Entry target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void clearArchivesEntryBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setFeedsEntryBook(ReadOnlyEntryBook feedsEntryBook) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyEntryBook getFeedsEntryBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasFeedsEntry(Entry feed) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteFeedsEntry(Entry target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addFeedsEntry(Entry feed) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void clearFeedsEntryBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setSearchEntryBook(ReadOnlyEntryBook searchEntryBook) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Entry> getFilteredEntryList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredEntryList(Predicate<Entry> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyProperty<Entry> selectedEntryProperty() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Entry getSelectedEntry() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setSelectedEntry(Entry entry) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyProperty<ViewMode> viewModeProperty() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ViewMode getViewMode() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setViewMode(ViewMode viewMode) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyProperty<Exception> exceptionProperty() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Exception getException() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setException(Exception exceptionToBePropagated) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyProperty<CommandResult> commandResultProperty() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public CommandResult getCommandResult() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setCommandResult(CommandResult result) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Model clone() {
        return this;
    }

    @Override
    public ReadOnlyProperty<ModelContext> contextProperty() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ModelContext getContext() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setContext(ModelContext context) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void archiveEntry(Entry target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void unarchiveEntry(Entry entry, Optional<byte[]> articleContent) {
        throw new AssertionError("This method should not be called.");
    }
}
