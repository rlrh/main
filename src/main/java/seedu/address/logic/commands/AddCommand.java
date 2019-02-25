package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import javafx.application.Platform;
import org.jsoup.nodes.Document;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import seedu.address.network.Network;
import org.apache.commons.io.*;
import com.chimbori.crux.articles.*;
import java.io.*;
import javafx.concurrent.Task;

import javax.imageio.plugins.tiff.ExifParentTIFFTagSet;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_FAILURE = "Failed to add person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        String url = toAdd.getEmail().value;
        String filename = toAdd.getName().fullName + ".html";

        /*
        model.addPerson(toAdd);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        */

        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws Exception {
                try {
                    Network.makeGetRequestAsString(url)
                            .thenAccept(rawHTML -> {
                                //System.out.println(string);
                                try {
                                    Document cleanDoc = ArticleExtractor.with(url, rawHTML)
                                            .extractMetadata()
                                            .extractContent()  // If you only need metadata, you can skip `.extractContent()`
                                            .article()
                                            .document;
                                    //System.out.println(cleanDoc);
                                    File targetFile = new File("data/files/" + filename);
                                    FileUtils.writeStringToFile(targetFile, cleanDoc.outerHtml());
                                } catch (IOException e) {
                                    e.printStackTrace();

                                }

                            })
                            .get();
                    Thread.sleep(5000);
                    Platform.runLater(() -> {
                        model.addPerson(toAdd);
                        model.commitAddressBook();
                    });
                    return null;
                } catch (Exception e) {
                    throw e;
                }
            }
        };
        new Thread(task).start();

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));

    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
