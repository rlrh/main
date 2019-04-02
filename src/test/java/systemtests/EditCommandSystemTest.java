package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TECH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static seedu.address.testutil.TypicalEntries.BOB;
import static seedu.address.testutil.TypicalEntries.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.model.Model;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBuilder;

public class EditCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void edit() {
        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_ENTRY;
        Entry entryToEdit = getModel().getFilteredEntryList().get(index.getZeroBased());

        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + TITLE_DESC_BOB + "  "
                + DESCRIPTION_DESC_BOB + " " + TAG_DESC_SCIENCE + " " + TAG_DESC_TECH + " ";
        Entry editedEntry = new EntryBuilder(BOB)
            .withLink(entryToEdit.getLink().value)
            .withAddress(entryToEdit.getAddress().value)
            .withTags(VALID_TAG_SCIENCE, VALID_TAG_TECH)
            .build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: edit a entry with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        assertCommandSuccess(command, index, editedEntry);

        /* Case: edit a entry with new values same as another entry's values -> edited */
        Entry firstEntry = editedEntry;
        assertTrue(getModel().getListEntryBook().getEntryList().contains(firstEntry));
        assertEquals(firstEntry, new EntryBuilder(BOB)
                                        .withLink(entryToEdit.getLink().value)
                                        .withAddress(entryToEdit.getAddress().value)
                                        .build());

        index = INDEX_SECOND_ENTRY;
        entryToEdit = getModel().getFilteredEntryList().get(index.getZeroBased());
        assertNotEquals(entryToEdit, new EntryBuilder(firstEntry)
            .withLink(entryToEdit.getLink().value)
            .withAddress(entryToEdit.getAddress().value));

        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        editedEntry = new EntryBuilder(firstEntry)
            .withLink(entryToEdit.getLink().value)
            .withAddress(entryToEdit.getAddress().value)
            .build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_ENTRY;
        entryToEdit = getModel().getFilteredEntryList().get(index.getZeroBased());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        editedEntry = new EntryBuilder(entryToEdit).withTags().build();
        assertCommandSuccess(command, index, editedEntry);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered entry list, edit index within bounds of entry book and entry list -> edited */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_ENTRY;
        assertTrue(index.getZeroBased() < getModel().getFilteredEntryList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + TITLE_DESC_BOB;
        entryToEdit = getModel().getFilteredEntryList().get(index.getZeroBased());
        editedEntry = new EntryBuilder(entryToEdit).withTitle(VALID_TITLE_BOB).build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: filtered entry list, edit index within bounds of entry book but out of bounds of entry list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getListEntryBook().getEntryList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + TITLE_DESC_BOB,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a entry card is selected -------------------------- */

        /* Case: selects second card in the entry list, edit a entry -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllPersons();
        Index indexSelected = INDEX_SECOND_ENTRY;
        selectPerson(indexSelected);
        index = INDEX_FIRST_ENTRY;
        entryToEdit = getModel().getFilteredEntryList().get(index.getZeroBased());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        editedEntry = new EntryBuilder(BOB)
            .withLink(entryToEdit.getLink().value)
            .withAddress(entryToEdit.getAddress().value)
            .build();
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new entry's name
        assertCommandSuccess(command, index, editedEntry, indexSelected);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + TITLE_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + TITLE_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredEntryList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + TITLE_DESC_BOB,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + TITLE_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid title -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ENTRY.getOneBased() + INVALID_TITLE_DESC,
                Title.formExceptionMessage(INVALID_TITLE.trim()));

        /* Case: invalid description -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ENTRY.getOneBased() + INVALID_DESCRIPTION_DESC,
                Description.formExceptionMessage(INVALID_DESCRIPTION.trim()));

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ENTRY.getOneBased() + INVALID_TAG_DESC,
                Tag.formExceptionMessage(INVALID_TAG.trim()));
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Entry, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Entry, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Entry editedEntry) {
        assertCommandSuccess(command, toEdit, editedEntry, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the entry at index {@code toEdit} being
     * updated to values specified {@code editedEntry}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Entry editedEntry,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.setListEntry(expectedModel.getFilteredEntryList().get(toEdit.getZeroBased()), editedEntry);
        expectedModel.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_ENTRY_SUCCESS, editedEntry), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatusExcludingCount();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar excluding count remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertResultDisplayShowsErrorStyle();
        assertStatusBarExcludingCountUnchanged();
    }
}
