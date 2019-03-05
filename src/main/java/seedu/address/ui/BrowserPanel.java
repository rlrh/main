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
import seedu.address.commons.util.XmlUtil;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.entry.Entry;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;

import javax.xml.transform.TransformerException;

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

    private String currentLocation;
    private boolean isCurrentlyNonBrowserView;
    private ViewMode viewMode;

    public BrowserPanel(ObservableValue<Entry> selectedPerson, ObservableValue<ViewMode> viewMode) {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        this.currentLocation = "";
        this.viewMode = viewMode.getValue();
        this.isCurrentlyNonBrowserView = false;

        // Load entry page when selected entry changes.
        selectedPerson.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                loadDefaultPage();
                return;
            }
            loadPersonPage(newValue);
        });

        viewMode.addListener((observable, oldViewMode, newViewMode) -> {
            this.viewMode = newViewMode;
            reload();
        });

        /*
         * Load reader view if view mode is not browser view but non-browser view is not showing
         * Load error page if error occurs.
         */
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
            case RUNNING:
                if (this.isCurrentlyNonBrowserView) {
                    logger.info(String.format("Loading non-browser view for %s...", this.currentLocation));
                } else {
                    logger.info(String.format("Loading %s...", this.currentLocation));
                }
                break;
            case SUCCEEDED:
                if (this.isCurrentlyNonBrowserView) {
                    logger.info(String.format("Successfully loaded non-browser view for %s", this.currentLocation));
                } else {
                    logger.info(String.format("Successfully loaded %s", this.currentLocation));
                }

                if (!this.viewMode.equals(ViewMode.BROWSER) && !this.isCurrentlyNonBrowserView) {
                    loadReaderPage(this.currentLocation);
                }
                break;
            case FAILED:
                logger.warning(String.format("Failed to load %s", this.currentLocation));
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

    private void loadPage(String url) {
        this.isCurrentlyNonBrowserView = false;
        this.currentLocation = url;
        Platform.runLater(() -> webEngine.load(url));
    }

    /**
     * Loads reader view of current content.
     * Assumes original page is already loaded.
     * @param url base url
     */
    private void loadReaderPage(String url) {
        this.isCurrentlyNonBrowserView = true;
        this.currentLocation = url;
        if (url.equals(DEFAULT_PAGE.toExternalForm()) || url.equals(ERROR_PAGE.toExternalForm())) {
            return;
        }
        try {
            String rawHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
            Article article = ArticleExtractor.with(url, rawHtml)
                    .extractMetadata()
                    .extractContent()
                    .article();
            String cleanHtml = article.document.outerHtml();
            Platform.runLater(() -> webEngine.loadContent(cleanHtml));
        } catch (TransformerException te) {
            logger.warning(String.format("Failed to load reader view for %s", this.currentLocation));
        }
    }

    /**
     * Reloads the current content.
     * Assumes original page is already loaded.
     */
    private void reload() {
        switch (this.viewMode) {
        case BROWSER:
            loadPage(this.currentLocation);
            break;
        case READER:
            loadReaderPage(this.currentLocation);
            break;
        default:
            break;
        }
    }

}
