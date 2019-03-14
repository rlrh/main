package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX
                + " You entered: [" + oneBasedIndex + "].");
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code Optional<String> title} into a {@code Title}.
     * Leading and trailing whitespaces will be trimmed.
     * If title is empty, default title used.
     *
     * @throws ParseException if the given {@code title}'s value is invalid.
     */
    public static Title parseTitle(Optional<String> title) throws ParseException {
        requireNonNull(title);
        if (title.isPresent()) {
            String trimmedTitle = title.get().trim();
            if (!Title.isValidUserInputTitle(trimmedTitle)) {
                throw new ParseException(Title.formExceptionMessage(trimmedTitle));
            }
            return new Title(trimmedTitle);
        } else {
            return new Title(Title.DEFAULT_TITLE);
        }
    }

    /**
     * Parses a {@code Optional<String> description} into a {@code Description}.
     * Leading and trailing whitespaces will be trimmed.
     * If description is empty, default description used.
     *
     * @throws ParseException if the given {@code description}'s value is invalid.
     */
    public static Description parseDescription(Optional<String> description) throws ParseException {
        requireNonNull(description);
        if (description.isPresent()) {
            String trimmedDescription = description.get().trim();
            if (!Description.isValidUserInputDescription(trimmedDescription)) {
                throw new ParseException(Description.formExceptionMessage(trimmedDescription));
            }
            return new Description(trimmedDescription);
        } else {
            return new Description(Description.DEFAULT_DESCRIPTION);
        }
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Address}.
     * Modified from original as address is now optional (and invisible) field.
     * Leading and trailing whitespaces will be trimmed.
     * If address is empty, default address used.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(Optional<String> address) throws ParseException {
        requireNonNull(address);
        if (address.isPresent()) {
            String trimmedAddress = address.get().trim();
            if (!Address.isValidUserInputAddress(trimmedAddress)) {
                throw new ParseException(Address.formExceptionMessage(trimmedAddress));
            }
            return new Address(trimmedAddress);
        } else {
            return new Address(Address.DEFAULT_ADDRESS);
        }
    }

    /**
     * Parses a {@code String link} into an {@code Link}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code link} is empty or {@code link}'s value is invalid.
     */
    public static Link parseLink(Optional<String> link) throws ParseException {
        requireNonNull(link);
        if (link.isPresent()) {
            String trimmedLink = link.get().trim();
            if (!Link.isValidUserInputLink(trimmedLink)) {
                throw new ParseException(Link.formExceptionMessage(trimmedLink));
            }
            return new Link(trimmedLink);
        } else {
            throw new ParseException(Link.formExceptionMessage());
        }
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.formExceptionMessage(trimmedTag));
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
