package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;

/**
 * Archives all entries in the displayed entry list.
 */
public class DeleteAllCommand extends Command {

    public static final String COMMAND_WORD = "deleteall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes all entries in the displayed entry list.\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "All entries deleted: (%s entries)";
    public static final String MESSAGE_FAILURE_INCORRECT_CONTEXT = "Invalid context for delete all command.";

    private static final Logger logger = LogsCenter.getLogger(DeleteAllCommand.class);

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();
        logger.info(String.format("%d", lastShownList.size()));

        int numDeleted = 0;
        for (int i = lastShownList.size(); i > 0; i--) {
            Index index = Index.fromOneBased(i);
            Command deleteCommand;
            switch (model.getContext()) {
            case CONTEXT_LIST:
                deleteCommand = new DeleteCommand(index);
                break;
            case CONTEXT_ARCHIVES:
                deleteCommand = new DeleteArchiveEntryCommand(index);
                break;
            default:
                throw new CommandException(MESSAGE_FAILURE_INCORRECT_CONTEXT);
            }
            deleteCommand.execute(model, history);
            numDeleted++;
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, numDeleted));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteAllCommand); // instanceof handles nulls
    }
}
