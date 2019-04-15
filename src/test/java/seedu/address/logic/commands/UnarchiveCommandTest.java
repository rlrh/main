package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showEntryAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showNoEntry;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.mocks.StorageStub;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.entry.Entry;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnarchiveCommand}.
 */
public class UnarchiveCommandTest {

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model.setContext(ModelContext.CONTEXT_ARCHIVES);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Entry entryToUnarchive = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(UnarchiveCommand.MESSAGE_UNARCHIVE_ENTRY_SUCCESS, entryToUnarchive);

        ModelManager expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
            model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);
        expectedModel.unarchiveEntry(entryToUnarchive, Optional.empty());

        assertCommandSuccess(unarchiveCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEntryList().size() + 1);
        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(outOfBoundIndex);

        assertCommandFailure(unarchiveCommand, model, commandHistory, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Entry entryToUnarchive = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(UnarchiveCommand.MESSAGE_UNARCHIVE_ENTRY_SUCCESS, entryToUnarchive);

        ModelManager expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
            model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);
        expectedModel.unarchiveEntry(entryToUnarchive, Optional.empty());
        showNoEntry(expectedModel);

        assertCommandSuccess(unarchiveCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getListEntryBook().getEntryList().size());

        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(outOfBoundIndex);

        assertCommandFailure(unarchiveCommand, model, commandHistory, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnarchiveCommand unarchiveFirstCommand = new UnarchiveCommand(INDEX_FIRST_ENTRY);
        UnarchiveCommand unarchiveSecondCommand = new UnarchiveCommand(INDEX_SECOND_ENTRY);

        // same object -> returns true
        assertTrue(unarchiveFirstCommand.equals(unarchiveFirstCommand));

        // same values -> returns true
        UnarchiveCommand unarchiveFirstCommandCopy = new UnarchiveCommand(INDEX_FIRST_ENTRY);
        assertTrue(unarchiveFirstCommand.equals(unarchiveFirstCommandCopy));

        // different types -> returns false
        assertFalse(unarchiveFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unarchiveFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(unarchiveFirstCommand.equals(unarchiveSecondCommand));
    }
}
