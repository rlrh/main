package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_SUCCESS;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;

public class FeedCommandTest {
    private Model model = new ModelManagerStub();
    private Model expectedModel = new ModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    private final static String TEST_URL = "https://m4th.b0ss.net/temp/rss.xml";
    private final static List<Entry> TEST_ENTRY_LIST = List.of(
            makeEntryFromRssTriple("Anime: Mahoujin Guru Guru",
                    "https://blog.GNU.moe/anime/review/mahoujin-guru-guru.html",
                    "Anime review 1"),
            makeEntryFromRssTriple("Anime: Gamers!",
                    "https://blog.GNU.moe/anime/review/gamers.html",
                    "Anime review 2"),
            makeEntryFromRssTriple("Anime: Made in Abyss",
                    "https://blog.GNU.moe/anime/review/made-in-abyss.html",
                    "Anime review n"),
            makeEntryFromRssTriple("Anime: Mob Psycho 100",
                    "https://blog.GNU.moe/anime/review/mob-psycho.html",
                    "Anime review"),
            makeEntryFromRssTriple("Anime: New Game!!",
                    "https://blog.GNU.moe/anime/review/new-game-2.html",
                    "Anime revieww"),
            makeEntryFromRssTriple("Anime: Saiki Kusuo no Psi-nan",
                    "https://blog.GNU.moe/anime/review/saiki-kusuo.html",
                    "sigh"),
            makeEntryFromRssTriple("Anime: Durarara!!",
                    "https://blog.GNU.moe/anime/review/durarara.html",
                    "bzzt"),
            makeEntryFromRssTriple("Anime: Battle Programmer Shirase",
                    "https://blog.GNU.moe/anime/review/bps.html",
                    "lol"),
            makeEntryFromRssTriple("Anime: Re:Zero",
                    "https://blog.GNU.moe/anime/review/re_zero.html",
                    "idk"),
            makeEntryFromRssTriple("Anime: Youjo Senki",
                    "https://blog.GNU.moe/anime/review/youjo_senki.html",
                    "I like this reviewer")
            );

    private static Entry makeEntryFromRssTriple(String title, String link, String comment) {
        return new Entry(
                new Title(title),
                new Comment(comment),
                new Link(link),
                new Address("unused"), // this dummy matches that in FeedCommand
                Collections.emptySet()
        );
    }

    @Test
    public void equals() {
        String firstUrl = "https://open.kattis.com/rss/new-problems";
        String secondUrl = "https://en.wikipedia.org/w/index.php?title=Special:RecentChanges&feed=rss";


        FeedCommand feedFirstCommand = new FeedCommand(firstUrl);
        FeedCommand feedSecondCommand = new FeedCommand(secondUrl);

        // same object -> returns true
        assertTrue(feedFirstCommand.equals(feedFirstCommand));

        // same values -> returns true
        FeedCommand feedFirstCommandCopy = new FeedCommand(firstUrl);
        assertTrue(feedFirstCommand.equals(feedFirstCommandCopy));

        // different types -> returns false
        assertFalse(feedFirstCommand.equals(1));

        // null -> returns false
        assertFalse(feedFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(feedFirstCommand.equals(feedSecondCommand));
    }

    @Test
    public void execute_UrlGiven_updatesEntryBook() {
        String expectedMessage = String.format(MESSAGE_SUCCESS, TEST_URL);
        FeedCommand command = new FeedCommand(TEST_URL);

        EntryBook expectedEntryBook = new EntryBook();
        expectedEntryBook.setPersons(TEST_ENTRY_LIST);
        expectedModel.setAddressBook(expectedEntryBook);
        expectedModel.commitAddressBook();

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }
}
