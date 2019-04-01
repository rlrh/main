package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

/** Enters the feed management context. */
public class FeedsCommand extends Command {

    public static final String COMMAND_WORD = "feeds";

    public static final String MESSAGE_SUCCESS = "Context switched to feeds. Listing all feeds";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setContext(ModelContext.CONTEXT_LIST);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
