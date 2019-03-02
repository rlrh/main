package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.StorageStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ExitCommandTest {
    private Model model = new ModelManager(
            new EntryBook(),
            new UserPrefs(),
            new StorageStub()
    );
    private Model expectedModel = new ModelManager(
            new EntryBook(),
            new UserPrefs(),
            new StorageStub()
    );
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(new ExitCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }
}
