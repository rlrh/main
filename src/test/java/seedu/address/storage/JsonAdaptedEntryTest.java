package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.JsonAdaptedEntry.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalEntries.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.testutil.Assert;

public class JsonAdaptedEntryTest {
    private static final String INVALID_TITLE = " ";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_LINK = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_TITLE = BENSON.getTitle().toString();
    private static final String VALID_DESCRIPTION = BENSON.getDescription().toString();
    private static final String VALID_LINK = BENSON.getLink().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validEntryDetails_returnsEntry() throws Exception {
        JsonAdaptedEntry entry = new JsonAdaptedEntry(BENSON);
        assertEquals(BENSON, entry.toModelType());
    }

    @Test
    public void toModelType_invalidTitle_throwsIllegalValueException() {
        JsonAdaptedEntry entry =
                new JsonAdaptedEntry(INVALID_TITLE, VALID_DESCRIPTION, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Title.formExceptionMessage(INVALID_TITLE);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_nullTitle_throwsIllegalValueException() {
        JsonAdaptedEntry entry = new JsonAdaptedEntry(null, VALID_DESCRIPTION, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Title.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedEntry entry =
            new JsonAdaptedEntry(VALID_TITLE, INVALID_DESCRIPTION, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Description.formExceptionMessage(INVALID_DESCRIPTION);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedEntry entry = new JsonAdaptedEntry(VALID_TITLE, null, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_invalidLink_throwsIllegalValueException() {
        JsonAdaptedEntry entry =
                new JsonAdaptedEntry(VALID_TITLE, VALID_DESCRIPTION, INVALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Link.formExceptionMessage(INVALID_LINK);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_nullLink_throwsIllegalValueException() {
        JsonAdaptedEntry entry = new JsonAdaptedEntry(VALID_TITLE, VALID_DESCRIPTION, null, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Link.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedEntry entry =
                new JsonAdaptedEntry(VALID_TITLE, VALID_DESCRIPTION, VALID_LINK, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.formExceptionMessage(INVALID_ADDRESS);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedEntry entry = new JsonAdaptedEntry(VALID_TITLE, VALID_DESCRIPTION, VALID_LINK, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, entry::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedEntry entry =
                new JsonAdaptedEntry(VALID_TITLE, VALID_DESCRIPTION, VALID_LINK, VALID_ADDRESS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, entry::toModelType);
    }

}
