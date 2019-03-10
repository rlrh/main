package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEntries.INVALID_LINK;
import static seedu.address.testutil.TypicalEntries.VALID_LINK;
import static seedu.address.testutil.TypicalEntries.WIKIPEDIA_LINK;
import static seedu.address.testutil.TypicalEntries.WIKIPEDIA_LINK_BASE_URL;

import java.net.URL;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.entry.Entry;

import javax.xml.transform.TransformerException;

public class BrowserPanelTest extends GuiUnitTest {
    private SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();
    private SimpleObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>(ViewMode.BROWSER);
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> {
            browserPanel = new BrowserPanel(selectedPerson, viewMode, success -> {}, failure -> {});
        });
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void displayDefaultPage() {
        // default web page
        assertEquals(BrowserPanel.DEFAULT_PAGE, browserPanelHandle.getLoadedUrl());
    }

    @Test
    public void displayCorrectPage() throws Exception {
        // associated web page of a entry with valid link
        guiRobot.interact(() -> selectedPerson.set(VALID_LINK));
        URL expectedPersonUrl = new URL(VALID_LINK.getLink().value);
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedPersonUrl, browserPanelHandle.getLoadedUrl());
    }

    @Test
    public void displayErrorPage() {
        // associated web page of a entry with invalid link
        guiRobot.interact(() -> selectedPerson.set(INVALID_LINK));
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(BrowserPanel.ERROR_PAGE, browserPanelHandle.getLoadedUrl());
    }

    @Test
    public void displayReader() {

        // load associated web page of a Wikipedia entry
        guiRobot.interact(() -> selectedPerson.set(WIKIPEDIA_LINK));
        waitUntilBrowserLoaded(browserPanelHandle);

        // process loaded content through Crux
        String originalHtml = "";
        try {
            originalHtml = XmlUtil.convertDocumentToString(browserPanel.webEngine.getDocument());
        } catch (TransformerException te) {
            fail();
        }
        String processedHtml = ArticleExtractor.with(WIKIPEDIA_LINK_BASE_URL, originalHtml)
                .extractMetadata()
                .extractContent()
                .article()
                .document
                .outerHtml();
        Element expectedBody = Jsoup.parse(processedHtml).body(); // Get body from Jsoup parse to maintain consistency

        // set reader mode and reload
        guiRobot.interact(() -> viewMode.set(ViewMode.READER));
        waitUntilBrowserLoaded(browserPanelHandle);

        // extract loaded content
        String readerHtml = "";
        try {
            readerHtml = XmlUtil.convertDocumentToString(browserPanel.webEngine.getDocument());
        } catch (TransformerException te) {
            fail();
        }
        Element actualBody = Jsoup.parse(readerHtml).body(); // Get body from Jsoup parse to maintain consistency

        // check actual loaded content is the same as expected processed content
        assertTrue(actualBody.hasSameValue(expectedBody));
    }

}
