package seedu.address.testutil;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_TITLE = "Alice Pauline";
    public static final String DEFAULT_DESCRIPTION = "85355255";
    public static final String DEFAULT_LINK = "https://alice.gmail.com";

    private Title title;
    private Description description;
    private Link link;
    private Set<Tag> tags;

    public EntryBuilder() {
        title = new Title(DEFAULT_TITLE);
        description = new Description(DEFAULT_DESCRIPTION);
        link = new Link(TestUtil.toUrl(DEFAULT_LINK));
        tags = new HashSet<>();
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     */
    public EntryBuilder(Entry entryToCopy) {
        title = entryToCopy.getTitle();
        description = entryToCopy.getDescription();
        link = entryToCopy.getLink();
        tags = new HashSet<>(entryToCopy.getTags());
    }

    /**
     * Sets the {@code Title} of the {@code Entry} that we are building.
     */
    public EntryBuilder withTitle(String title) {
        this.title = new Title(title);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Entry} that we are building.
     */
    public EntryBuilder withTags(String ... tags) {
        this.tags = getTagSet(tags);
        return this;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
            .map(Tag::new)
            .collect(Collectors.toSet());
    }

    /**
     * Sets the {@code Description} of the {@code Entry} that we are building.
     */
    public EntryBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Link} of the {@code Entry} that we are building.
     */
    public EntryBuilder withLink(URL link) {
        this.link = new Link(link);
        return this;
    }

    /**
     * Sets the {@code Link} of the {@code Entry} that we are building.
     */
    public EntryBuilder withLink(String link) {
        this.link = new Link(TestUtil.toUrl(link));
        return this;
    }

    public Entry build() {
        return new Entry(title, description, link, tags);
    }

}
