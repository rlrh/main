package seedu.address.mocks;

import static seedu.address.testutil.TypicalEntries.getTypicalArchivesEntryBook;
import static seedu.address.testutil.TypicalEntries.getTypicalListEntryBook;

import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

/**
 * A mock to create a ModelManager initialised with typical entries.
 */
public class TypicalModelManagerStub extends ModelManager {

    public TypicalModelManagerStub() {
        super(getTypicalListEntryBook(), getTypicalArchivesEntryBook(),
            new UserPrefs(), new StorageStub());
    }

    public TypicalModelManagerStub(Storage storage) {
        super(getTypicalListEntryBook(), getTypicalArchivesEntryBook(),
            new UserPrefs(), storage);
    }

}
