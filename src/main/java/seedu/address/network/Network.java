package seedu.address.network;


import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.fluent.Request;

/**
 * Manager of Network component
 */
public abstract class Network {
    public static InputStream fetchAsStream(String url) throws IOException {
        return Request.Get(url)
                .execute()
                .returnContent()
                .asStream();
    }

    public static String fetchAsString(String url) throws IOException {
        return Request.Get(url)
                .execute()
                .returnContent()
                .asString();
    }
}
