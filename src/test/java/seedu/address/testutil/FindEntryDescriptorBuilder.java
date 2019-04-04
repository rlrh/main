package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand.FindEntryDescriptor;
import seedu.address.model.entry.Entry;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building FindEntryDescriptor objects.
 */
public class FindEntryDescriptorBuilder {

    private FindEntryDescriptor descriptor;

    public FindEntryDescriptorBuilder() {
        descriptor = new FindEntryDescriptor();
    }

    public FindEntryDescriptorBuilder(FindEntryDescriptor descriptor) {
        this.descriptor = new FindEntryDescriptor(descriptor);
    }

    /**
     * Returns an {@code FindEntryDescriptor} with fields containing {@code entry}'s details
     */
    public FindEntryDescriptorBuilder(Entry entry) {
        descriptor = new FindEntryDescriptor();
        descriptor.setTitle(entry.getTitle().fullTitle);
        descriptor.setDescription(entry.getDescription().value);
        descriptor.setLink(entry.getLink().value);
        descriptor.setAddress(entry.getAddress().value);
        descriptor.setTags(entry.getTags());
    }

    /**
     * Sets the {@code String} keyphrase for title of the {@code FindEntryDescriptor} that we are building.
     */
    public FindEntryDescriptorBuilder withTitle(String title) {
        descriptor.setTitle(title);
        return this;
    }

    /**
     * Sets the {@code String} keyphrase for description of the {@code FindEntryDescriptor} that we are building.
     */
    public FindEntryDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(description);
        return this;
    }

    /**
     * Sets the {@code String} keyphrase for link of the {@code FindEntryDescriptor} that we are building.
     */
    public FindEntryDescriptorBuilder withLink(String link) {
        descriptor.setLink(link);
        return this;
    }

    /**
     * Sets the {@code String} keyphrase for address of the {@code FindEntryDescriptor} that we are building.
     */
    public FindEntryDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(address);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code FindEntryDescriptor}
     * that we are building.
     */
    public FindEntryDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    /**
     * Resets all the fields to empty.
     */
    public FindEntryDescriptorBuilder reset() {
        return new FindEntryDescriptorBuilder();
    }

    public FindEntryDescriptor build() {
        return descriptor;
    }
}
