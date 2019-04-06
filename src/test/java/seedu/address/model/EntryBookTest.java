package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BOB;
import static seedu.address.testutil.TypicalEntries.getTypicalListEntryBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.testutil.EntryBuilder;

public class EntryBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EntryBook entryBook = new EntryBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), entryBook.getEntryList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        entryBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyEntryBook_replacesData() {
        EntryBook newData = getTypicalListEntryBook();
        entryBook.resetData(newData);
        assertEquals(newData, entryBook);
    }

    @Test
    public void resetData_withDuplicateEntries_throwsDuplicateEntryException() {
        // Two entries with the same identity fields
        Entry editedBob = new EntryBuilder(BOB).withLink(ALICE.getLink().value).withTags(VALID_TAG_SCIENCE)
                .build();
        List<Entry> newEntries = Arrays.asList(ALICE, editedBob);
        EntryBookStub newData = new EntryBookStub(newEntries);

        thrown.expect(DuplicateEntryException.class);
        entryBook.resetData(newData);
    }

    @Test
    public void hasEntry_nullEntry_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        entryBook.hasEntry(null);
    }

    @Test
    public void hasEntry_entryNotInEntryBook_returnsFalse() {
        assertFalse(entryBook.hasEntry(ALICE));
    }

    @Test
    public void hasEntry_entryInEntryBook_returnsTrue() {
        entryBook.addEntry(ALICE);
        assertTrue(entryBook.hasEntry(ALICE));
    }

    @Test
    public void hasEntry_entryWithSameIdentityFieldsInEntryBook_returnsTrue() {
        entryBook.addEntry(ALICE);
        Entry editedBob = new EntryBuilder(BOB).withLink(ALICE.getLink().value).withTags(VALID_TAG_SCIENCE)
                .build();
        assertTrue(entryBook.hasEntry(editedBob));
    }

    @Test
    public void getEntryList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        entryBook.getEntryList().remove(0);
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        entryBook.addListener(listener);
        entryBook.addEntry(ALICE);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        entryBook.addListener(listener);
        entryBook.removeListener(listener);
        entryBook.addEntry(ALICE);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyEntryBook whose entries list can violate interface constraints.
     */
    private static class EntryBookStub implements ReadOnlyEntryBook {
        private final ObservableList<Entry> entries = FXCollections.observableArrayList();

        EntryBookStub(Collection<Entry> entries) {
            this.entries.setAll(entries);
        }

        @Override
        public ObservableList<Entry> getEntryList() {
            return entries;
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

}
