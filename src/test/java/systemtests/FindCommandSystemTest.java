package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_ENTRIES_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.EntryUtil.getFindEntryDescriptorDetails;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BENSON;
import static seedu.address.testutil.TypicalEntries.CARL;
import static seedu.address.testutil.TypicalEntries.DANIEL;
import static seedu.address.testutil.TypicalEntries.ELLE;
import static seedu.address.testutil.TypicalEntries.KEYPHRASE_NOT_MATCHING_ANYWHERE;
import static seedu.address.testutil.TypicalEntries.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.Model;
import seedu.address.testutil.FindEntryDescriptorBuilder;

public class FindCommandSystemTest extends EntryBookSystemTest {

    @Test
    public void find() {
        FindEntryDescriptorBuilder builder = new FindEntryDescriptorBuilder();
        Model expectedModel = getModel();

        /* Case: find title of entry in entry book -> 1 entry found */
        showAllEntries();
        String command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(BENSON.getTitle().fullTitle).build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find title (substring) of entry in entry book -> 1 entry found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle("enson Me").build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find description of entry in entry book -> 1 entry found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withDescription(BENSON.getDescription().value).build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find description (substring) of entry in entry book -> 1 entry found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withDescription("older Bens").build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find link of entry in entry book -> 1 entry found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withLink(BENSON.getLink().value.toString()).build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of entry in entry book -> 1 entry found */
        showAllEntries();
        String[] bensonTags = new ArrayList<>(BENSON.getTags())
            .stream()
            .map(t -> t.tagName)
            .toArray(String[]::new);
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTags(bensonTags).build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags (substring) of entry in entry book -> 0 entries found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTags("wesMon").build());
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: only one field matching for one entry in entry book -> 1 entry found */
        // Only matching tag
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder
                .withTitle(KEYPHRASE_NOT_MATCHING_ANYWHERE)
                .withLink(KEYPHRASE_NOT_MATCHING_ANYWHERE)
                .withDescription(KEYPHRASE_NOT_MATCHING_ANYWHERE)
                .withTags(bensonTags)
                .build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find all of entry in entry book -> 4 entries found, one for each field */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withAll("Carl").build());
        ModelHelper.setFilteredList(expectedModel, ALICE, CARL, DANIEL, ELLE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: same as above, but non-exact matching tag -> 3 entries found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withAll("Car").build());
        ModelHelper.setFilteredList(expectedModel, ALICE, CARL, ELLE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        // Only matching title
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder
                .withTitle(BENSON.getTitle().fullTitle)
                .withLink(KEYPHRASE_NOT_MATCHING_ANYWHERE)
                .withDescription(KEYPHRASE_NOT_MATCHING_ANYWHERE)
                .withTags(KEYPHRASE_NOT_MATCHING_ANYWHERE)
                .build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        // Only matching description
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder
            .withTitle(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withLink(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withDescription(BENSON.getDescription().value)
            .withTags(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        // Only matching link
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder
            .withTitle(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withLink(BENSON.getLink().value.toString())
            .withDescription(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withTags(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .build());
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: no fields matching any entries -> 0 entries found */
        showAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder
            .withTitle(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withLink(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withDescription(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .withTags(KEYPHRASE_NOT_MATCHING_ANYWHERE)
            .build());
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple entries in entry book, command with leading spaces and trailing spaces
         * -> 2 entries found
         */
        command = "   "
            + FindCommand.COMMAND_WORD
            + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(KEYWORD_MATCHING_MEIER).build())
            + "   ";
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where entry list is displaying the entries we are finding
         * -> 2 entries found
         */
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(KEYWORD_MATCHING_MEIER).build());
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(CARL.getTitle().fullTitle).build());
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple entries in entry book, 2 search terms -> 2 entries found */
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder
                .reset()
                .withTitle(CARL.getTitle().fullTitle)
                .withTags(bensonTags)
                .build());
        ModelHelper.setFilteredList(expectedModel, BENSON, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find same entries in entry book after deleting 1 of them -> 1 entry found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getListEntryBook().getEntryList().contains(BENSON));
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(KEYWORD_MATCHING_MEIER).build());
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find entry in entry book, keyword is same as title but of different case -> 1 entry found */
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle("MeIeR").build());
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find entry not in entry book -> 0 entries found */
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle("Mark").build());
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a entry is selected -> selected card deselected */
        showAllEntries();
        selectEntry(Index.fromOneBased(1));
        assertFalse(getEntryListPanel().getHandleToSelectedCard().getLink().equals(DANIEL.getLink().value));
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withLink(DANIEL.getLink().value.toString()).build());
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find entry in empty entry book -> 0 entries found */
        deleteAllEntries();
        command = FindCommand.COMMAND_WORD + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(KEYWORD_MATCHING_MEIER).build());
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd" + " "
            + getFindEntryDescriptorDetails(builder.reset().withTitle(KEYWORD_MATCHING_MEIER).build());
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_ENTRIES_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar excluding count remains unchanged,
     * and the command box has the default style class,
     * and the selected card updated accordingly, depending on {@code cardStatus}.
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_ENTRIES_LISTED_OVERVIEW, expectedModel.getFilteredEntryList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarExcludingCountUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar excluding count remain unchanged,
     * and the command box has the error style.
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
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
