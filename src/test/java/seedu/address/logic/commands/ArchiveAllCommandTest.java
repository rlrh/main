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
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.TypicalEntries;

public class ArchiveAllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allEntriesArchivedSuccessfully() throws Exception {
        Model model = new TypicalModelManagerStub();

        int numEntries = model.getFilteredEntryList().size();
        List<Entry> entriesToArchive = new ArrayList<>(model.getFilteredEntryList());

        assertTrue(
            entriesToArchive.stream()
                .noneMatch(model::hasArchivesEntry)
        );
        assertTrue(
            entriesToArchive.stream()
                .allMatch(model::hasListEntry)
        );
        assertFalse(model.getFilteredEntryList().isEmpty());

        CommandResult commandResult = new ArchiveAllCommand().execute(model, commandHistory);
        assertEquals(
            String.format(ArchiveAllCommand.MESSAGE_SUCCESS, numEntries),
            commandResult.getFeedbackToUser());

        assertTrue(
            entriesToArchive.stream()
                .allMatch(model::hasArchivesEntry)
        );
        assertTrue(
            entriesToArchive.stream()
                .noneMatch(model::hasListEntry)
        );
        assertTrue(model.getFilteredEntryList().isEmpty());

        // Executing the command again results in no entries archived because they are all duplicates
        model.setListEntryBook(TypicalEntries.getTypicalListEntryBook());

        assertTrue(
            entriesToArchive.stream()
                .allMatch(model::hasArchivesEntry)
        );
        assertTrue(
            entriesToArchive.stream()
                .allMatch(model::hasListEntry)
        );
        assertFalse(model.getFilteredEntryList().isEmpty());

        commandResult = new ArchiveAllCommand().execute(model, commandHistory);
        assertEquals(
            String.format(ArchiveAllCommand.MESSAGE_SUCCESS, 0),
            commandResult.getFeedbackToUser());

        assertTrue(
            entriesToArchive.stream()
                .allMatch(model::hasArchivesEntry)
        );
        assertTrue(
            entriesToArchive.stream()
                .noneMatch(model::hasListEntry)
        );
        assertTrue(model.getFilteredEntryList().isEmpty());
    }

}
