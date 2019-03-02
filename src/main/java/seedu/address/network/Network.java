package seedu.address.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
        return new URL(url).openStream();
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a String.
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static String fetchAsString(String url) throws IOException {
        return new String(new URL(url).openStream().readAllBytes(), "utf-8");
    }
}
