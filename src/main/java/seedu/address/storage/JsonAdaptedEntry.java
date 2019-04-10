package seedu.address.storage;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Entry}.
 */
class JsonAdaptedEntry {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Entry's %s field is missing!";

    private final String title;
    private final String description;
    private final String link;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedEntry} with the given entry details.
     */
    @JsonCreator
    public JsonAdaptedEntry(@JsonProperty("title") String title,
                            @JsonProperty("description") String description,
                            @JsonProperty("link") String link,
                            @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.title = title;
        this.description = description;
        this.link = link;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Entry} into this class for Jackson use.
     */
    public JsonAdaptedEntry(Entry source) {
        title = source.getTitle().fullTitle;
        description = source.getDescription().value;
        link = source.getLink().value.toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted entry object into the model's {@code Entry} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted entry.
     */
    public Entry toModelType() throws IllegalValueException {
        final List<Tag> entryTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            entryTags.add(tag.toModelType());
        }

        if (title == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Title.class.getSimpleName()));
        }
        if (!Title.isValidConstructionTitle(title)) {
            throw new IllegalValueException(Title.formExceptionMessage(title));
        }
        final Title modelTitle = new Title(title);

        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                                                          Description.class.getSimpleName()));
        }
        if (!Description.isValidConstructionDescription(description)) {
            throw new IllegalValueException(Description.formExceptionMessage(description));
        }
        final Description modelDescription = new Description(description);

        if (link == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Link.class.getSimpleName()));
        }
        final Link modelLink;
        try {
            modelLink = new Link(link);
        } catch (MalformedURLException mue) {
            throw new IllegalValueException(Link.formExceptionMessage(link));
        }

        final Set<Tag> modelTags = new HashSet<>(entryTags);
        return new Entry(modelTitle, modelDescription, modelLink, modelTags);
    }

}
