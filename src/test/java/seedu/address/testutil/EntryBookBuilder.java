package seedu.address.testutil;

import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code EntryBook ab = new EntryBookBuilder().withEntry("John", "Doe").build();}
 */
public class EntryBookBuilder {

    private EntryBook entryBook;

    public EntryBookBuilder() {
        entryBook = new EntryBook();
    }

    public EntryBookBuilder(EntryBook entryBook) {
        this.entryBook = entryBook;
    }

    /**
     * Adds a new {@code Entry} to the {@code EntryBook} that we are building.
     */
    public EntryBookBuilder withEntry(Entry entry) {
        entryBook.addEntry(entry);
        return this;
    }

    public EntryBook build() {
        return entryBook;
    }
}
