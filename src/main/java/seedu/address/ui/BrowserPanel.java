package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.UrlUtil;
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
    private static final List<URL> INTERNAL_URLS = List.of(DEFAULT_PAGE, ERROR_PAGE, READER_VIEW_FAILURE_PAGE);

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

    private boolean isRequestInFlight;
    private URL lastUrl;
    private ViewMode viewMode; // current view mode
    private final Function<URL, Optional<URL>> getOfflineUrl; // asks logic what the offline link for a url is
    private final Function<URL, Optional<String>> getArticle;

    public BrowserPanel(ObservableValue<Entry> selectedEntry,
                        ObservableValue<ViewMode> viewMode,
                        Function<URL, Optional<URL>> getOfflineUrl,
                        Function<URL, Optional<String>> getArticle) {

        super(FXML);

        this.viewMode = viewMode.getValue();
        this.getOfflineUrl = getOfflineUrl;
        this.getArticle = getArticle;

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

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

        // Update last external url when current location changes
        webEngine.locationProperty().addListener((observable, oldLocation, newLocation) -> {
            Optional<URL> newUrl = UrlUtil.fromString(newLocation);
            if (newUrl.isPresent() && !INTERNAL_URLS.contains(newUrl.get())) {
                lastUrl = newUrl.get();
            }
        });

        // Load entry page when selected entry changes.
        selectedEntry.addListener((observable, oldEntry, newEntry) -> {
            Optional.ofNullable(newEntry).ifPresentOrElse(this::loadEntryPage, this::loadDefaultPage);
        });

        // React to view mode changes
        viewMode.addListener((observable, oldViewMode, newViewMode) -> {
            this.viewMode = newViewMode;
            if (newViewMode.hasReaderViewType() && currentlyInContent()) {
                setStyleSheet(newViewMode.getReaderViewStyle().getStylesheetLocation());
            } else if (newViewMode.hasReaderViewType() && currentlyInExternalPage()) {
                getArticle
                        .apply(lastUrl)
                        .ifPresentOrElse(this::loadReader, this::loadReader);
            } else if (newViewMode.hasBrowserViewType() && !currentlyInExternalPage()) {
                loadPage(lastUrl.toExternalForm());
            }
        });

        // Load default page on startup.
        loadDefaultPage();

    }

    //=========== Loading state handlers ===============================================================================

    /**
     * Handle running state.
     */
    private void handleRunning() {
        log("Loading");
    }

    /**
     * Handle succeeded state.
     */
    private void handleSucceeded() {
        log("Successfully loaded");

        if (viewMode.hasReaderViewType() && currentlyInExternalPage() && !isRequestInFlight) {
            loadReader(lastUrl.toExternalForm());
        }

        // cleanup
        this.isRequestInFlight = false;
    }

    /**
     * Handle failed state.
     */
    private void handleFailed() {
        log("Failed to load");

        loadErrorPage();

        // cleanup
        this.isRequestInFlight = false;
    }

    /**
     * Handle reader view failed state.
     */
    private void handleReaderViewFailure() {
        log("Failed to load reader view of");

        loadReaderViewFailurePage();
    }

    //=========== State methods ========================================================================================

    /**
     * Finds out the type of the given URL
     * @param currentUrl URL to find type of
     * @return type of the currentUrl
     */
    private UrlType getUrlType(Optional<URL> currentUrl) {
        if (!currentUrl.isPresent()) {
            return UrlType.CONTENT;
        } else if (INTERNAL_URLS.contains(currentUrl.get())) {
            return UrlType.INTERNAL;
        } else if (currentUrl.get().getProtocol().contains("file")) {
            return UrlType.OFFLINE;
        } else {
            return UrlType.ONLINE;
        }
    }

    private UrlType getCurrentUrlType() {
        return getUrlType(UrlUtil.fromString(webEngine.getLocation()));
    }

    private boolean currentlyInExternalPage() {
        return getCurrentUrlType().equals(UrlType.OFFLINE) || getCurrentUrlType().equals(UrlType.ONLINE);
    }

    private boolean currentlyInInternalPage() {
        return getCurrentUrlType().equals(UrlType.INTERNAL);
    }

    private boolean currentlyInContent() {
        return getCurrentUrlType().equals(UrlType.CONTENT);
    }

    /**
     * Logs current URL with loading status.
     * @param status loading status
     */
    private void log(String status) {
        Optional<URL> currentUrl = UrlUtil.fromString(webEngine.getLocation());
        UrlType currentUrlType = getUrlType(currentUrl);

        String message = String.format("%s %s: %s", status, currentUrlType, currentUrl.orElse(lastUrl));
        logger.info(message);
    }

    //=========== Base methods =========================================================================================

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
     * Loads the given entry's web page.
     * @param entry entry to load
     */
    private void loadEntryPage(Entry entry) {
        URL entryUrl = entry.getLink().value;
        if (viewMode.hasReaderViewType()) {
            getOfflineUrl
                    .apply(entryUrl)
                    .ifPresent(url -> lastUrl = url);
            getArticle
                    .apply(entryUrl)
                    .ifPresentOrElse(this::loadReader, () -> loadPage(entryUrl.toExternalForm()));
        } else {
            getOfflineUrl
                    .apply(entryUrl)
                    .map(URL::toExternalForm)
                    .ifPresentOrElse(this::loadPage, () -> loadPage(entryUrl.toExternalForm()));
        }

    }

    /**
     * Loads reader view of current content.
     * Assumes original web page is already loaded.
     */
    private void loadReader() {
        Optional.ofNullable(webEngine.getDocument())
                .map(document -> {
                    try {
                        return XmlUtil.convertDocumentToString(document);
                    } catch (TransformerException te) {
                        return null;
                    }
                })
                .ifPresentOrElse(this::loadReader, this::handleReaderViewFailure);
    }

    /**
     * Loads reader view of given HTML.
     * @param rawHtml HTML to generate reader view of
     */
    private void loadReader(String rawHtml) {

        // set stylesheet for reader view
        setStyleSheet(viewMode.getReaderViewStyle().getStylesheetLocation());

        // process loaded content, then load processed content
        try {
            String processedHtml = ReaderViewUtil.generateReaderViewStringFrom(rawHtml, "");
            loadContent(processedHtml);
        } catch (IllegalArgumentException e) {
            handleReaderViewFailure();
        }

    }

    //=========== Base methods =========================================================================================

    /**
     * Loads a Web page.
     * @param url URL of the Web page to load
     */
    private void loadPage(String url) {
        this.isRequestInFlight = true;
        Platform.runLater(() -> {
            webEngine.setUserStyleSheetLocation(null);
            webEngine.load(url);
        });
    }

    /**
     * Loads HTML content.
     * @param html HTML content to load
     */
    private void loadContent(String html) {
        this.isRequestInFlight = true;
        Platform.runLater(() -> webEngine.loadContent(html));
    }

    /**
     * Sets the CSS stylesheet.
     * @param styleSheetUrl URL of the stylesheet to set
     */
    private void setStyleSheet(URL styleSheetUrl) {
        try {
            Platform.runLater(() -> webEngine.setUserStyleSheetLocation(styleSheetUrl.toExternalForm()));
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }
    }

}
