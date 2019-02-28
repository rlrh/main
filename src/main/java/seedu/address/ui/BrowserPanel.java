package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.entry.Entry;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final URL DEFAULT_PAGE =
            requireNonNull(MainApp.class.getResource(FXML_FILE_FOLDER + "default.html"));
    public static final URL ERROR_PAGE =
            requireNonNull(MainApp.class.getResource(FXML_FILE_FOLDER + "error.html"));

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

    public BrowserPanel(ObservableValue<Entry> selectedPerson) {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        // Load entry page when selected entry changes.
        selectedPerson.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                loadDefaultPage();
                return;
            }
            loadPersonPage(newValue);
        });

        // Load error page when error occurs.
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            String location = browser.getEngine().getLocation();
            switch (newState) {
            case RUNNING:
                logger.info(String.format("Loading %s...", location));
                break;
            case SUCCEEDED:
                logger.info(String.format("Successfully loaded %s", location));
                break;
            case FAILED:
                logger.warning(String.format("Failed to load %s", location));
                loadErrorPage();
                break;
            default:
                break;
            }
        });

        loadDefaultPage();
    }

    private void loadPersonPage(Entry entry) {
        loadPage(entry.getLink().value);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> webEngine.load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadPage(DEFAULT_PAGE.toExternalForm());
    }

    /**
     * Loads an error HTML file with a background that matches the general theme.
     */
    private void loadErrorPage() {
        loadPage(ERROR_PAGE.toExternalForm());
    }

}
