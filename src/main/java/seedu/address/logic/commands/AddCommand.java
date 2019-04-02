package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.util.EntryAutofill;
import seedu.address.util.Network;

/**
 * Adds a entry to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a entry to the entry book. "
            + "Parameters: "
            + PREFIX_LINK + "LINK "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_DESCRIPTION + "COMMENT] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_LINK + "https://example.com "
            + PREFIX_TITLE + "Example Title "
            + PREFIX_DESCRIPTION + "Example Description "
            + PREFIX_TAG + "science "
            + PREFIX_TAG + "tech";

    public static final String MESSAGE_SUCCESS = "New entry added: %1$s";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the entry book";

    private static final Logger logger = LogsCenter.getLogger(AddCommand.class);

    private final Entry toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Entry}
     */
    public AddCommand(Entry entry) {
        requireNonNull(entry);
        toAdd = entry;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        String urlString = toAdd.getLink().value;

        EntryAutofill autofill = new EntryAutofill(toAdd.getTitle(), toAdd.getDescription());
        autofill.extractFromUrl(urlString);

        Optional<byte[]> articleContent = Network.fetchArticleOptional(urlString);
        if (articleContent.isPresent()) {
            String html = new String(articleContent.get());
            autofill.extractFromHtml(html);
        }

        Entry updatedEntry = new Entry(
                autofill.getTitle(),
                autofill.getDescription(),
                toAdd.getLink(),
                toAdd.getAddress(),
                toAdd.getTags()
        );

        if (model.hasEntry(updatedEntry)) {
            throw new CommandException(MESSAGE_DUPLICATE_ENTRY);
        }

        model.addListEntry(updatedEntry, articleContent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedEntry));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }

}
