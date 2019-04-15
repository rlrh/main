package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.OfflineMode;

/**
 * Sets the view mode.
 */
public class OfflineModeCommand extends Command {

    public static final String COMMAND_WORD = "offline";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Enable or disable offline mode for this session.\n"
            + "Parameters: MODE (enable, disable)\n"
            + "Example: " + COMMAND_WORD + " disable";

    public static final String MESSAGE_SET_VIEW_MODE_ENABLED = "Offline mode enabled for this session";
    public static final String MESSAGE_SET_VIEW_MODE_DISABLED = "Offline mode disabled for this session";

    private final OfflineMode offlineMode;

    public OfflineModeCommand(OfflineMode offlineMode) {
        this.offlineMode = offlineMode;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setOfflineMode(offlineMode);
        if (offlineMode == OfflineMode.ENABLED) {
            return new CommandResult(MESSAGE_SET_VIEW_MODE_ENABLED);
        } else {
            return new CommandResult(MESSAGE_SET_VIEW_MODE_DISABLED);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OfflineModeCommand // instanceof handles nulls
                && offlineMode == ((OfflineModeCommand) other).offlineMode); // state check
    }
}
