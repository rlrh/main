package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE;

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
    public void execute_newEntryHasNoTitleAndNoDescription_titleAndDescriptionReplacedSuccess() {
        Model expectedModel = model.clone();
        expectedModel.addEntry(READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE);

        assertCommandSuccess(new AddCommand(READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE),
                model,
                commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE),
                expectedModel);
    }

    @Test
    public void execute_newEntryHasNoTitle_titleReplacedSuccess() {
        Model expectedModel = model.clone();
        expectedModel.addEntry(READABILITY_LINK_NO_TITLE_COMPLETE);

        assertCommandSuccess(new AddCommand(READABILITY_LINK_NO_TITLE_INCOMPLETE), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, READABILITY_LINK_NO_TITLE_COMPLETE), expectedModel);
    }

    @Test
    public void execute_newEntryHasNoDescription_descriptionReplacedSuccess() {
        Model expectedModel = model.clone();
        expectedModel.addEntry(READABILITY_LINK_NO_DESCRIPTION_COMPLETE);

        assertCommandSuccess(new AddCommand(READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, READABILITY_LINK_NO_DESCRIPTION_COMPLETE), expectedModel);
    }

    @Test
    public void execute_duplicateEntry_throwsCommandException() {
        Entry entryInList = model.getListEntryBook().getEntryList().get(0);
        assertCommandFailure(new AddCommand(entryInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
