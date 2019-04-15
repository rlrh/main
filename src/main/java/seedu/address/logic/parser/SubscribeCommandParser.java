package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.SubscribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new SubscribeCommand object
 */
public class SubscribeCommandParser implements Parser<SubscribeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SubscribeCommand
     * and returns an SubscribeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SubscribeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args,
                PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_LINK, PREFIX_TAG);

        if (!argMultimap.getValue(PREFIX_LINK).isPresent()
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SubscribeCommand.MESSAGE_USAGE));
        }

        Title title = ParserUtil.parseTitle(argMultimap.getValue(PREFIX_TITLE));
        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION));
        Link link = ParserUtil.parseLink(argMultimap.getValue(PREFIX_LINK));
        Optional<Set<Tag>> initialTagList = ParserUtil.parseTagsOptional(argMultimap.getAllValues(PREFIX_TAG));

        Set<Tag> tagList = initialTagList
            .or(() -> {
                // If the user did not provide any tags, use link's hostname as tag
                String defaultTagName = link.value.getHost()
                    .replaceAll("[^\\p{Alnum}]", ""); // Strip non-alphanumeric characters
                if (defaultTagName.length() > 0) {
                    return Optional.of(Set.of(new Tag(defaultTagName)));
                } else {
                    return Optional.empty();
                }
            })
            .orElse(Collections.emptySet()); // Sometimes there is no host, so even the default tag doesn't work

        Entry feedEntry = new Entry(title, description, link, tagList);

        return new SubscribeCommand(feedEntry);
    }
}
