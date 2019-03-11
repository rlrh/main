package seedu.address.mocks;

import static seedu.address.testutil.TypicalEntries.getTypicalEntryBook;

import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * A mock to create a ModelManager initialised with typical entries.
 */
public class TypicalModelManagerStub extends ModelManager {

    public TypicalModelManagerStub() {
        super(getTypicalEntryBook(), new UserPrefs(), new StorageStub());
    }

}
