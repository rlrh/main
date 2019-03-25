package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

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
    public static final URL READER_VIEW_FAILURE_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "reader_view_failure.html"));
    public static final URL STYLESHEET =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "default.css"));

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

    private String currentLocation;
    private String currentBaseUrl;
    private boolean isLoadingReaderView;
    private boolean isReaderViewLoaded;
    private ViewMode viewMode;

    public BrowserPanel(ObservableValue<Entry> selectedEntry, ObservableValue<ViewMode> viewMode) {

        super(FXML);

        this.currentLocation = "";
        this.currentBaseUrl = "";
        this.isLoadingReaderView = false;
        this.isReaderViewLoaded = false;
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

        if (isLoadingReaderView) {
            String message = String.format("Loading reader view for %s...", currentLocation);
            logger.info(message);
        } else {
            String message = String.format("Loading %s...", webEngine.getLocation());
            logger.info(message);
        }
        isReaderViewLoaded = false;

    }

    /**
     * Displays loaded message, and loads reader view if necessary.
     */
    private void handleSucceeded() {

        // Log and display loaded message
        if (isLoadingReaderView) {
            String message = String.format("Successfully loaded reader view for %s", currentLocation);
            logger.info(message);
            isReaderViewLoaded = true;
        } else {
            String message = String.format("Successfully loaded %s", webEngine.getLocation());
            logger.info(message);
            isReaderViewLoaded = false;
        }
        isLoadingReaderView = false;

        // Load reader view if reader view mode is selected but not loaded
        if (viewMode.equals(ViewMode.READER) && !isReaderViewLoaded && currentLocation.equals(webEngine.getLocation())) {
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

        isLoadingReaderView = false;
        isReaderViewLoaded = false;

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
            Platform.runLater(() -> webEngine.setUserStyleSheetLocation(STYLESHEET.toExternalForm()));
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }

        // process loaded content, then load processed content
        try {
            String rawHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
            Document readerDocument = getReaderDocumentFrom(rawHtml, baseUrl);
            String processedHtml = readerDocument.outerHtml();
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

    /**
     * Gets browser's web engine.
     * @return browser's web engine
     */
    protected WebEngine getWebEngine() {
        return webEngine;
    }

    /**
     * Gets a document representing the reader view of the given HTML.
     * @param rawHtml raw HTML to process
     * @param baseUrl base URL used to resolve relative URLs to absolute URLs
     * @return document representing the reader view of rawHtml
     */
    protected Document getReaderDocumentFrom(String rawHtml, String baseUrl) throws IllegalArgumentException {

        // raw document
        Document rawDocument = Jsoup.parse(rawHtml, baseUrl);
        if (rawDocument.body().text().isEmpty()) {
            throw new IllegalArgumentException();
        }

        // extract article metadata and content using Crux
        Article article = ArticleExtractor.with(baseUrl, rawDocument)
                .extractMetadata()
                .extractContent()
                .estimateReadingTime()
                .article();

        // reparse using Jsoup
        String processedHtml = article.document.outerHtml();
        Document document = Jsoup.parse(processedHtml, baseUrl);
        if (document.body().text().isEmpty()) {
            throw new IllegalArgumentException();
        }

        // resolve relative links to absolute links
        resolveRelativeLinksToAbsoluteLinks(document);

        // wrap body in container
        document.body().addClass("container py-5");

        // add article metadata
        createArticleMetadataElement(rawDocument).ifPresent(document.body()::prependChild);

        // add title
        createTitleElement(article).ifPresent(document.body()::prependChild);

        // add site name
        createSiteNameElement(article).ifPresent(document.body()::prependChild);

        return document;

    }

    /**
     * Resolves relative links to absolute links.
     * @param document Jsoup document parsed from raw HTML
     */
    private void resolveRelativeLinksToAbsoluteLinks(Document document) {
        Elements links = document.select("a");
        for (Element link : links) {
            String absoluteUrl = link.absUrl("href");
            link.attr("href", absoluteUrl);
        }
    }

    /**
     * Attempts to create an article metadata element.
     * @param rawDocument Jsoup document parsed from raw HTML
     * @return optional article metadata element
     */
    private Optional<Element> createArticleMetadataElement(Document rawDocument) {

        Element articleMetadataWrapperElement = new Element(Tag.valueOf("p"), "");

        // add authors
        String authors = rawDocument
                .select("head meta[property=article:author]")
                .stream()
                .map(author -> author.attr("content"))
                .collect(Collectors.joining(", "));
        if (!authors.isEmpty()) {
            Element authorsElement = new Element(Tag.valueOf("small"), "")
                    .text("By " + authors)
                    .addClass("pr-3");
            articleMetadataWrapperElement.appendChild(authorsElement);
        }

        // add published time
        String publishedTime = rawDocument
                .select("head meta[property=article:published_time]")
                .attr("content");
        if (!publishedTime.isEmpty()) {
            Element publishedTimeElement = new Element(Tag.valueOf("small"), "")
                    .text("Published " + publishedTime)
                    .addClass("pr-2");
            articleMetadataWrapperElement.appendChild(publishedTimeElement);
        }

        // add modified time
        String modifiedTime = rawDocument
                .select("head meta[property=article:modified_time]")
                .attr("content");
        if (!modifiedTime.isEmpty()) {
            Element modifiedTimeElement = new Element(Tag.valueOf("small"), "")
                    .text("Modified " + modifiedTime)
                    .addClass("pr-3");
            articleMetadataWrapperElement.appendChild(modifiedTimeElement);
        }

        // add section
        String section = rawDocument
                .select("head meta[property=article:section]")
                .attr("content");
        if (!section.isEmpty()) {
            Element sectionElement = new Element(Tag.valueOf("small"), "")
                    .text("In " + section + " section")
                    .addClass("pr-3");
            articleMetadataWrapperElement.appendChild(sectionElement);
        }

        if (articleMetadataWrapperElement.children().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(articleMetadataWrapperElement);
        }

    }

    /**
     * Attempts to create an estimated reading time element.
     * @param article Crux article
     * @return optional estimated reading time element
     */
    private Optional<Element> createReadingTimeElement(Article article) {
        if (article.estimatedReadingTimeMinutes == 0) {
            return Optional.empty();
        }
        Element timeElement = new Element(Tag.valueOf("small"), "")
                .text(article.estimatedReadingTimeMinutes + " minute(s)");
        Element timeWrapperElement = new Element(Tag.valueOf("p"), "").appendChild(timeElement);
        return Optional.of(timeWrapperElement);
    }

    /**
     * Attempts to create a title element.
     * @param article Crux article
     * @return optional title element
     */
    private Optional<Element> createTitleElement(Article article) {
        if (article.title.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Element(Tag.valueOf("h1"), "").text(article.title).addClass("pb-3"));
    }

    /**
     * Attempts to create a site name element.
     * @param article Crux article
     * @return optional site name element
     */
    private Optional<Element> createSiteNameElement(Article article) {
        if (article.siteName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Element(Tag.valueOf("p"), "").text(article.siteName).addClass("lead"));
    }

}
