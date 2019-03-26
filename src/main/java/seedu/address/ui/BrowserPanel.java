package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import net.dankito.readability4j.extended.Readability4JExtended;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.DateUtil;
import seedu.address.commons.util.OptionalCandidate;
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
            try {
                URL url = new URL(webEngine.getLocation());
                if (url.equals(DEFAULT_PAGE) || url.equals(ERROR_PAGE) || url.equals(READER_VIEW_FAILURE_PAGE)) {
                    return;
                }
                loadPage(currentLocation);
            } catch (MalformedURLException mue) {
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
        if (viewMode.getViewType().equals(ViewType.READER)
                && !isReaderViewLoaded
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
            Platform.runLater(() -> {
                webEngine.setUserStyleSheetLocation(
                        viewMode
                        .getReaderViewStyle()
                        .getStylesheetLocation()
                        .toExternalForm()
                );
            });
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
     * Gets a document representing the reader view of the given HTML.
     * @param rawHtml raw HTML to process
     * @param baseUrl base URL used to resolve relative URLs to absolute URLs
     * @return document representing the reader view of rawHtml
     */
    protected Document getReaderDocumentFrom(String rawHtml, String baseUrl) throws IllegalArgumentException {

        // create Jsoup document
        Document document = Document.createShell(baseUrl);
        document.updateMetaCharsetElement(true);
        document.charset(StandardCharsets.UTF_8);

        // extract article using Readability4J
        Document rawDocument = Jsoup.parse(rawHtml, baseUrl);
        Readability4J readability4J = new Readability4JExtended(baseUrl, rawDocument);
        Article article = readability4J.parse();

        // put article content in document
        if (article.getArticleContent() == null) {
            throw new IllegalArgumentException("Null article content");
        }
        document.body().prependChild(article.getArticleContent());

        // wrap body in container
        document.body().addClass("container py-5");

        // make images responsive
        makeImagesResponsive(document);

        // add article metadata
        createArticleMetadataElement(rawDocument, article).ifPresent(document.body()::prependChild);

        // add title
        createTitleElement(article).ifPresent(document.body()::prependChild);

        // add site name
        createSiteNameElement(rawDocument).ifPresent(document.body()::prependChild);

        System.out.println(document);

        return document;

    }

    /**
     * Resolves relative links to absolute links.
     * @param document Jsoup document parsed from raw HTML
     */
    private void resolveRelativeLinksToAbsoluteLinks(Document document) {
        document.select("a")
                .forEach(link -> {
                    String absoluteUrl = link.absUrl("href");
                    link.attr("href", absoluteUrl);
                });
    }

    /**
     * Resolves relative links to absolute links.
     * @param document Jsoup document parsed from raw HTML
     */
    private void makeImagesResponsive(Document document) {
        document.select("img").forEach(image -> image.addClass("img-fluid"));
    }

    /**
     * Attempts to create an article metadata element.
     * @param rawDocument Jsoup document parsed from raw HTML
     * @return optional article metadata element
     */
    private Optional<Element> createArticleMetadataElement(Document rawDocument, Article article) {

        Element articleMetadataWrapperElement =
                new Element(Tag.valueOf("div"), "").addClass("d-flex flex-wrap pb-4");

        // add byline element if author(s) present
        OptionalCandidate.with((String candidate) ->
                        Optional.ofNullable(candidate).filter(presentCandidate -> !presentCandidate.isEmpty())
                )
                .tryout(rawDocument
                        .select("head meta[name=author]")
                        .attr("content"))
                .tryout(rawDocument
                        .select("head meta[property=author]")
                        .attr("content"))
                .tryout(rawDocument
                        .select("head meta[property=article:author]")
                        .stream()
                        .map(author -> author.attr("content"))
                        .collect(Collectors.joining(", ")))
                .getOptional()
                .map(authors ->
                        new Element(Tag.valueOf("small"), "")
                                .text("By " + authors)
                                .addClass("pr-3"))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // add published datetime element if published datetime is present
        Optional.of(rawDocument.select("head meta[property=article:published_time]").attr("content"))
                .filter(dateTime -> !dateTime.isEmpty())
                .map(DateUtil::getHumanReadableDateTimeFrom)
                .map(dateTime ->
                    new Element(Tag.valueOf("small"), "")
                            .text(dateTime)
                            .addClass("pr-3"))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // add section element if section is present
        Optional.of(rawDocument.select("head meta[property=article:section]").attr("content"))
                .filter(section -> !section.isEmpty())
                .map(section ->
                        new Element(Tag.valueOf("small"), "")
                                .text("In " + section + " section")
                                .addClass("pr-3"))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // add reading time element
        Optional.ofNullable(article.getTextContent())
                .filter(text -> !text.isEmpty())
                .map(text -> text.split("\\s+"))
                .map(words -> Arrays.stream(words).count())
                .map(count -> Math.max(1, (int) Math.ceil(count / 250.0)))
                .map(minutes -> new Element(Tag.valueOf("small"), "")
                        .text(minutes + " minute read")
                        .addClass("pr-3"))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // return article metadata wrapper element if it has child elements
        return Optional.of(articleMetadataWrapperElement).filter(element -> !element.children().isEmpty());

    }

    /**
     * Attempts to create a title element.
     * @param article Readability4J article
     * @return optional title element
     */
    private Optional<Element> createTitleElement(Article article) {
        return Optional.ofNullable(article.getTitle())
                .filter(title -> !title.isEmpty())
                .map(title ->
                    new Element(Tag.valueOf("h1"), "").text(title).addClass("pb-3")
                );
    }

    /**
     * Attempts to create a excerpt element.
     * @param article Readability4J article
     * @return optional excerpt element
     */
    private Optional<Element> createExcerptElement(Article article) {
        return Optional.ofNullable(article.getExcerpt())
                .filter(excerpt -> !excerpt.isEmpty())
                .map(excerpt ->
                        new Element(Tag.valueOf("p"), "").text(excerpt).addClass("lead")
                );
    }

    /**
     * Attempts to create a site name element.
     * @param document Jsoup document
     * @return optional site name element
     */
    private Optional<Element> createSiteNameElement(Document document) {

        OptionalCandidate<String, String> candidateSiteName = new OptionalCandidate<>(candidate ->
            Optional.ofNullable(candidate).filter(presentCandidate -> !presentCandidate.isEmpty())
        );

        String hostName = "";
        try {
            hostName = new URL(document.baseUri()).getHost();
        } catch (MalformedURLException mue) {
            // do nothing
        }

        return candidateSiteName
                .tryout(hostName)
                .tryout(document
                        .select("head meta[property=og:site_name]")
                        .attr("content"))
                .tryout(document
                        .select("head meta[name=application-name]")
                        .attr("content"))
                .getOptional()
                .map(siteName ->
                    new Element(Tag.valueOf("p"), "").text(siteName).addClass("lead")
                );

    }

}
