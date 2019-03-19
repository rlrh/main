package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

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
import seedu.address.model.entry.Entry;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String BROWSER_FILE_FOLDER = "/browser/";

    public static final URL DEFAULT_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "default.html"));
    public static final URL ERROR_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "error.html"));
    public static final URL STYLESHEET =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "default.css"));

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

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
            loadPage(currentLocation);
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
            loadReader(currentLocation);
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
        loadPage(entry.getOfflineOrOriginalLink().value);
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

        Platform.runLater(() -> {
            webEngine.setUserStyleSheetLocation(null);
            webEngine.load(url);
        });

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

        // set stylesheet for reader view
        try {
            Platform.runLater(() -> webEngine.setUserStyleSheetLocation(STYLESHEET.toExternalForm()));
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }

        // process loaded content, then load processed content
        try {
            String rawHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
            Document readerDocument = getReaderDocumentFrom(url, rawHtml);
            String processedHtml = readerDocument.outerHtml();
            Platform.runLater(() -> webEngine.loadContent(processedHtml));
        } catch (TransformerException | IllegalArgumentException e) {
            String message = String.format("Failed to load reader view for %s", this.currentLocation);
            logger.warning(message);
        }
    }

    /**
     * Gets browser's web engine.
     * @return browser's web engine
     */
    protected WebEngine getWebEngine() {
        return webEngine;
    }

    /**
     * Gets a document representing the reader view of the given HTML.
     * @param baseUrl base URL
     * @param rawHtml raw HTML to process
     * @return document representing the reader view of rawHtml
     */
    protected Document getReaderDocumentFrom(String baseUrl, String rawHtml) throws IllegalArgumentException {

        // extract article metadata and content using Crux
        Article article = ArticleExtractor.with(baseUrl, rawHtml)
                .extractMetadata()
                .extractContent()
                .estimateReadingTime()
                .article();

        // reparse using Jsoup
        String processedHtml = article.document.outerHtml();
        Document document = Jsoup.parse(processedHtml);

        // wrap body in container
        document.body().addClass("container py-5");

        // add estimated reading time
        Element timeElement = new Element(Tag.valueOf("small"), "")
                .text(article.estimatedReadingTimeMinutes + " minutes");
        Element timeWrapperElement = new Element(Tag.valueOf("p"), "").appendChild(timeElement);
        document.body().prependChild(timeWrapperElement);

        // add title
        if (!article.title.isEmpty()) {
            Element titleElement = new Element(Tag.valueOf("h1"), "").text(article.title).addClass("pb-3");
            document.body().prependChild(titleElement);
        }

        // add site name
        if (!article.siteName.isEmpty()) {
            Element siteNameElement = new Element(Tag.valueOf("p"), "").text(article.siteName).addClass("lead");
            document.body().prependChild(siteNameElement);
        }

        return document;

    }
}
