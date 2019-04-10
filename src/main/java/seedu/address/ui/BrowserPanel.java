package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

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

    private Optional<String> onlineLink;
    private Optional<String> offlineLink;
    private boolean isLoadingReaderView; //  flag - whether reader view is loading
    private boolean hasLoadedReaderView; // status - whether reader view has loaded
    private ViewMode viewMode; // current view mode
    private final Function<URL, Optional<URL>> getOfflineUrl; // asks logic what the offline link for a url is
    private final Function<URL, Optional<String>> getArticle;

    public BrowserPanel(ObservableValue<Entry> selectedEntry,
                        ObservableValue<ViewMode> viewMode,
                        Function<URL, Optional<URL>> getOfflineUrl,
                        Function<URL, Optional<String>> getArticle) {

        super(FXML);

        this.onlineLink = Optional.empty();
        this.offlineLink = Optional.empty();
        this.isLoadingReaderView = false;
        this.hasLoadedReaderView = false;
        this.viewMode = viewMode.getValue();
        this.getOfflineUrl = getOfflineUrl;
        this.getArticle = getArticle;

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        // Load entry page when selected entry changes.
        selectedEntry.addListener((observable, oldValue, newValue) ->
            Optional.ofNullable(newValue).ifPresentOrElse(this::loadEntryPage, this::loadDefaultPage)
        );

        // Reload when view mode changes.
        viewMode.addListener((observable, oldViewMode, newViewMode) -> {
            this.viewMode = newViewMode;
            if (newViewMode.getViewType().equals(ViewType.READER) && hasLoadedReaderView) {
                setStyleSheet();
            } else {
                offlineLink.or(() -> onlineLink).ifPresent(this::loadPage);
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
     * Logs loading message.
     */
    private void handleRunning() {
        if (isLoadingReaderView) {
            String message = String.format("Loading reader view for %s...", onlineLink.orElse("unknown"));
            logger.info(message);
        } else {
            String message = String.format("Loading %s...", webEngine.getLocation());
            logger.info(message);
        }
        hasLoadedReaderView = false;
    }

    /**
     * Logs loaded message, and loads reader view if necessary.
     */
    private void handleSucceeded() {

        // Log and display loaded message
        if (isLoadingReaderView) {
            String message = String.format("Successfully loaded reader view for %s", onlineLink.orElse("unknown"));
            logger.info(message);
            hasLoadedReaderView = true;
        } else {
            String message = String.format("Successfully loaded %s", webEngine.getLocation());
            logger.info(message);
            hasLoadedReaderView = false;
        }
        isLoadingReaderView = false;

        // Process any hyperlinks in loaded page
        processHyperlinks();

        // Load reader view if reader view mode is selected but not loaded
        if (viewMode.getViewType().equals(ViewType.READER) && !hasLoadedReaderView) {
            onlineLink.ifPresent(this::loadReader);
        }

    }

    /**
     * Logs failed to load message, and loads error page.
     */
    private void handleFailed() {
        String message = String.format("Failed to load %s", webEngine.getLocation());
        logger.warning(message);
        loadErrorPage();
    }

    /**
     * Logs failed to load reader view message, and loads reader view failure page.
     */
    private void handleReaderViewFailure(String baseUrl) {
        String message = String.format("Failed to load reader view for %s", baseUrl);
        logger.warning(message);
        loadReaderViewFailurePage();
    }

    /**
     * Updates online link whenever a hyperlink is clicked.
     */
    private void processHyperlinks() {
        Optional.ofNullable(webEngine.getDocument())
                .map(document -> document.getElementsByTagName("a"))
                .ifPresent(links -> {
                    for (int i = 0; i < links.getLength(); i++) {
                        final Node link = links.item(i);
                        final Element linkElement = (Element) link;
                        final String href = linkElement.getAttribute("href");
                        final EventTarget linkEventTarget = (EventTarget) link;
                        linkEventTarget.addEventListener("click", e -> onlineLink = Optional.of(href), false);
                    }
                });
    }

    /**
     * Loads an entry.
     * @param entry entry to load
     */
    private void loadEntryPage(Entry entry) {
        final URL entryUrl = entry.getLink().value;
        onlineLink = Optional.of(entryUrl).map(URL::toExternalForm);
        offlineLink = getOfflineUrl.apply(entryUrl).map(URL::toExternalForm);
        final Optional<String> offlineContent = getArticle.apply(entryUrl);

        if (viewMode.getViewType().equals(ViewType.READER) && offlineContent.isPresent()) {
            loadReader(offlineContent.get(), entryUrl.toExternalForm());
        } else {
            loadPage(offlineLink.or(() -> onlineLink).get());
        }
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadInternalPage(DEFAULT_PAGE.toExternalForm());
    }

    /**
     * Loads an error HTML file with a background that matches the general theme.
     */
    private void loadErrorPage() {
        loadInternalPage(ERROR_PAGE.toExternalForm());
    }

    /**
     * Loads an reader view failure HTML file.
     */
    private void loadReaderViewFailurePage() {
        loadInternalPage(READER_VIEW_FAILURE_PAGE.toExternalForm());
    }

    /**
     * Loads a Web page.
     * @param url URL of the Web page to load
     */
    private void loadInternalPage(String url) {
        onlineLink = Optional.empty();
        offlineLink = Optional.empty();
        loadPage(url);
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
        Optional.ofNullable(webEngine.getDocument())
                .ifPresentOrElse(document -> {
                    try {
                        String rawHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
                        loadReader(rawHtml, baseUrl);
                    } catch (TransformerException te) {
                        handleReaderViewFailure(baseUrl);
                    }
                }, () -> handleReaderViewFailure(baseUrl));
    }

    /**
     * Loads reader view of given HTML.
     * @param rawHtml HTML to generate reader view of
     * @param baseUrl base URL used to resolve relative URLs to absolute URLs
     */
    private void loadReader(String rawHtml, String baseUrl) {

        // set stylesheet for reader view
        setStyleSheet();

        // process loaded content, then load processed content
        try {
            String processedHtml = ReaderViewUtil.generateReaderViewStringFrom(rawHtml, baseUrl);
            Platform.runLater(() -> {
                isLoadingReaderView = true;
                webEngine.loadContent(processedHtml);
            });
        } catch (IllegalArgumentException e) {
            handleReaderViewFailure(baseUrl);
        }

    }

    private void setStyleSheet() {
        try {
            Platform.runLater(() ->
                    webEngine.setUserStyleSheetLocation(
                            viewMode.getReaderViewStyle().getStylesheetLocation().toExternalForm())
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }
    }

}
