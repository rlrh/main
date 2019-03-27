package seedu.address.model;

import seedu.address.logic.parser.EntryBookArchivesParser;
import seedu.address.logic.parser.EntryBookListParser;
import seedu.address.logic.parser.EntryBookParser;
import seedu.address.logic.parser.EntryBookSearchParser;

/**
 * Enums for contexts Model can take
 */
public enum ModelContext {
    CONTEXT_LIST(new EntryBookListParser()),
    CONTEXT_ARCHIVES(new EntryBookArchivesParser()),
    CONTEXT_SEARCH(new EntryBookSearchParser());

    private EntryBookParser parser;

    ModelContext(EntryBookParser parser) {
        this.parser = parser;
    }

    public EntryBookParser getParser() {
        return parser;
    }
}
