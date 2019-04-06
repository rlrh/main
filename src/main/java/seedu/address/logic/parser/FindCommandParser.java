package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.FindEntryDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.EntryContainsSearchTermsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_TITLE,
                PREFIX_DESCRIPTION,
                PREFIX_LINK,
                PREFIX_ALL,
                PREFIX_TAG);

        FindEntryDescriptor findEntryDescriptor = new FindEntryDescriptor();
        if (argMultimap.getValue(PREFIX_TITLE).isPresent()) {
            findEntryDescriptor.setTitle(ParserUtil.parseKeyphrase(argMultimap.getValue(PREFIX_TITLE).get()));
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            findEntryDescriptor.setDescription(ParserUtil.parseKeyphrase(
                                                            argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        if (argMultimap.getValue(PREFIX_LINK).isPresent()) {
            findEntryDescriptor.setLink(ParserUtil.parseKeyphrase(argMultimap.getValue(PREFIX_LINK).get()));
        }
        if (argMultimap.getValue(PREFIX_ALL).isPresent()) {
            findEntryDescriptor.setAll(ParserUtil.parseKeyphrase(argMultimap.getValue(PREFIX_ALL).get()));
        }
        parseTagsForFind(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(findEntryDescriptor::setTags);

        if (!findEntryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(FindCommand.MESSAGE_NO_SEARCH_TERMS);
        }

        return new FindCommand(new EntryContainsSearchTermsPredicate(findEntryDescriptor));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForFind(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }


}
