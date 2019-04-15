package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TemporaryStorageManager;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.OfflineMode;

/**
 * Contains integration tests (interaction with the Model) for {@code OfflineModeCommand}.
 */
public class OfflineModeCommandTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Model model;
    private CommandHistory commandHistory;

    @Before
    public void setUp() throws IOException {
        model = new TypicalModelManagerStub(new TemporaryStorageManager(temporaryFolder));
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_offlineMode_success() {
        CommandResult commandResult;

        // disable
        commandResult = new OfflineModeCommand(OfflineMode.DISABLED).execute(model, commandHistory);
        assertEquals(OfflineMode.DISABLED, model.getOfflineModeProperty().getValue());
        assertEquals(OfflineModeCommand.MESSAGE_SET_VIEW_MODE_DISABLED, commandResult.getFeedbackToUser());

        // enable
        commandResult = new OfflineModeCommand(OfflineMode.ENABLED).execute(model, commandHistory);
        assertEquals(OfflineMode.ENABLED, model.getOfflineModeProperty().getValue());
        assertEquals(OfflineModeCommand.MESSAGE_SET_VIEW_MODE_ENABLED, commandResult.getFeedbackToUser());

    }

    @Test
    public void equals() {
        OfflineModeCommand viewModeFirstCommand = new OfflineModeCommand(OfflineMode.DISABLED);
        OfflineModeCommand viewModeSecondCommand = new OfflineModeCommand(OfflineMode.ENABLED);

        // same object -> returns true
        assertEquals(viewModeFirstCommand, viewModeFirstCommand);

        // same values -> returns true
        OfflineModeCommand viewModeFirstCommandCopy = new OfflineModeCommand(OfflineMode.DISABLED);
        assertEquals(viewModeFirstCommand, viewModeFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(viewModeFirstCommand, 1);

        // null -> returns false
        assertNotEquals(viewModeFirstCommand, null);

        // different entry -> returns false
        assertNotEquals(viewModeFirstCommand, viewModeSecondCommand);
    }

}
