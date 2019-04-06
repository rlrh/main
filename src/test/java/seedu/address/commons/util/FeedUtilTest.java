package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.util.FeedUtil.DEFAULT_ADDRESS_TEXT;
import static seedu.address.commons.util.FeedUtil.DEFAULT_DESCRIPTION_TEXT;
import static seedu.address.commons.util.FeedUtil.fromFeedUrl;

import java.net.URL;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rometools.rome.io.FeedException;

import seedu.address.MainApp;
import seedu.address.model.EntryBook;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.testutil.EntryBookBuilder;
import seedu.address.testutil.TestUtil;

public class FeedUtilTest {
    private static final URL TEST_URL =
        TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/rss.xml");
    private static final URL TEST_URL_LOCAL = MainApp.class.getResource("/RssFeedTest/rss.xml");
    private static final URL NOTAFEED_URL =
        TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/notafeed.notxml");

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Generates a list of entries. It has to take in the url because we need it for the default description.
     */
    private static EntryBook getTestEntryBook(URL url) {
        return new EntryBookBuilder()
                .withEntry(makeEntryFromRssTriple("Anime: Mahoujin Guru Guru",
                        "https://blog.GNU.moe/anime/review/mahoujin-guru-guru.html",
                        "Anime review 1"))
                .withEntry(makeEntryFromRssTriple("Anime: Gamers!",
                        "https://blog.GNU.moe/anime/review/gamers.html",
                        "Anime review 2"))
                .withEntry(makeEntryFromRssTriple("Anime: Made in Abyss",
                        "https://blog.GNU.moe/anime/review/made-in-abyss.html",
                        "Anime review n"))
                .withEntry(makeEntryFromRssTriple("Anime: Mob Psycho 100",
                        "https://blog.GNU.moe/anime/review/mob-psycho.html",
                        "Anime review"))
                .withEntry(makeEntryFromRssTriple("Anime: New Game!!",
                        "https://blog.GNU.moe/anime/review/new-game-2.html",
                        "Anime revieww"))
                .withEntry(makeEntryFromRssTriple("Anime: Saiki Kusuo no Psi-nan",
                        "https://blog.GNU.moe/anime/review/saiki-kusuo.html",
                        "sigh"))
                .withEntry(makeEntryFromRssTriple("Anime: Durarara!!",
                        "https://blog.GNU.moe/anime/review/durarara.html",
                        String.format(DEFAULT_DESCRIPTION_TEXT, url)))
                .withEntry(makeEntryFromRssTriple("Anime: Battle Programmer Shirase",
                        "https://blog.GNU.moe/anime/review/bps.html",
                        "lol"))
                .withEntry(makeEntryFromRssTriple("Anime: Re:Zero",
                        "https://blog.GNU.moe/anime/review/re_zero.html",
                        "idk"))
                .withEntry(makeEntryFromRssTriple("Anime: Youjo Senki",
                        "https://blog.GNU.moe/anime/review/youjo_senki.html",
                        "I like this reviewer"))
                .build();
    }


    /** Makes an EntryBook entry from the 3 fields that we are harvesting from RSS. */
    private static Entry makeEntryFromRssTriple(String title, String link, String description) {
        return new Entry(
                new Title(title),
                new Description(description),
                new Link(TestUtil.toUrl(link)),
                new Address(DEFAULT_ADDRESS_TEXT),
                Collections.emptySet()
        );
    }

    @Test
    public void fromFeedUrl_validLocalUrl_success() throws Exception {
        assertEquals(fromFeedUrl(TEST_URL_LOCAL), getTestEntryBook(TEST_URL_LOCAL));
    }

    @Test
    public void fromFeedUrl_validRemoteUrl_success() throws Exception {
        assertEquals(fromFeedUrl(TEST_URL), getTestEntryBook(TEST_URL));
    }

    @Test
    public void fromFeedUrl_notValidFeed_throwsException() throws Exception {
        thrown.expect(FeedException.class);
        fromFeedUrl(NOTAFEED_URL);
    }
}
