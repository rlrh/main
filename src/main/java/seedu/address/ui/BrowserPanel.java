package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

import com.google.common.base.Strings;
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
import seedu.address.model.entry.Entry;
import seedu.address.ui.util.ReaderViewUtil;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String BROWSER_FILE_FOLDER = "/browser/";

    public static final URL DEFAULT_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "default.html"));
    public static final URL ERROR_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "error.html"));
    public static final URL READER_VIEW_FAILURE_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "reader_view_failure.html"));

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

    private String currentLocation; // location of selected entry, regardless of reader view, error page etc
    private String currentBaseUrl; // original URL, even if offline file is loaded
    private boolean isLoadingReaderView; //  flag - whether reader view is loading
    private boolean hasLoadedReaderView; // status - whether reader view has loaded
    private ViewMode viewMode; // current view mode

    public BrowserPanel(ObservableValue<Entry> selectedEntry, ObservableValue<ViewMode> viewMode) {

        super(FXML);

        this.currentLocation = "";
        this.currentBaseUrl = "";
        this.isLoadingReaderView = false;
        this.hasLoadedReaderView = false;
        this.viewMode = viewMode.getValue();

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        // Load entry page when selected entry changes.
        selectedEntry.addListener((observable, oldValue, newValue) ->
            Optional.ofNullable(newValue).ifPresentOrElse(this::loadEntryPage, this::loadDefaultPage)
        );

        // Reload when view mode changes.
        viewMode.addListener((observable, oldViewMode, newViewMode) -> {
            this.viewMode = newViewMode;
            if (!Strings.isNullOrEmpty(webEngine.getLocation())) {
                loadPage(webEngine.getLocation());
            } else if (!Strings.isNullOrEmpty(currentLocation)) {
                loadPage(currentLocation);
            }
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
        if (isLoadingReaderView) {
            String message = String.format("Loading reader view for %s...", currentLocation);
            logger.info(message);
        } else {
            String message = String.format("Loading %s...", webEngine.getLocation());
            logger.info(message);
        }
        hasLoadedReaderView = false;
    }

    /**
     * Displays loaded message, and loads reader view if necessary.
     */
    private void handleSucceeded() {

        // Log and display loaded message
        if (isLoadingReaderView) {
            String message = String.format("Successfully loaded reader view for %s", currentLocation);
            logger.info(message);
            hasLoadedReaderView = true;
        } else {
            String message = String.format("Successfully loaded %s", webEngine.getLocation());
            logger.info(message);
            hasLoadedReaderView = false;
        }
        isLoadingReaderView = false;

        // Load reader view if reader view mode is selected but not loaded
        if (viewMode.getViewType().equals(ViewType.READER)
                && !hasLoadedReaderView
                && currentLocation.equals(webEngine.getLocation())) {
            try {
                URL url = new URL(webEngine.getLocation());
                if (url.equals(DEFAULT_PAGE) || url.equals(ERROR_PAGE) || url.equals(READER_VIEW_FAILURE_PAGE)) {
                    return;
                }
                loadReader(currentBaseUrl);
            } catch (MalformedURLException mue) {
                // do nothing
            }
        }

    }

    /**
     * Displays failed to load message, and loads error page.
     */
    private void handleFailed() {
        String message = String.format("Failed to load %s", webEngine.getLocation());
        logger.warning(message);
        loadErrorPage();
    }

    /**
     * Loads an entry.
     * @param entry entry to load
     */
    private void loadEntryPage(Entry entry) {
        currentBaseUrl = entry.getLink().value;
        currentLocation = entry.getOfflineOrOriginalLink().value;
        loadPage(currentLocation);
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
     * Loads an reader view failure HTML file.
     */
    private void loadReaderViewFailurePage() {
        loadPage(READER_VIEW_FAILURE_PAGE.toExternalForm());
    }

    /**
     * Loads a Web page.
     * @param url URL of the Web page to load
     */
    private void loadPage(String url) {
        Platform.runLater(() -> {
            isLoadingReaderView = false;
            webEngine.setUserStyleSheetLocation(null);
            webEngine.load(url);
        });
    }

    /**
     * Loads reader view of current content.
     * Assumes original Web page is already loaded.
     * @param baseUrl base URL used to resolve relative URLs to absolute URLs
     */
    private void loadReader(String baseUrl) {

        // set stylesheet for reader view
        try {
            Platform.runLater(() ->
                webEngine.setUserStyleSheetLocation(
                        viewMode
                        .getReaderViewStyle()
                        .getStylesheetLocation()
                        .toExternalForm()
                )
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }

        // process loaded content, then load processed content
        try {
            String rawHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
            String processedHtml = ReaderViewUtil.generateReaderViewStringFrom(rawHtml, baseUrl);
            Platform.runLater(() -> {
                isLoadingReaderView = true;
                webEngine.loadContent(processedHtml);
            });
        } catch (TransformerException | IllegalArgumentException e) {
            String message = String.format("Failed to load reader view for %s", this.currentLocation);
            logger.warning(message);
            loadReaderViewFailurePage();
        }

    }

}
