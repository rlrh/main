package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Selects a person identified using it's displayed index from the address book.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "reader";
    public static final String COMMAND_ALIAS = "v";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Toggles reader view.\n"
            + "Parameters: ENABLE_READER_VIEW_BOOLEAN \n"
            + "Example: " + COMMAND_WORD + " true";

    public static final String MESSAGE_VIEW_SUCCESS = "Selected View: %1$s";

    private final Boolean bool;

    public ViewCommand(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        model.setReaderView(this.bool);
        return new CommandResult(String.format(MESSAGE_VIEW_SUCCESS, this.bool.equals(true) ? "Reader" : "Browser"));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewCommand // instanceof handles nulls
                && this.bool.equals(((ViewCommand) other).bool)); // state check
    }
}
