package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

import java.io.*;
import java.net.*;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final URL DEFAULT_PAGE =
            requireNonNull(MainApp.class.getResource(FXML_FILE_FOLDER + "default.html"));
    public static final String SEARCH_PAGE_URL = "https://se-education.org/dummy-search-page/?name=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private boolean isReaderView;
    private Person currentlySelectedPerson;

    @FXML
    private WebView browser;


    public BrowserPanel(ObservableValue<Person> selectedPerson, ObservableValue<Boolean> readerView) {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        // Load person page when selected person changes.
        selectedPerson.addListener((observable, oldValue, newValue) -> {
            this.currentlySelectedPerson = newValue;
            this.reload();
        });

        readerView.addListener((observable, oldValue, newValue) -> {
            this.isReaderView = newValue;
            this.reload();
        });

        loadDefaultPage();
    }

    private void loadPersonPage(Person person) {
        // loadPage(SEARCH_PAGE_URL + person.getName().fullName);
        browser.getEngine().setUserStyleSheetLocation(null);
        System.out.println(browser.getEngine().getUserStyleSheetLocation());
        loadPage(person.getEmail().value);
    }

    private void loadPersonReaderPage(Person person) {
        try {
            File file = new File("data/files/" + person.getName().fullName + ".html");
            if (!file.exists()) {
                throw new IllegalArgumentException("File doesn't exist");
            }
            URL url = new File("data/files/" + person.getName().fullName + ".html").toURI().toURL();
            browser.getEngine().setUserStyleSheetLocation(MainApp.class.getResource("/stylesheets/bootstrap.css").toString());
            System.out.println(browser.getEngine().getUserStyleSheetLocation());
            loadPage(url.toString());
        } catch (Exception e) {
            loadDefaultPage();
        }
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadPage(DEFAULT_PAGE.toExternalForm());
    }

    private void reload() {
        if (this.currentlySelectedPerson == null) {
            loadDefaultPage();
            return;
        }
        if (isReaderView) {
            loadPersonReaderPage(this.currentlySelectedPerson);
        } else {
            loadPersonPage(this.currentlySelectedPerson);
        }
    }

}
