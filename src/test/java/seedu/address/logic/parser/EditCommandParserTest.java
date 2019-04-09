package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TECH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_ENTRY;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditEntryDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY_DESC = " " + PREFIX_TAG + "";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_TITLE_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TITLE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + TITLE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1"
            + INVALID_TITLE_DESC, Title.formExceptionMessage(INVALID_TITLE.trim())); // invalid title
        assertParseFailure(parser, "1"
            + INVALID_DESCRIPTION_DESC, Description.formExceptionMessage(INVALID_DESCRIPTION.trim())); // invalid desc
        assertParseFailure(parser, "1"
            + INVALID_TAG_DESC, Tag.formExceptionMessage(INVALID_TAG.trim())); // invalid tag

        // invalid description followed by valid title
        assertParseFailure(parser, "1"
            + INVALID_DESCRIPTION_DESC + TITLE_DESC_AMY, Description.formExceptionMessage(INVALID_DESCRIPTION.trim()));

        // valid description followed by invalid description.
        // The test case for invalid description followed by valid description
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1"
            + DESCRIPTION_DESC_BOB + INVALID_DESCRIPTION_DESC,
            Description.formExceptionMessage(INVALID_DESCRIPTION.trim()));

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Entry} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1"
            + TAG_DESC_TECH + TAG_DESC_SCIENCE + TAG_EMPTY_DESC, Tag.formExceptionMessage(""));
        assertParseFailure(parser, "1"
            + TAG_DESC_TECH + TAG_EMPTY_DESC + TAG_DESC_SCIENCE, Tag.formExceptionMessage(""));
        assertParseFailure(parser, "1"
            + TAG_EMPTY_DESC + TAG_DESC_TECH + TAG_DESC_SCIENCE, Tag.formExceptionMessage(""));

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser,
            "1" + INVALID_TITLE_DESC + INVALID_DESCRIPTION_DESC + VALID_DESCRIPTION_AMY,
                Title.formExceptionMessage(INVALID_TITLE.trim()));
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_ENTRY;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_BOB + TAG_DESC_SCIENCE
                + TITLE_DESC_AMY + TAG_DESC_TECH;

        EditCommand.EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withTitle(VALID_TITLE_AMY)
                .withDescription(VALID_DESCRIPTION_BOB).withTags(VALID_TAG_SCIENCE, VALID_TAG_TECH).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder()
            .withTitle(VALID_TITLE_BOB).withDescription(VALID_DESCRIPTION_BOB).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // title
        Index targetIndex = INDEX_THIRD_ENTRY;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_AMY;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withTitle(VALID_TITLE_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_AMY;
        descriptor = new EditEntryDescriptorBuilder().withDescription(VALID_DESCRIPTION_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_TECH;
        descriptor = new EditEntryDescriptorBuilder().withTags(VALID_TAG_TECH).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_AMY
                + TAG_DESC_TECH + DESCRIPTION_DESC_AMY + TAG_DESC_TECH
                + DESCRIPTION_DESC_BOB + TAG_DESC_SCIENCE;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withDescription(VALID_DESCRIPTION_BOB)
                .withTags(VALID_TAG_TECH, VALID_TAG_SCIENCE).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + DESCRIPTION_DESC_BOB;
        EditCommand.EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder()
                                                            .withDescription(VALID_DESCRIPTION_BOB)
                                                            .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + TITLE_DESC_BOB + INVALID_DESCRIPTION_DESC
                + DESCRIPTION_DESC_BOB;
        descriptor = new EditEntryDescriptorBuilder()
            .withDescription(VALID_DESCRIPTION_BOB)
            .withTitle(VALID_TITLE_BOB)
            .build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_ENTRY;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY_DESC;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
