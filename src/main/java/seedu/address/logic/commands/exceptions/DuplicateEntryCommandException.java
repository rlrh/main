package seedu.address.logic.commands.exceptions;

/**
 * Represents an error which occurs during execution of a {@link Command}.
 */
public class DuplicateEntryCommandException extends CommandException {
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the entry book";

    public DuplicateEntryCommandException() {
        super(MESSAGE_DUPLICATE_ENTRY);
    }

}
