package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Entry;

public class DeleteAllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allListEntriesDeletedSuccessfully() throws Exception {
        Model model = new TypicalModelManagerStub();

        int numEntries = model.getFilteredEntryList().size();
        List<Entry> entriesToDelete = new ArrayList<>(model.getFilteredEntryList());

        assertTrue(
            entriesToDelete.stream()
                .allMatch(model::hasListEntry)
        );
        assertFalse(model.getFilteredEntryList().isEmpty());

        CommandResult commandResult = new DeleteAllCommand().execute(model, commandHistory);
        assertEquals(
            String.format(DeleteAllCommand.MESSAGE_SUCCESS, numEntries),
            commandResult.getFeedbackToUser());

        assertTrue(
            entriesToDelete.stream()
                .noneMatch(model::hasEntry)
        );
        assertTrue(model.getFilteredEntryList().isEmpty());

        // Executing the command again results in no entries deleted
        EntryBook expectedListEntryBook = new EntryBook(model.getListEntryBook());

        commandResult = new DeleteAllCommand().execute(model, commandHistory);
        assertEquals(
            String.format(DeleteAllCommand.MESSAGE_SUCCESS, 0),
            commandResult.getFeedbackToUser());

        assertEquals(model.getListEntryBook(), expectedListEntryBook);
    }

    @Test
    public void execute_allArchiveEntriesDeletedSuccessfully() throws Exception {
        Model model = new TypicalModelManagerStub();
        model.setContext(ModelContext.CONTEXT_ARCHIVES);

        int numEntries = model.getFilteredEntryList().size();
        List<Entry> entriesToDelete = new ArrayList<>(model.getFilteredEntryList());

        assertTrue(
            entriesToDelete.stream()
                .allMatch(model::hasArchivesEntry)
        );
        assertFalse(model.getFilteredEntryList().isEmpty());

        CommandResult commandResult = new DeleteAllCommand().execute(model, commandHistory);
        assertEquals(
            String.format(DeleteAllCommand.MESSAGE_SUCCESS, numEntries),
            commandResult.getFeedbackToUser());

        assertTrue(
            entriesToDelete.stream()
                .noneMatch(model::hasEntry)
        );
        assertTrue(model.getFilteredEntryList().isEmpty());

        // Executing the command again results in no entries deleted
        EntryBook expectedArchivesEntryBook = new EntryBook(model.getArchivesEntryBook());

        commandResult = new DeleteAllCommand().execute(model, commandHistory);
        assertEquals(
            String.format(DeleteAllCommand.MESSAGE_SUCCESS, 0),
            commandResult.getFeedbackToUser());

        assertEquals(model.getArchivesEntryBook(), expectedArchivesEntryBook);
    }
}
