package seedu.address.commons.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class UrlUtil {

    public static Optional<URL> fromString(String urlString) {
        return Optional
                .ofNullable(urlString)
                .map(string -> {
                    try {
                        return new URL(string);
                    } catch (MalformedURLException mue) {
                        return null;
                    }
                });
    }

}
