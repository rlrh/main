package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEntries.INVALID_FILE_LINK;
import static seedu.address.testutil.TypicalEntries.VALID_FILE_LINK;
import static seedu.address.testutil.TypicalEntries.WIKIPEDIA_LINK;

import java.net.URL;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.entry.Entry;
import seedu.address.ui.util.ReaderViewUtil;

public class BrowserPanelTest extends GuiUnitTest {
    private SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();
    private SimpleObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>(new ViewMode());
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> {
            browserPanel = new BrowserPanel(selectedPerson, viewMode);
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
        guiRobot.interact(() -> selectedPerson.set(VALID_FILE_LINK));
        URL expectedPersonUrl = new URL(VALID_FILE_LINK.getLink().value);
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedPersonUrl, browserPanelHandle.getLoadedUrl());
    }

    @Test
    public void displayErrorPage() {
        // associated web page of a entry with invalid link
        guiRobot.interact(() -> selectedPerson.set(INVALID_FILE_LINK));
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(BrowserPanel.ERROR_PAGE, browserPanelHandle.getLoadedUrl());
    }

    @Test
    public void displayReaderView() {

        // load associated web page of a Wikipedia entry
        guiRobot.interact(() -> selectedPerson.set(WIKIPEDIA_LINK));
        waitUntilBrowserLoaded(browserPanelHandle);

        // generate reader view by processing loaded content
        String originalHtml = "";
        try {
            originalHtml = XmlUtil.convertDocumentToString(browserPanelHandle.getDocument());
        } catch (TransformerException te) {
            fail();
        }
        Document originalDoc = Jsoup.parse(originalHtml, WIKIPEDIA_LINK.getLink().value);
        Document doc = ReaderViewUtil.generateReaderViewFrom(originalDoc);
        String expectedText = doc.text();

        // set reader view mode
        guiRobot.interact(() -> viewMode.set(new ViewMode(ViewType.READER)));
        waitUntilBrowserLoaded(browserPanelHandle);

        // extract loaded content
        String readerHtml = "";
        try {
            readerHtml = XmlUtil.convertDocumentToString(browserPanelHandle.getDocument());
        } catch (TransformerException te) {
            fail();
        }
        String actualText = Jsoup.parse(readerHtml).text();

        // check actual loaded content is the same as expected processed content
        assertEquals(expectedText, actualText);

    }

    @Test
    public void displayReaderViewStyle() {

        // load associated web page of a Wikipedia entry
        guiRobot.interact(() -> selectedPerson.set(WIKIPEDIA_LINK));
        waitUntilBrowserLoaded(browserPanelHandle);

        // set reader view mode with specified style
        guiRobot.interact(() -> viewMode.set(new ViewMode(ViewType.READER, ReaderViewStyle.DARK)));
        waitUntilBrowserLoaded(browserPanelHandle);

        // check actual stylesheet is the same as specified stylesheet
        assertEquals(
                ReaderViewStyle.DARK.getStylesheetLocation().toExternalForm(),
                browserPanelHandle.getUserStyleSheetLocation()
        );

    }

}
