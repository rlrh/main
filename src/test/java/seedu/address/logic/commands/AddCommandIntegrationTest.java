package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalEntries.CRUX_LINK_FINAL;
import static seedu.address.testutil.TypicalEntries.CRUX_LINK_NO_TITLE_NO_DESCRIPTION;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_FINAL;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_TITLE_NO_DESCRIPTION;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_FINAL;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_TITLE_NO_DESCRIPTION;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_newEntry_success() {
        Entry validEntry = new EntryBuilder().build();

        Model expectedModel = model.clone();
        expectedModel.addEntry(validEntry);

        assertCommandSuccess(new AddCommand(validEntry), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validEntry), expectedModel);
    }

    @Test
    public void execute_stubEntryHasNoTitleAndNoDescription_titleAndDescriptionReplaced() {
        Model expectedModel = model.clone();
        expectedModel.addEntry(STUB_LINK_FINAL);

        assertCommandSuccess(new AddCommand(STUB_LINK_NO_TITLE_NO_DESCRIPTION), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, STUB_LINK_FINAL), expectedModel);
    }

    @Test
    public void execute_realEntryHasNoTitleAndNoDescription_titleAndDescriptionReplaced() {
        Model expectedModel = model.clone();
        expectedModel.addEntry(REAL_LINK_FINAL);

        assertCommandSuccess(new AddCommand(REAL_LINK_NO_TITLE_NO_DESCRIPTION), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, REAL_LINK_FINAL), expectedModel);
    }

    @Test
    public void execute_cruxEntryHasNoTitleAndNoDescription_titleAndDescriptionReplaced() {
        Model expectedModel = model.clone();
        expectedModel.addEntry(CRUX_LINK_FINAL);

        assertCommandSuccess(new AddCommand(CRUX_LINK_NO_TITLE_NO_DESCRIPTION), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, CRUX_LINK_FINAL), expectedModel);
    }

    @Test
    public void execute_duplicateEntry_throwsCommandException() {
        Entry entryInList = model.getEntryBook().getEntryList().get(0);
        assertCommandFailure(new AddCommand(entryInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
