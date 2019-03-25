package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STYLE;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.ui.ViewMode;
import seedu.address.ui.ViewType;

/**
 * Sets the view mode.
 */
public class ViewModeCommand extends Command {

    public static final String COMMAND_WORD = "view";
    public static final String COMMAND_ALIAS = "v";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the view mode.\n"
            + "Parameters: VIEW_MODE (browser, reader) "
            + "[" + PREFIX_STYLE + "STYLE (default, sepia, dark, black)]\n"
            + "Example: " + COMMAND_WORD + " " + ViewType.READER.toString().toLowerCase();

    public static final String MESSAGE_SET_VIEW_MODE_SUCCESS = "View Mode: %1$s";

    private final ViewMode viewMode;

    public ViewModeCommand(ViewMode viewMode) {
        this.viewMode = viewMode;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setViewMode(this.viewMode);
        return new CommandResult(String.format(MESSAGE_SET_VIEW_MODE_SUCCESS, this.viewMode.toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewModeCommand // instanceof handles nulls
                && viewMode.equals(((ViewModeCommand) other).viewMode)); // state check
    }
}
