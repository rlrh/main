package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.CommandResult;
import seedu.address.mocks.StorageStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonEntryBookStorage;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.testutil.TestUtil;
import systemtests.ModelHelper;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final Path SAVE_LOCATION_LIST_ENTRYBOOK_FOR_TESTING =
        TestUtil.getFilePathInSandboxFolder("sampleListEntryBookData.json");

    public static final Path SAVE_LOCATION_ARCHIVES_ENTRYBOOK_FOR_TESTING =
        TestUtil.getFilePathInSandboxFolder("sampleArchiveEntryBookData.json");

    protected static final Path DEFAULT_PREF_FILE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("pref_testing.json");

    protected Supplier<ReadOnlyEntryBook> initialListEntryBookDataSupplier = () -> null;
    protected Supplier<ReadOnlyEntryBook> initialArchivesEntryBookDataSupplier = () -> null;
    protected Path saveFileLocationListEntryBook = SAVE_LOCATION_LIST_ENTRYBOOK_FOR_TESTING;
    protected Path saveFileLocationArchivesEntryBook = SAVE_LOCATION_ARCHIVES_ENTRYBOOK_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(
        Supplier<ReadOnlyEntryBook> initialListEntryBookDataSupplier,
        Supplier<ReadOnlyEntryBook> initialArchivesEntryBookDataSupplier,
        Path saveFileLocationListEntryBook,
        Path saveFileLocationArchivesEntryBook) {
        super();
        this.initialListEntryBookDataSupplier = initialListEntryBookDataSupplier;
        this.initialArchivesEntryBookDataSupplier = initialArchivesEntryBookDataSupplier;
        this.saveFileLocationListEntryBook = saveFileLocationListEntryBook;
        this.saveFileLocationArchivesEntryBook = saveFileLocationArchivesEntryBook;

        // If some initial local data has been provided, write those to the file
        if (initialListEntryBookDataSupplier.get() != null) {
            JsonEntryBookStorage jsonEntryBookStorage = new JsonEntryBookStorage(saveFileLocationListEntryBook);
            try {
                jsonEntryBookStorage.saveEntryBook(initialListEntryBookDataSupplier.get());
            } catch (IOException ioe) {
                throw new AssertionError(ioe);
            }
        }
        if (initialArchivesEntryBookDataSupplier.get() != null) {
            JsonEntryBookStorage jsonEntryBookStorage = new JsonEntryBookStorage(saveFileLocationArchivesEntryBook);
            try {
                jsonEntryBookStorage.saveEntryBook(initialArchivesEntryBookDataSupplier.get());
            } catch (IOException ioe) {
                throw new AssertionError(ioe);
            }
        }
    }

    @Override
    protected Config initConfig(Path configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.setGuiSettings(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        userPrefs.setListEntryBookFilePath(saveFileLocationListEntryBook);
        userPrefs.setArchivesEntryBookFilePath(saveFileLocationArchivesEntryBook);
        return userPrefs;
    }

    /**
     * Returns a defensive copy of the list entry book data stored inside the storage file.
     */
    public EntryBook readStorageListEntryBook() {
        try {
            return new EntryBook(storage.readListEntryBook().get());
        } catch (DataConversionException dce) {
            throw new AssertionError("Data is not in the EntryBook format.", dce);
        } catch (IOException ioe) {
            throw new AssertionError("Storage file cannot be found.", ioe);
        }
    }

    /**
     * Returns a defensive copy of the archives entry book data stored inside the storage file.
     */
    public EntryBook readStorageArchivesEntryBook() {
        try {
            return new EntryBook(storage.readArchivesEntryBook().get());
        } catch (DataConversionException dce) {
            throw new AssertionError("Data is not in the EntryBook format.", dce);
        } catch (IOException ioe) {
            throw new AssertionError("Storage file cannot be found.", ioe);
        }
    }

    /**
     * Returns the file path of the storage file for the list entry book.
     */
    public Path getListEntryBookStorageSaveLocation() {
        return storage.getListEntryBookFilePath();
    }

    /**
     * Returns the file path of the storage file for the archives entry book.
     */
    public Path getArchivesEntryBookStorageSaveLocation() {
        return storage.getArchivesEntryBookFilePath();
    }

    /**
     * Returns a defensive copy of the model.
     */
    public Model getModel() {
        Model copy = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
                model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        copy.setContext(model.getContext());
        ModelHelper.setFilteredList(copy, model.getFilteredEntryList());
        return copy;
    }

    /**
     * Sets the command result in the app.
     * @param commandResult command result to set
     */
    public void setCommandResult(CommandResult commandResult) {
        model.setCommandResult(commandResult);
    }

    /**
     * Sets the exception in the app.
     * @param e exception to set
     */
    public void setException(Exception e) {
        model.setException(e);
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
