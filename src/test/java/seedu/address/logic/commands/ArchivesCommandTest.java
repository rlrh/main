package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showEntryAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ArchivesCommandTest {

    private Model model = new TypicalModelManagerStub();
    private Model expectedModel = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model.setContext(ModelContext.CONTEXT_ARCHIVES);
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);
    }

    @Test
    public void execute_archivesIsNotFiltered_showsSameArchives() {
        assertCommandSuccess(new ArchivesCommand(), model, commandHistory,
            ArchivesCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_archivesIsFiltered_showsEverything() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);
        assertCommandSuccess(new ArchivesCommand(), model, commandHistory,
            ArchivesCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
