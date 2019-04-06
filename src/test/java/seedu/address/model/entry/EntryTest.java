package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.testutil.EntryBuilder;

public class EntryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Entry entry = new EntryBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        entry.getTags().remove(0);
    }

    @Test
    public void isSameEntry() {
        // same object -> returns true
        assertTrue(ALICE.isSameEntry(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameEntry(null));

        // different link -> returns false
        Entry editedAlice = new EntryBuilder(ALICE).withLink(VALID_LINK_BOB).build();
        assertFalse(ALICE.isSameEntry(editedAlice));

        // same link, different description -> returns true
        editedAlice = new EntryBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).build();
        assertTrue(ALICE.isSameEntry(editedAlice));

        // same link, different title -> returns true
        editedAlice = new EntryBuilder(ALICE).withTitle(VALID_TITLE_BOB).build();
        assertTrue(ALICE.isSameEntry(editedAlice));

        // same link, different description and title -> returns true
        editedAlice = new EntryBuilder(ALICE).withTitle(VALID_TITLE_BOB).withDescription(VALID_DESCRIPTION_BOB).build();
        assertTrue(ALICE.isSameEntry(editedAlice));

        // same link, different tags -> returns true
        editedAlice = new EntryBuilder(ALICE).withTags(VALID_TAG_SCIENCE).build();
        assertTrue(ALICE.isSameEntry(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Entry aliceCopy = new EntryBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different entry -> returns false
        assertFalse(ALICE.equals(BOB));

        // different title -> returns false
        Entry editedAlice = new EntryBuilder(ALICE).withTitle(VALID_TITLE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different description -> returns false
        editedAlice = new EntryBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different link -> returns false
        editedAlice = new EntryBuilder(ALICE).withLink(VALID_LINK_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new EntryBuilder(ALICE).withTags(VALID_TAG_SCIENCE).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
