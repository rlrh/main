package seedu.address.model.entry.util;

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
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Title;

/**
 * Attempts to autofill an Entry's missing Title or Description by parsing its URL or HTML.
 */
public class EntryAutofill {

    private static final Logger logger = LogsCenter.getLogger(EntryAutofill.class);
    private static final String FALLBACK_TITLE = "Untitled";
    private static final String FALLBACK_DESCRIPTION = "No description";
    private static final int MAX_WORDS = 32;

    private final Title title;
    private final Description description;
    private final boolean noTitleOrNoDescription;
    private final Candidate<String, Title> titleCandidate;
    private final Candidate<String, Description> descriptionCandidate;

    public EntryAutofill(Title originalTitle, Description originalDescription) {
        this.title = originalTitle;
        this.description = originalDescription;
        this.noTitleOrNoDescription = title.isEmpty() || description.isEmpty();
        this.titleCandidate = new Candidate<>(new Title(FALLBACK_TITLE), string -> {
            try {
                return Optional.of(ParserUtil.parseTitle(Optional.of(string)));
            } catch (ParseException pe) {
                return Optional.empty();
            }
        });
        this.descriptionCandidate = new Candidate<>(new Description(FALLBACK_DESCRIPTION), string -> {
            try {
                return Optional.of(ParserUtil.parseDescription(Optional.of(string)));
            } catch (ParseException pe) {
                return Optional.empty();
            }
        });
    }

    /**
     * Extract candidates by parsing URL.
     * @param url URL to parse
     */
    public EntryAutofill extractFromUrl(URL url) {
        if (noTitleOrNoDescription) {
            String baseName = Files.getNameWithoutExtension(url.getPath())
                    .replaceAll("\n", "") // remove newline chars
                    .replaceAll("\r", "") // remove carriage return chars
                    .replaceAll("[^a-zA-Z0-9]+", " ") // replace special chars with spaces
                    .trim();
            titleCandidate.tryout(WordUtils.capitalizeFully(baseName)); // title - cleaned up base name
            descriptionCandidate.tryout(url.getHost()); // description - host name
        }
        return this;
    }

    /**
     * Extract candidates by parsing HTML.
     * @param html raw HTML to parse
     */
    public EntryAutofill extractFromHtml(String html) {
        if (noTitleOrNoDescription) {

            // Process through Jsoup
            Document document = Jsoup.parse(html);
            titleCandidate // title 2nd choice - document title element
                    .tryout(document.title().trim());
            descriptionCandidate // desc 3rd choice - first N words of raw document body text
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                        StringUtil.utfSafeOf(document.body().text().trim()), MAX_WORDS));


            // Process through Readability4J
            Readability4J readability4J = new Readability4J("", document);
            Article article = readability4J.parse();
            titleCandidate // title 1st choice - extract title
                    .tryout(StringUtil.utfSafeOf(StringUtil.nullSafeOf(article.getTitle())).trim());
            descriptionCandidate
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                            StringUtil.utfSafeOf(StringUtil.nullSafeOf(article.getTextContent())).trim(), MAX_WORDS
                    )) // desc 2nd choice - first N words of cleaned-up document body text
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                            StringUtil.utfSafeOf(StringUtil.nullSafeOf(article.getExcerpt())).trim(), MAX_WORDS
                    )); // desc 1st choice - extract description

        }
        return this;
    }

    /**
     * Gets the best title.
     * @return autofilled title if original title is empty, else original title
     */
    public Title getTitle() {
        return title.isEmpty() ? titleCandidate.get() : title;
    }

    /**
     * Gets the best description.
     * @return autofilled description if original description is empty, else original description
     */
    public Description getDescription() {
        return description.isEmpty() ? descriptionCandidate.get() : description;
    }

}
