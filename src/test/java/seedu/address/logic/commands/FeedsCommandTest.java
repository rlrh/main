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

public class FeedsCommandTest {

    private Model model = new TypicalModelManagerStub();
    private Model expectedModel = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        expectedModel.setContext(ModelContext.CONTEXT_FEEDS);
    }

    @Test
    public void execute_notFiltered_showsSameFeeds() {
        assertCommandSuccess(new FeedsCommand(), model, commandHistory,
                FeedsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_feedsIsFiltered_showsEverything() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);
        assertCommandSuccess(new FeedsCommand(), model, commandHistory,
                FeedsCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
