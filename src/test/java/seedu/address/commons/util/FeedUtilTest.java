package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.util.FeedUtil.fromFeedUrl;
import static seedu.address.testutil.TypicalEntries.ANIMEREVIEW_FEED_BASE_URL;
import static seedu.address.testutil.TypicalEntries.LOCAL_FEED_BASE_URL;
import static seedu.address.testutil.TypicalEntries.NOT_A_FEED_BASE_URL;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rometools.rome.io.FeedException;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.TypicalEntries;

public class FeedUtilTest {
    private static final Tag TAG_TECH = new Tag("Tech");

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void fromFeedUrl_validLocalUrl_success() throws Exception {
        assertEquals(fromFeedUrl(LOCAL_FEED_BASE_URL), TypicalEntries.getAnimeReviewEntryBook(LOCAL_FEED_BASE_URL));
    }

    @Test
    public void fromFeedUrl_validRemoteUrl_success() throws Exception {
        assertEquals(fromFeedUrl(ANIMEREVIEW_FEED_BASE_URL),
                TypicalEntries.getAnimeReviewEntryBook(ANIMEREVIEW_FEED_BASE_URL));
    }

    @Test
    public void fromFeedUrl_validLocalUrl_allHasTags() throws Exception {
        assertTrue(fromFeedUrl(LOCAL_FEED_BASE_URL, Set.of(TAG_TECH)).getEntryList().stream()
                .allMatch(entry -> entry.getTags().contains(TAG_TECH)));
    }

    @Test
    public void fromFeedUrl_notValidFeed_throwsException() throws Exception {
        thrown.expect(FeedException.class);
        fromFeedUrl(NOT_A_FEED_BASE_URL);
    }
}
