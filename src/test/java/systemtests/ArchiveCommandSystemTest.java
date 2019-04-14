package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.ArchiveCommand.MESSAGE_ARCHIVE_ENTRY_SUCCESS;
import static seedu.address.logic.commands.UnarchiveCommand.MESSAGE_UNARCHIVE_ENTRY_SUCCESS;
import static seedu.address.testutil.TestUtil.getEntry;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TypicalEntries.KEYWORD_MATCHING_TOKI;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import java.util.Optional;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.UnarchiveCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Entry;

/**
 * This includes tests for {@code UnarchiveCommand}, seeing as they are closely related.
 * This does not test the behaviour of html resources of entries being deleted (when archived)
 * or restored (when unarchived).
 */
public class ArchiveCommandSystemTest extends EntryBookSystemTest {

    private static final String MESSAGE_INVALID_ARCHIVE_COMMAND_FORMAT =
        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ArchiveCommand.MESSAGE_USAGE);

    private static final String MESSAGE_INVALID_UNARCHIVE_COMMAND_FORMAT =
        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, UnarchiveCommand.MESSAGE_USAGE);

    @Test
    public void archiveAndUnarchive() {
        /* ----------------- Performing archive operation while an unfiltered list is being shown ------------------ */

        /* Case: archive the first entry in the list, command with leading spaces and trailing spaces -> archived */
        Model expectedModel = getModel();
        String command = "     " + ArchiveCommand.COMMAND_WORD + "      " + INDEX_FIRST_ENTRY.getOneBased() + "       ";
        Entry archivedEntry = archiveEntry(expectedModel, INDEX_FIRST_ENTRY);
        String expectedResultMessage = String.format(MESSAGE_ARCHIVE_ENTRY_SUCCESS, archivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: archive the last entry in the list -> archived */
        Index lastEntryIndex = getLastIndex(getModel());
        assertArchiveCommandSuccess(lastEntryIndex);

        /* Case: archive the middle entry in the list -> archived */
        Index middleEntryIndex = getMidIndex(getModel());
        assertArchiveCommandSuccess(middleEntryIndex);

        /* ------------------ Performing archive operation while a filtered list is being shown -------------------- */

        /* Case: filtered entry list, archive index within bounds of entry book and entry list -> archived */
        showEntriesWithTitle(KEYWORD_MATCHING_TOKI);
        Index index = INDEX_FIRST_ENTRY;
        assertTrue(index.getZeroBased() < getModel().getFilteredEntryList().size());
        assertArchiveCommandSuccess(index);

        /* Case: archive the last entry in the filtered list -> archived */
        lastEntryIndex = getLastIndex(getModel());
        assertArchiveCommandSuccess(lastEntryIndex);

        /* Case: archive the middle entry in the filtered list -> archived */
        middleEntryIndex = getMidIndex(getModel());
        assertArchiveCommandSuccess(middleEntryIndex);

        /* Case: filtered entry list, archive index within bounds of entry book but out of bounds of entry list
         * -> rejected
         */
        showEntriesWithTitle(KEYWORD_MATCHING_TOKI);
        int invalidIndex = getModel().getListEntryBook().getEntryList().size();
        command = ArchiveCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

        /* Archive the rest of the "Toki" entries for future tests */
        index = INDEX_FIRST_ENTRY;
        assertArchiveCommandSuccess(index);
        assertArchiveCommandSuccess(index);

        /* --------------------- Performing archive operation while a entry card is selected ------------------------ */

        /* Case: archive the first selected entry -> nothing selected */
        showAllListEntries();
        expectedModel = getModel();
        Index selectedIndex = INDEX_FIRST_ENTRY;
        selectEntry(selectedIndex);
        command = ArchiveCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        archivedEntry = archiveEntry(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_ARCHIVE_ENTRY_SUCCESS, archivedEntry);
        assertCommandSuccessDeselected(command, expectedModel, expectedResultMessage);

        /* Case: archive the last selected entry -> entry list panel selects the entry before the archived entry */
        showAllListEntries();
        expectedModel = getModel();
        selectedIndex = getMidIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectEntry(selectedIndex);
        command = ArchiveCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        archivedEntry = archiveEntry(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_ARCHIVE_ENTRY_SUCCESS, archivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* Case: archive the last selected entry -> entry list panel selects the entry before the archived entry */
        showAllListEntries();
        expectedModel = getModel();
        selectedIndex = getLastIndex(expectedModel);
        expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectEntry(selectedIndex);
        command = ArchiveCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        archivedEntry = archiveEntry(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_ARCHIVE_ENTRY_SUCCESS, archivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid archive operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = ArchiveCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_ARCHIVE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = ArchiveCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_ARCHIVE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
            getModel().getListEntryBook().getEntryList().size() + 1);
        command = ArchiveCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(ArchiveCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_ARCHIVE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(ArchiveCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_ARCHIVE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ArChivE 1", String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));

        /* ----------------- Show archives, and change the context of the model ------------------------------------- */
        executeCommand(ArchivesCommand.COMMAND_WORD);
        assertTrue(getModel().getContext().equals(ModelContext.CONTEXT_ARCHIVES));

        /* ----------------- Performing unarchive operation while an unfiltered list is being shown ---------------- */
        showAllArchivesEntries();

        /* Case: unarchive the first entry in the list, command with leading spaces and trailing spaces -> unarchived */
        expectedModel = getModel();
        command = "     " + UnarchiveCommand.COMMAND_WORD + "      " + INDEX_FIRST_ENTRY.getOneBased() + "       ";
        Entry unarchivedEntry = unarchiveEntry(expectedModel, INDEX_FIRST_ENTRY);
        expectedResultMessage = String.format(MESSAGE_UNARCHIVE_ENTRY_SUCCESS, unarchivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: unarchive the last entry in the list -> unarchived */
        lastEntryIndex = getLastIndex(getModel());
        assertUnarchiveCommandSuccess(lastEntryIndex);

        /* Case: unarchive the middle entry in the list -> unarchived */
        middleEntryIndex = getMidIndex(getModel());
        assertUnarchiveCommandSuccess(middleEntryIndex);

        /* ------------------ Performing unarchive operation while a filtered list is being shown ------------------ */

        /* Case: filtered entry list, unarchive index within bounds of entry book and entry list -> unarchived */
        showEntriesWithTitle(KEYWORD_MATCHING_TOKI);
        index = INDEX_FIRST_ENTRY;
        assertTrue(index.getZeroBased() < getModel().getFilteredEntryList().size());
        assertUnarchiveCommandSuccess(index);

        /* Case: unarchive the last entry in the filtered list -> unarchived */
        lastEntryIndex = getLastIndex(getModel());
        assertUnarchiveCommandSuccess(lastEntryIndex);

        /* Case: unarchive the middle entry in the filtered list -> unarchived */
        middleEntryIndex = getMidIndex(getModel());
        assertUnarchiveCommandSuccess(middleEntryIndex);

        /* Case: filtered entry list, unarchive index within bounds of entry book but out of bounds of entry list
         * -> rejected
         */
        showEntriesWithTitle(KEYWORD_MATCHING_TOKI);
        invalidIndex = getModel().getArchivesEntryBook().getEntryList().size();
        command = UnarchiveCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

        /* --------------------- Performing unarchive operation while a entry card is selected ---------------------- */

        /* Case: unarchive the first selected entry -> nothing selected */
        showAllArchivesEntries();
        expectedModel = getModel();
        selectedIndex = INDEX_FIRST_ENTRY;
        selectEntry(selectedIndex);
        command = UnarchiveCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        unarchivedEntry = unarchiveEntry(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_UNARCHIVE_ENTRY_SUCCESS, unarchivedEntry);
        assertCommandSuccessDeselected(command, expectedModel, expectedResultMessage);

        /* Case: unarchive the middle selected entry -> entry list panel selects the entry before this entry */
        showAllArchivesEntries();
        expectedModel = getModel();
        selectedIndex = getMidIndex(expectedModel);
        expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectEntry(selectedIndex);
        command = UnarchiveCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        unarchivedEntry = unarchiveEntry(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_UNARCHIVE_ENTRY_SUCCESS, unarchivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* Case: unarchive the last selected entry -> entry list panel selects the entry before the unarchived entry */
        showAllArchivesEntries();
        expectedModel = getModel();
        selectedIndex = getLastIndex(expectedModel);
        expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectEntry(selectedIndex);
        command = UnarchiveCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        unarchivedEntry = unarchiveEntry(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_UNARCHIVE_ENTRY_SUCCESS, unarchivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid archive operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = UnarchiveCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_UNARCHIVE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = UnarchiveCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_UNARCHIVE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        outOfBoundsIndex = Index.fromOneBased(
            getModel().getArchivesEntryBook().getEntryList().size() + 1);
        command = UnarchiveCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(UnarchiveCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_UNARCHIVE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(UnarchiveCommand.COMMAND_WORD
            + " 1 abc", MESSAGE_INVALID_UNARCHIVE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("uNArChivE 1", String.format(MESSAGE_UNKNOWN_COMMAND,
            ModelContext.CONTEXT_ARCHIVES));
    }

    /**
     * Unarchives the {@code Entry} at the specified {@code index} in {@code model}'s entry book.
     * @return the removed entry
     */
    private Entry archiveEntry(Model model, Index index) {
        Entry targetEntry = getEntry(model, index);
        model.archiveEntry(targetEntry);
        return targetEntry;
    }

    /**
     * Archives the {@code Entry} at the specified {@code index} in {@code model}'s entry book.
     * @return the removed entry
     */
    private Entry unarchiveEntry(Model model, Index index) {
        Entry targetEntry = getEntry(model, index);
        model.unarchiveEntry(targetEntry, Optional.empty());
        return targetEntry;
    }

    /**
     * Archives the entry at {@code toArchive} by creating a default {@code ArchiveCommand} using {@code toArchive} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see ArchiveCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertArchiveCommandSuccess(Index toArchive) {
        Model expectedModel = getModel();
        Entry archivedEntry = archiveEntry(expectedModel, toArchive);
        String expectedResultMessage = String.format(MESSAGE_ARCHIVE_ENTRY_SUCCESS, archivedEntry);

        assertCommandSuccess(
            ArchiveCommand.COMMAND_WORD + " " + toArchive.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Unarchives the entry at {@code toUnarchive} by creating a default {@code UnarchiveCommand} using
     * {@code toUnarchive} and performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see ArchiveCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertUnarchiveCommandSuccess(Index toUnarchive) {
        Model expectedModel = getModel();
        Entry unarchivedEntry = unarchiveEntry(expectedModel, toUnarchive);
        String expectedResultMessage = String.format(MESSAGE_UNARCHIVE_ENTRY_SUCCESS, unarchivedEntry);

        assertCommandSuccess(
            UnarchiveCommand.COMMAND_WORD + " "
                + toUnarchive.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. {@code Storage} and {@code EntryListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1, 2 and 3 are performed by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see ArchiveCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see EntryBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatusExcludingCount();
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that browser url
     * should be set to the default page and no entries are selected.
     * @see ArchiveCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see EntryBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccessDeselected(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardDeselected();

        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatusExcludingCount();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. {@code Storage} and {@code EntryListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * 4. Asserts that the browser url, selected card and status bar excluding count remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1, 2 and 3 are performed by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
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
