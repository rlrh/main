package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.io.Files;

import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.Candidate;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;
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
    public static final String MESSAGE_DUPLICATE_PERSON = "This entry already exists in the entry book";

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

        // Initial data
        final Title title = toAdd.getTitle();
        final Description description = toAdd.getDescription();
        final boolean noTitleOrNoDescription = title.isEmpty() || description.isEmpty();
        final String urlString = toAdd.getLink().value;

        // Candidates to replace empty title and description
        Candidate<String, Title> candidateTitle = new Candidate<>(new Title("Untitled"), string -> {
            try {
                return Optional.of(ParserUtil.parseTitle(Optional.of(string)));
            } catch (ParseException pe) {
                return Optional.empty();
            }
        });
        Candidate<String, Description> candidateDescription =
                new Candidate<>(new Description("No description"), string -> {
                    try {
                        return Optional.of(ParserUtil.parseDescription(Optional.of(string)));
                    } catch (ParseException pe) {
                        return Optional.empty();
                    }
                });

        // First try - extract candidates just from URL
        if (noTitleOrNoDescription) {
            try {
                URL url = new URL(urlString);
                String baseName = Files.getNameWithoutExtension(url.getPath())
                        .replaceAll("\n", "") // remove newline chars
                        .replaceAll("\r", "") // remove carriage return chars
                        .replaceAll("[^a-zA-Z0-9]+", " ") // replace special chars with spaces
                        .trim();
                candidateTitle.tryout(WordUtils.capitalizeFully(baseName)); // title 3rd choice - cleaned up base name
                candidateDescription.tryout(url.getHost()); // desc 4th choice - host name
            } catch (MalformedURLException mue) {
                // Skip if URL is malformed
                logger.warning("Malformed URL: " + urlString);
            }
        }

        Optional<byte[]> articleContent = Network.fetchArticleOptional(urlString);

        if (noTitleOrNoDescription && articleContent.isPresent()) {

            // Second try - extract candidates by parsing through Jsoup
            String html = new String(articleContent.get());
            Document document = Jsoup.parse(html);
            candidateTitle.tryout(document.title().trim()); // title 2nd choice - document title element
            candidateDescription.tryout(StringUtil.getFirstNWordsWithEllipsis(document.body().text(), 24)
                    .trim()); // desc 3rd choice - first N words of raw document body text

            // Third try - extract candidates by processing through Readability4J
            Readability4J readability4J = new Readability4J(urlString, document);
            Article article = readability4J.parse();
            candidateTitle.tryout(StringUtil.nullSafeOf(article.getTitle())); // title 1st choice - extract title
            candidateDescription
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                            StringUtil.nullSafeOf(article.getTextContent()), 24)
                            .trim()) // desc 2nd choice - first N words of cleaned-up document body text
                    .tryout(StringUtil.nullSafeOf(article.getExcerpt())); // desc 1st choice - extract description

        }

        // Attempt to add updated entry to entry book
        Entry updatedEntry = new Entry(
                title.isEmpty() ? candidateTitle.get() : title, // replace title if empty
                description.isEmpty() ? candidateDescription.get() : description, // replace description if empty
                toAdd.getLink(),
                Optional.empty(),
                toAdd.getAddress(),
                toAdd.getTags()
        );

        if (model.hasEntry(updatedEntry)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
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
