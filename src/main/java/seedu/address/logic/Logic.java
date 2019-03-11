package seedu.address.logic;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.ui.ViewMode;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the EntryBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyEntryBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Entry> getFilteredPersonList();

    /**
     * Returns an unmodifiable view of the list of commands entered by the user.
     * The list is ordered from the least recent command to the most recent command.
     */
    ObservableList<String> getHistory();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Selected entry in the filtered entry list.
     * null if no entry is selected.
     *
     * @see seedu.address.model.Model#selectedPersonProperty()
     */
    ReadOnlyProperty<Entry> selectedPersonProperty();

    /**
     * Sets the selected entry in the filtered entry list.
     *
     * @see seedu.address.model.Model#setSelectedPerson(Entry)
     */
    void setSelectedPerson(Entry entry);

    /**
     * Current view mode.
     *
     * @see seedu.address.model.Model#viewModeProperty()
     */
    ReadOnlyProperty<ViewMode> viewModeProperty();

    /**
     * Sets the view mode.
     *
     * @see seedu.address.model.Model#setViewMode(ViewMode)
     */
    void setViewMode(ViewMode viewMode);

    /**
     * Propagated exception.
     * null if no exception.
     *
     * @see seedu.address.model.Model#exceptionProperty()
     */
    ReadOnlyProperty<Exception> exceptionProperty();

    /**
     * Sets the exception to be propagated.
     *
     * @see seedu.address.model.Model#setException(Exception)
     */
    void setException(Exception exception);

    /**
     * Command result from manual setting.
     * null if no manually set command result.
     * FUTURE: Command result from latest command execution or manual setting.
     *
     * @see seedu.address.model.Model#commandResultProperty()
     */
    ReadOnlyProperty<CommandResult> commandResultProperty();

    /**
     * Sets the command result manually.
     *
     * @see seedu.address.model.Model#setCommandResult(CommandResult)
     */
    void setCommandResult(CommandResult commandResult);
}
