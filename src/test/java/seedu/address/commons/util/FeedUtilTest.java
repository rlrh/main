package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.util.FeedUtil.DEFAULT_DESCRIPTION_TEXT;
import static seedu.address.commons.util.FeedUtil.fromFeedUrl;

import java.net.URL;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rometools.rome.io.FeedException;

import seedu.address.MainApp;
import seedu.address.model.EntryBook;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBookBuilder;
import seedu.address.testutil.EntryBuilder;
import seedu.address.testutil.TestUtil;

public class FeedUtilTest {
    private static final URL TEST_URL =
        TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/rss.xml");
    private static final URL TEST_URL_LOCAL = MainApp.class.getResource("/RssFeedTest/rss.xml");
    private static final URL NOTAFEED_URL =
        TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/notafeed.notxml");

    private static final Tag TAG_TECH = new Tag("Tech");

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Generates a list of entries. It has to take in the url because we need it for the default description.
     */
    private static EntryBook getAnimeReviewEntryBook(URL url) {
        return new EntryBookBuilder()
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Mahoujin Guru Guru")
                        .withLink("https://blog.GNU.moe/anime/review/mahoujin-guru-guru.html")
                        .withDescription("Anime review 1").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Gamers!")
                        .withLink("https://blog.GNU.moe/anime/review/gamers.html")
                        .withDescription("Anime review 2").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Made in Abyss")
                        .withLink("https://blog.GNU.moe/anime/review/made-in-abyss.html")
                        .withDescription("Anime review n").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Mob Psycho 100")
                        .withLink("https://blog.GNU.moe/anime/review/mob-psycho.html")
                        .withDescription("Anime review").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: New Game!!")
                        .withLink("https://blog.GNU.moe/anime/review/new-game-2.html")
                        .withDescription("Anime revieww").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Saiki Kusuo no Psi-nan")
                        .withLink("https://blog.GNU.moe/anime/review/saiki-kusuo.html")
                        .withDescription("sigh").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Durarara!!")
                        .withLink("https://blog.GNU.moe/anime/review/durarara.html")
                        .withDescription(String.format(DEFAULT_DESCRIPTION_TEXT, url)).build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Battle Programmer Shirase")
                        .withLink("https://blog.GNU.moe/anime/review/bps.html")
                        .withDescription("lol").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Re:Zero")
                        .withLink("https://blog.GNU.moe/anime/review/re_zero.html")
                        .withDescription("idk").build())
                .withEntry(new EntryBuilder()
                        .withTitle("Anime: Youjo Senki")
                        .withLink("https://blog.GNU.moe/anime/review/youjo_senki.html")
                        .withDescription("I like this reviewer").build())
                .build();
    }

    @Test
    public void fromFeedUrl_validLocalUrl_success() throws Exception {
        assertEquals(fromFeedUrl(TEST_URL_LOCAL), getAnimeReviewEntryBook(TEST_URL_LOCAL));
    }

    @Test
    public void fromFeedUrl_validRemoteUrl_success() throws Exception {
        assertEquals(fromFeedUrl(TEST_URL), getAnimeReviewEntryBook(TEST_URL));
    }

    @Test
    public void fromFeedUrl_validLocalUrl_allHasTags() throws Exception {
        assertTrue(fromFeedUrl(TEST_URL_LOCAL, Set.of(TAG_TECH)).getEntryList().stream()
                .allMatch(entry -> entry.getTags().contains(TAG_TECH)));
    }

    @Test
    public void fromFeedUrl_notValidFeed_throwsException() throws Exception {
        thrown.expect(FeedException.class);
        fromFeedUrl(NOTAFEED_URL);
    }
}
