package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
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



    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(BrowserPanel.class);

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

    //private final List<URL> internalPages;

    /** aaa */
    //enum LoadingState { LOADING, LOADED }
    enum LoadingSource { INTERNAL, LOCAL, REMOTE, READER }

    //private LoadingState loadState;
    private LoadingSource loadSrc;

    private ObservableValue<ViewMode> viewMode; // current view mode
    private final Function<URL, Optional<URL>> getOfflineUrl; // asks logic what the offline link for a url is
    private final Function<URL, Optional<String>> getArticle;

    public BrowserPanel(ObservableValue<Entry> selectedEntry,
                        ObservableValue<ViewMode> viewMode,
                        Function<URL, Optional<URL>> getOfflineUrl,
                        Function<URL, Optional<String>> getArticle) {

        super(FXML);

        this.viewMode = viewMode;
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

        // Load entry page when selected entry changes.
        selectedEntry.addListener((observable) -> {
            System.out.println("Loading new entry");
            Entry newValue = selectedEntry.getValue();
            Optional.ofNullable(newValue).ifPresentOrElse(this::loadEntryPage, this::loadDefaultPage);
        });

        viewMode.addListener((obs, oldv, newv) -> {
            if (oldv.getViewType().equals(ViewType.READER) && newv.getViewType().equals(ViewType.READER)) {
                logger.fine("Changing reader view style from " + oldv.getReaderViewStyle() + " to "
                        + newv.getReaderViewStyle());
                updateReaderViewStyleSheet(newv.getReaderViewStyle());
            }
            // todo: handle page already open or something (or maybe just reload page???)

        });

        /* viewMode.addListener((observable) -> {
            ViewMode newViewMode = viewMode.getValue();
            if (newViewMode.getViewType().equals(ViewType.READER) && getCurrentUrlType().equals(UrlType.READER_VIEW)) {
                //setStyleSheet(newViewMode.getReaderViewStyle().getStylesheetLocation());
            } else if (newViewMode.getViewType().equals(ViewType.READER) && getCurrentUrlType().equals(UrlType.ONLINE)) {
                loadReader(lastUrl.toExternalForm());
            } else if (newViewMode.getViewType().equals(ViewType.READER) && getCurrentUrlType().equals(UrlType.OFFLINE)) {
                getArticle.apply(lastUrl).ifPresentOrElse(html ->
                        loadReader(html, lastUrl.toExternalForm()),
                        () -> loadReader(lastUrl.toExternalForm())
                );
            } else if (newViewMode.getViewType().equals(ViewType.BROWSER) &&
                    !(getCurrentUrlType().equals(UrlType.ONLINE) && getCurrentUrlType().equals(UrlType.OFFLINE))) {
                loadPage(lastUrl.toExternalForm());
            }
        }); */

        // Load default page on startup.
        loadDefaultPage();

    }


    /**
     * Logs loading message.
     */
    private void handleRunning() {
        logger.info("Loading " + webEngine.getLocation());
    }

    /**
     * Logs loaded message, and loads reader view if necessary.
     */
    private void handleSucceeded() {
        if (isReaderView() && loadSrc == LoadingSource.REMOTE) {
            logger.info("Refreshing just loaded page " + webEngine.getLocation() + " with reader view.");
            loadReader(webEngine.getLocation()); // todo: maybe refer to something?
            return;
        }
        // current state
        //loadState = LoadingState.LOADED;
        /*Optional<URL> currentUrl = UrlUtil.fromString(webEngine.getLocation());
        currentUrl.ifPresent(url -> {
            if (!internalPages.contains(url)) {
                lastUrl = url;
            }
        });
        UrlType currentState = calculateState(UrlUtil.fromString(webEngine.getLocation()), internalPages);
        String message = String.format("Successfully loaded %s -- state: %s", webEngine.getLocation(), loadState);*/
        logger.info("Successfully loaded " + webEngine.getLocation());

        // actions
        /*if (viewMode.getViewType().equals(ViewType.READER) &&
                (getCurrentUrlType().equals(UrlType.ONLINE) || getCurrentUrlType().equals(UrlType.OFFLINE))) {
            loadReader(lastUrl.toExternalForm());
        }*/

        // next state
    }

    private boolean isReaderView() {
        return viewMode.getValue().getViewType().equals(ViewType.READER);
    }
    /**
     * Logs failed to load message, and loads error page.
     */
    private void handleFailed() {
        // current state
        Optional<URL> currentUrl = UrlUtil.fromString(webEngine.getLocation());
        /* currentUrl.ifPresent(url -> {
            if (!internalPages.contains(url)) lastUrl = url;
        });
        UrlType currentState = calculateState(UrlUtil.fromString(webEngine.getLocation()), internalPages);
        String message = String.format("Failed to load %s: %s", "BB", currentUrl.orElse(lastUrl));*/
        logger.info("Failed to load " + webEngine.getLocation());

        // next state

        // actions
        loadErrorPage();
    }

    /**
     * Logs failed to load message, and loads error page.
     */
    private void handleReaderViewFailure(String baseUrl) {
        // next state

        // actions
        loadReaderViewFailurePage();
    }


    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadInternalPage(DEFAULT_PAGE);
    }

    /**
     * Loads an error HTML file with a background that matches the general theme.
     */
    private void loadErrorPage() {
        loadInternalPage(ERROR_PAGE);
    }

    /**
     * Loads an reader view failure HTML file.
     */
    private void loadReaderViewFailurePage() {
        loadInternalPage(READER_VIEW_FAILURE_PAGE);
    }

    private void loadInternalPage(URL url) {
        loadSrc = LoadingSource.INTERNAL;
        loadPage(url.toExternalForm());
    }

    private void loadEntryPage(Entry entry) {
        URL entryUrl = entry.getLink().value;
        Optional<String> offlineUrl = getOfflineUrl.apply(entryUrl).map(URL::toExternalForm);
        if (offlineUrl.isPresent()) {
            Optional<String> html = getArticle.apply(entryUrl);
            if (isReaderView() && html.isPresent()) {
                loadSrc = LoadingSource.READER;
                loadReader(html.get(), entryUrl.toExternalForm());
                // when html is not present we let handleSuccess take care of things
            } else {
                loadSrc = LoadingSource.LOCAL;
                loadPage(offlineUrl.get());
            }
        } else {
            loadSrc = LoadingSource.REMOTE;
            loadPage(entryUrl.toExternalForm());
        }
        /*if (viewMode.getValue().getViewType().equals(ViewType.READER)) {
            System.out.println("Reader entry page");
            getOfflineUrl.apply(entryUrl).ifPresent(url -> {
                System.out.println(url);
                lastUrl = url;
            });
            getArticle.apply(entryUrl)
                    .ifPresentOrElse(html -> {
                        System.out.println("Offline reader entry page");
                        loadReader(html, entryUrl.toExternalForm());
                    }, () -> {
                        System.out.println("Online reader entry page");
                        loadPage(entryUrl.toExternalForm());
                    });
        } else {
            getOfflineUrl.apply(entryUrl)
                    .map(URL::toExternalForm)
                    .ifPresentOrElse(this::loadPage, () -> loadPage(entryUrl.toExternalForm()));
        }*/

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
        loadSrc = LoadingSource.READER;
        // set stylesheet for reader view
        setStyleSheet(viewMode.getValue().getReaderViewStyle().getStylesheetLocation());

        // process loaded content, then load processed content
        try {
            String processedHtml = ReaderViewUtil.generateReaderViewStringFrom(rawHtml, baseUrl);
            loadContent(processedHtml);
        } catch (IllegalArgumentException e) {
            handleReaderViewFailure(baseUrl);
        }

    }

    /**
     * Sets the CSS stylesheet.
     * @param styleSheetUrl URL of the stylesheet to set
     */
    private void setStyleSheet(URL styleSheetUrl) {
        try {
            Platform.runLater(() ->
                    webEngine.setUserStyleSheetLocation(styleSheetUrl.toExternalForm())
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }
    }

    /**
     * Loads a Web page.
     * @param url URL of the Web page to load
     */
    private void loadPage(String url) {
        System.out.println("loadPage: Trying to load " + url);
        /*if (loadState == LoadingState.LOADING) {
            System.out.println("Changing page, try (?) cancel current load");
            webEngine.getLoadWorker().cancel();
        }*/
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
        Platform.runLater(() -> webEngine.loadContent(html));
    }

    /*
    private UrlType calculateState(Optional<URL> currentUrl, List<URL> internalPages) {

        if (!currentUrl.isPresent()) {
            return UrlType.READER_VIEW;
        } else if (internalPages.contains(currentUrl.get())) {
            return UrlType.INTERNAL;
        } else if (currentUrl.get().getProtocol().contains("file")) {
            return UrlType.OFFLINE;
        } else {
            return UrlType.ONLINE;
        }

        /*
        boolean loadingInternalPage = currentUrl.map(internalPages::contains).orElse(false);
        System.out.println(String.format("Loading internal page: %s", loadingInternalPage));

        Optional<URL> lastEntryOfflineUrl = lastEntryUrl.flatMap(getOfflineUrl);
        boolean loadingEntryPage = false;
        boolean loadingReaderView = false;
        if (currentUrl.isPresent() && lastEntryUrl.isPresent()) {
            loadingEntryPage = currentUrl.get().equals(lastEntryUrl.get());
        }
        if (currentUrl.isPresent() && lastEntryOfflineUrl.isPresent()) {
            loadingEntryPage = currentUrl.get().equals(lastEntryOfflineUrl.get());
        }
        if (!currentUrl.isPresent() && lastEntryUrl.isPresent()) {
            loadingEntryPage = currentUrl.get().equals(lastEntryUrl.get());
        }
        if (!currentUrl.isPresent() && lastEntryOfflineUrl.isPresent()) {
            loadingEntryPage = currentUrl.get().equals(lastEntryOfflineUrl.get());
        }
        System.out.println(String.format("Loading entry page: %s", loadingEntryPage));


    }

    private UrlType getCurrentUrlType() {
        return calculateState(UrlUtil.fromString(webEngine.getLocation()), internalPages);
    }*/


    /** aaa. */
    private void updateReaderViewStyleSheet(ReaderViewStyle readerViewStyle) {
        try {
            Platform.runLater(() ->
                    webEngine.setUserStyleSheetLocation(readerViewStyle.getStylesheetLocation().toExternalForm())
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }
    }

}
