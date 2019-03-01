package seedu.address.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.EntryBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandHistory history;
    private final EntryBookParser entryBookParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        history = new CommandHistory();
        entryBookParser = new EntryBookParser();

        // Save the models' address book to storage whenever it is modified.
        // In future, this can be moved to Model, but currently Model does not have a reference to Storage.
        model.getAddressBook().addListener(observable -> {
            logger.info("Address book modified, saving to file.");
            try {
                storage.saveAddressBook(model.getAddressBook());
            } catch (IOException ioe) {
                model.setException(new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe));
            }
        });
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        try {
            Command command = entryBookParser.parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            history.add(commandText);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyEntryBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Entry> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyProperty<Entry> selectedPersonProperty() {
        return model.selectedPersonProperty();
    }

    @Override
    public void setSelectedPerson(Entry entry) {
        model.setSelectedPerson(entry);
    }

    @Override
    public ReadOnlyProperty<Exception> exceptionProperty() {
        return model.exceptionProperty();
    }

    @Override
    public void setException(Exception exception) {
        model.setException(exception);
    }

    @Override
    public ReadOnlyProperty<CommandResult> commandResultProperty() {
        return model.commandResultProperty();
    }

    @Override
    public void setCommandResult(CommandResult commandResult) {
        model.setCommandResult(commandResult);
    }

}
