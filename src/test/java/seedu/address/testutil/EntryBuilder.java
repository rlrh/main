package seedu.address.testutil;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_TITLE = "Alice Pauline";
    public static final String DEFAULT_DESCRIPTION = "85355255";
    public static final String DEFAULT_LINK = "https://alice.gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Title title;
    private Description description;
    private Link link;
    private Address address;
    private Set<Tag> tags;

    public EntryBuilder() {
        title = new Title(DEFAULT_TITLE);
        description = new Description(DEFAULT_DESCRIPTION);
        link = new Link(TestUtil.toUrl(DEFAULT_LINK));
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     */
    public EntryBuilder(Entry entryToCopy) {
        title = entryToCopy.getTitle();
        description = entryToCopy.getDescription();
        link = entryToCopy.getLink();
        address = entryToCopy.getAddress();
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
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Entry} that we are building.
     */
    public EntryBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
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
        return new Entry(title, description, link, address, tags);
    }

}
