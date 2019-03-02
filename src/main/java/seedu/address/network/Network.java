package seedu.address.network;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.fluent.Request;

/**
 * Manager of Network component
 */
public abstract class Network {
    /**
     * Fetches the resource (i.e. webpage) at url, returning it as an InputStream.
     * @param url
     * @return The input stream containing the content fetched.
     * @throws IOException
     */
    public static InputStream fetchAsStream(String url) throws IOException {
        return Request.Get(url)
                .execute()
                .returnContent()
                .asStream();
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a String.
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static String fetchAsString(String url) throws IOException {
        return Request.Get(url)
                .execute()
                .returnContent()
                .asString();
    }
}
