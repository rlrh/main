package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;
import com.google.common.io.Files;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.util.Candidate;
import seedu.address.util.AbsoluteUrlDocumentConverter;
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
        Candidate<Title> candidateTitle = new Candidate<>(new Title("Untitled"), (String s) -> {
            try {
                return Optional.of(ParserUtil.parseTitle(Optional.of(s)));
            } catch (ParseException pe) {
                return Optional.empty();
            }
        });
        Candidate<Description> candidateDescription = new Candidate<>(new Description("No description"), (String s) -> {
            try {
                return Optional.of(ParserUtil.parseDescription(Optional.of(s)));
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
                candidateDescription.tryout(url.getHost().trim()); // desc 4th choice - host name
            } catch (MalformedURLException mue) {
                // Skip if URL is malformed
                logger.warning("Malformed URL: " + urlString);
            }
        }

        Optional<Link> offlineLink = Optional.empty();

        try {

            // Download article content to local storage
            byte[] articleContent = Network.fetchAsBytes(urlString);
            // Convert all links in article to absolute links
            byte[] absoluteLinkedArticleContent = AbsoluteUrlDocumentConverter.convert(
                    new URL(urlString),
                    articleContent);
            Optional<Path> articlePath = model.addArticle(urlString, absoluteLinkedArticleContent);
            if (articlePath.isPresent()) {
                offlineLink = Optional.of(new Link(articlePath.get().toUri().toASCIIString()));
            }

            if (noTitleOrNoDescription) {

                // Second try - extract candidates by parsing through Jsoup
                String html = new String(articleContent);
                Document document = Jsoup.parse(html);
                candidateTitle.tryout(document.title().trim()); // title 2nd choice - document title element
                candidateDescription.tryout(StringUtil.getFirstNWordsWithEllipsis(document.body().text(), 24)
                        .trim()); // desc 3rd choice - first N words of raw document body text

                // Third try - extract candidates by processing through Crux
                Article article = ArticleExtractor.with(urlString, document)
                        .extractMetadata()
                        .extractContent()
                        .article();
                candidateTitle.tryout(article.title.trim()); // title 1st choice - extract title
                candidateDescription
                        .tryout(StringUtil.getFirstNWordsWithEllipsis(article.document.text(), 24)
                                .trim()) // desc 2nd choice - first N words of cleaned-up document body text
                        .tryout(article.description.trim()); // desc 1st choice - extract description

            }

        } catch (IOException ioe) {
            // Do nothing if fail to fetch the page
            logger.warning("Failed to fetch URL: " + urlString);
        }

        // Attempt to add updated entry to entry book
        Entry updatedEntry = new Entry(
                title.isEmpty() ? candidateTitle.get() : title, // replace title if empty
                description.isEmpty() ? candidateDescription.get() : description, // replace description if empty
                toAdd.getLink(),
                offlineLink,
                toAdd.getAddress(),
                toAdd.getTags()
        );

        if (model.hasEntry(updatedEntry)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addListEntry(updatedEntry);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedEntry));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
