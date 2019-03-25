package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.Locale;

import seedu.address.MainApp;

/**
 * Available reader view styles.
 */
public enum ReaderViewStyle {
    DEFAULT(requireNonNull(MainApp.class.getResource(Constants.STYLESHEETS_FILE_FOLDER + "default.css"))),
    SEPIA(requireNonNull(MainApp.class.getResource(Constants.STYLESHEETS_FILE_FOLDER + "sepia.css"))),
    DARK(requireNonNull(MainApp.class.getResource(Constants.STYLESHEETS_FILE_FOLDER + "dark.css"))),
    BLACK(requireNonNull(MainApp.class.getResource(Constants.STYLESHEETS_FILE_FOLDER + "black.css")));

    private URL stylesheetLocation;

    ReaderViewStyle(URL stylesheetLocation) {
        this.stylesheetLocation = stylesheetLocation;
    }

    public URL getStylesheetLocation() {
        return stylesheetLocation;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ENGLISH);
    }

    /**
     * Constants to be used by ReaderViewStyle
     */
    private static class Constants {
        private static final String STYLESHEETS_FILE_FOLDER = "/stylesheets/";
    }
}
