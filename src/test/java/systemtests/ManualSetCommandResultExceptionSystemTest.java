package systemtests;

import org.junit.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

public class ManualSetCommandResultExceptionSystemTest extends AddressBookSystemTest {

    @Test
    public void commandResultDisplayed() {
        String expectedMessage = "Manually set command result successfully displayed";
        CommandResult result = new CommandResult(expectedMessage);
        assertCommandResultHandled(result, expectedMessage);
    }

    @Test
    public void exceptionPropagated() {
        String expectedMessage = "Manually set exception successfully propagated";
        Exception exception = new Exception(expectedMessage);
        assertExceptionHandled(exception, expectedMessage);
    }

    /**
     * Asserts that a manually set {@code CommandResult} displays as expected:<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. Result display box has default style class.<br>
     * 3. Command box has the default style class.<br>
     * @param commandResult command result to set
     * @param expectedResultMessage result message expected to be displayed
     */
    private void assertCommandResultHandled(CommandResult commandResult, String expectedResultMessage) {

        Model expectedModel = getModel();
        expectedModel.setCommandResult(commandResult);

        setCommandResultInApp(commandResult);

        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();

    }

    /**
     * Asserts that a manually set {@code Exception} displays as expected:<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. Result display box has error style class.<br>
     * 3. Command box has the error style class.<br>
     * @param e exception to set
     * @param expectedResultMessage result message expected to be displayed
     */
    private void assertExceptionHandled(Exception e, String expectedResultMessage) {

        Model expectedModel = getModel();
        expectedModel.setException(e);

        setExceptionInApp(e);

        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertResultDisplayShowsErrorStyle();

    }
}
