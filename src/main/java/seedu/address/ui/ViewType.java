package seedu.address.ui;

import java.util.Locale;

/**
 * View types available.
 */
public enum ViewType {
    READER, BROWSER;

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ENGLISH);
    }
}
