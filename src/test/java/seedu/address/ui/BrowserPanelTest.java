package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalEntries.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.model.entry.Entry;

public class BrowserPanelTest extends GuiUnitTest {
    private SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> browserPanel = new BrowserPanel(selectedPerson));
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

}
