package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.XmlUtil;
import seedu.address.logic.commands.CommandResult;
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

    WebEngine webEngine = browser.getEngine();

    private String currentLocation;
    private boolean isCurrentlyReaderView;
    private ViewMode viewMode;

    public BrowserPanel(ObservableValue<Entry> selectedEntry, ObservableValue<ViewMode> viewMode) {

        super(FXML);

        this.currentLocation = "";
        this.isCurrentlyReaderView = false;
        this.viewMode = viewMode.getValue();

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        // Load entry page when selected entry changes.
        selectedEntry.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                loadDefaultPage();
                return;
            }
            loadEntryPage(newValue);
        });

        // Reload when view mode changes.
        viewMode.addListener((observable, oldViewMode, newViewMode) -> {
            this.viewMode = newViewMode;
            reload();
        });

        // Respond to browser state events.
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
            case RUNNING:
                handleRunning();
                break;
            case SUCCEEDED:
                handleSucceeded();
                break;
            case FAILED:
                handleFailed();
                break;
            default:
                break;
            }
        });

        // Load default page on startup.
        loadDefaultPage();

    }

    /**
     * Displays loading message.
     */
    private void handleRunning() {

        if (isCurrentlyReaderView) {
            String message = String.format("Loading reader view for %s...", this.currentLocation);
            logger.info(message);
        } else {
            String message = String.format("Loading %s...", this.currentLocation);
            logger.info(message);
        }

    }

    /**
     * Displays loaded message, and loads reader view if necessary.
     */
    private void handleSucceeded() {

        // Log and display loaded message
        if (isCurrentlyReaderView) {
            String message = String.format("Successfully loaded reader view for %s", this.currentLocation);
            logger.info(message);
        } else {
            String message = String.format("Successfully loaded %s", this.currentLocation);
            logger.info(message);
        }

        // Load reader view if reader view mode is selected but not loaded
        if (viewMode.equals(ViewMode.READER) && !isCurrentlyReaderView) {
            loadReader(this.currentLocation);
        }

    }

    /**
     * Displays failed to load message, and loads error page.
     */
    private void handleFailed() {

        String message = String.format("Failed to load %s", this.currentLocation);
        logger.warning(message);

        loadErrorPage();

    }

    /**
     * Loads an entry.
     * @param entry entry to load
     */
    private void loadEntryPage(Entry entry) {
        loadPage(entry.getLink().value);
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

    /**
     * Loads a Web page.
     * @param url URL of the Web page to load
     */
    private void loadPage(String url) {

        isCurrentlyReaderView = false;
        currentLocation = url;

        Platform.runLater(() -> webEngine.load(url));

    }

    /**
     * Loads reader view of current content.
     * Assumes original Web page is already loaded.
     * @param url base URL
     */
    private void loadReader(String url) {

        isCurrentlyReaderView = true;

        // don't load reader view of default and error pages
        if (url.equals(DEFAULT_PAGE.toExternalForm()) || url.equals(ERROR_PAGE.toExternalForm())) {
            return;
        }

        // process loaded content through Crux, then load processed content
        try {
            String rawHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
            Article article = ArticleExtractor.with(url, rawHtml)
                    .extractMetadata()
                    .extractContent()
                    .article();
            String cleanHtml = article.document.outerHtml();
            Platform.runLater(() -> webEngine.loadContent(cleanHtml));
        } catch (TransformerException te) {
            String message = String.format("Failed to load reader view for %s", this.currentLocation);
            logger.warning(message);
        }
    }

    /**
     * Reloads the current content.
     * Assumes original Web page is already loaded.
     */
    private void reload() {
        switch (viewMode) {
        case BROWSER:
            loadPage(currentLocation);
            break;
        case READER:
            loadReader(currentLocation);
            break;
        default:
            break;
        }
    }

}
