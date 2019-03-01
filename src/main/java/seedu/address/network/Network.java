package seedu.address.network;


import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.http.client.fluent.Request;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;

/**
 * Manager of Network component
 */
public abstract class Network {
    private static final Logger logger = LogsCenter.getLogger(Network.class);

    public static Optional<InputStream> fetchAsStream(String url) {
        try {
            return Optional.of(Request.Get(url)
                    .execute()
                    .returnContent()
                    .asStream());
        } catch (IOException e) {
            logger.warning(StringUtil.getDetails(e));
            return Optional.empty();
        }
    }

    public static String fetch(String url) {
        try {
            return Request.Get(url)
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            logger.warning(StringUtil.getDetails(e));
            return "HTTP ERROR PAGE OR SOMETHING " + e;
        }
    }
}
