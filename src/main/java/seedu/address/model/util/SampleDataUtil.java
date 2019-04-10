package seedu.address.model.util;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code EntryBook} with sample data.
 */
public class SampleDataUtil {
    public static Entry[] getSampleEntries() {
        try {
            return new Entry[]{
                new Entry(
                    new Title("CS2103 the best mod ever?"),
                    new Description("CS2101 disagrees!"),
                    new Link("https://www.comp.nus.edu.sg/programmes/ug/cs/"),
                    getTagSet("nus", "soc")),
                new Entry(
                    new Title("Filler - "),
                    new Description("or is it...?"),
                    new Link("file:///example/folder/file"),
                    getTagSet()),
                new Entry(
                    new Title("Qiji trivalises all engineering majors!"),
                    new Description("Has he taken it too far?"),
                    new Link("https://github.com/qjqqyy"),
                    getTagSet("engineering")),
                new Entry(
                    new Title("Jonathan voted best in procrastination!"),
                    new Description("The 22-time winner says \"I don't want to win anymore...\"!"),
                    new Link("https://github.com/epicfailname"),
                    getTagSet("award")),
                new Entry(
                    new Title("Thomas invents time travel!"),
                    new Description("Still can't finish his work.."),
                    new Link("https://github.com/thomastanck"),
                    getTagSet("science", "tech")),
                new Entry(
                    new Title("Rayner - the new CEO of Amazon"),
                    new Description("Says \"I couldn't have done it without CS2103!\""),
                    new Link("https://github.com/rlrh"),
                    getTagSet("nus", "soc", "amazon"))
            };
        } catch (MalformedURLException mue) {
            // Will never happen because we wrote the sample links!
            throw new AssertionError("Sample links are incorrect.");
        }
    }

    public static ReadOnlyEntryBook getSampleEntryBook() {
        EntryBook sampleAb = new EntryBook();
        for (Entry sampleEntry : getSampleEntries()) {
            sampleAb.addEntry(sampleEntry);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
            .map(Tag::new)
            .collect(Collectors.toSet());
    }

}
