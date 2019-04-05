package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.entry.EntryContainsSearchTermsPredicate;
import seedu.address.testutil.FindEntryDescriptorBuilder;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(FindCommand.MESSAGE_NO_SEARCH_TERMS,
            FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new EntryContainsSearchTermsPredicate(
                    new FindEntryDescriptorBuilder().withTitle("Alice Bob").build()));
        assertParseSuccess(parser, " " + PREFIX_TITLE + "Alice Bob", expectedFindCommand);
    }

}
