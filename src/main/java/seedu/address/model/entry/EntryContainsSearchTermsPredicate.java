package seedu.address.model.entry;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.FindEntryDescriptor;

/**
 * Tests that a {@code Entry}'s {@code Title} matches any of the keywords given.
 */
public class EntryContainsSearchTermsPredicate implements Predicate<Entry> {
    private final FindCommand.FindEntryDescriptor findEntryDescriptor;

    public EntryContainsSearchTermsPredicate(FindEntryDescriptor findEntryDescriptor) {
        this.findEntryDescriptor = findEntryDescriptor;
    }

    @Override
    public boolean test(Entry entry) {
        boolean result = false;

        if (findEntryDescriptor.getTags().isPresent()) {
            result = findEntryDescriptor.getTags().get()
                .stream()
                .anyMatch((tag) -> entry.getTags().contains(tag));

        }
        if (!result && findEntryDescriptor.getTitle().isPresent()) {
            result = StringUtil.containsPhraseIgnoreCase(
                entry.getTitle().fullTitle,
                findEntryDescriptor.getTitle().get());

        }
        if (!result && findEntryDescriptor.getLink().isPresent()) {
            result = StringUtil.containsPhraseIgnoreCase(
                entry.getLink().value,
                findEntryDescriptor.getLink().get());

        }
        if (!result && findEntryDescriptor.getDescription().isPresent()) {
            result = StringUtil.containsPhraseIgnoreCase(
                entry.getDescription().value,
                findEntryDescriptor.getDescription().get());
        }

        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EntryContainsSearchTermsPredicate // instanceof handles nulls
                && findEntryDescriptor.equals(((EntryContainsSearchTermsPredicate) other).findEntryDescriptor)); //state
    }

}
