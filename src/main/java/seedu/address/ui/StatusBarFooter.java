package seedu.address.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.Date;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.ModelContext;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    public static final String SYNC_STATUS_INITIAL = "Not updated yet in this session";
    public static final String SYNC_STATUS_UPDATED = "Last updated: %s";
    public static final String CONTEXT_ENTRY_COUNT_STATUS = "Displaying %d entries in %s context";

    /**
     * Used to generate time stamps.
     *
     * TODO: change clock to an instance variable.
     * We leave it as a static variable because manual dependency injection
     * will require passing down the clock reference all the way from MainApp,
     * but it should be easier once we have factories/DI frameworks.
     */
    private static Clock clock = Clock.systemDefaultZone();

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label syncStatus;
    @FXML
    private Label saveLocationStatus;
    @FXML
    private Label contextEntryCountStatus;

    public StatusBarFooter(Path saveLocation,
                           ReadOnlyEntryBook addressBook,
                           ObservableList<Entry> entryList,
                           ObservableValue<ModelContext> context) {
        super(FXML);

        addressBook.addListener(observable -> updateSyncStatus());
        syncStatus.setText(SYNC_STATUS_INITIAL);

        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());

        entryList.addListener((ListChangeListener.Change<? extends Entry> change) ->
            updateContextEntryCountStatus(change.getList().size(), context.getValue().toString())
        );
        context.addListener((observable, oldContext, newContext) ->
            updateContextEntryCountStatus(entryList.size(), newContext.toString())
        );
        updateContextEntryCountStatus(entryList.size(), context.getValue().toString());
    }

    /**
     * Updates the context and entry count status.
     * @param entryCount number of entries displayed
     * @param context current context in model
     */
    private void updateContextEntryCountStatus(int entryCount, String context) {
        contextEntryCountStatus.setText(String.format(CONTEXT_ENTRY_COUNT_STATUS, entryCount, context));
    }

    /**
     * Sets the clock used to determine the current time.
     */
    public static void setClock(Clock clock) {
        StatusBarFooter.clock = clock;
    }

    /**
     * Returns the clock currently in use.
     */
    public static Clock getClock() {
        return clock;
    }

    /**
     * Updates "last updated" status to the current time.
     */
    private void updateSyncStatus() {
        long now = clock.millis();
        String lastUpdated = new Date(now).toString();
        syncStatus.setText(String.format(SYNC_STATUS_UPDATED, lastUpdated));
    }

}
