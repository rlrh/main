package seedu.address.model;

import seedu.address.logic.parser.EntryBookArchivesParser;
import seedu.address.logic.parser.EntryBookListParser;
import seedu.address.logic.parser.EntryBookParser;

/**
 * Enums for contexts Model can take
 */
public enum ModelContext {
    CONTEXT_LIST(new EntryBookListParser()), CONTEXT_ARCHIVE(new EntryBookArchivesParser());

    private EntryBookParser parser;

    ModelContext(EntryBookParser parser) {
        this.parser = parser;
    }

    public EntryBookParser getParser() {
        return parser;
    }
}
