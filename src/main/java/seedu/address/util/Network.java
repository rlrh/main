package seedu.address.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

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

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a byte array
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static byte[] fetchAsBytes(String url) throws IOException {
        return new URL(url).openStream().readAllBytes();
    }

    /**
     * Fetches the article linked at the URL and returns it,
     * but first pre-processing it by converting all links to absolute form.
     */
    public static Optional<byte[]> fetchArticleOptional(String url) {
        try {
            return Optional.of(fetchArticle(url));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Fetches the article linked at the URL and returns it,
     * but first pre-processing it by converting all links to absolute form.
     */
    public static byte[] fetchArticle(String url) throws IOException {
        // Download article content to local storage
        byte[] articleContent = Network.fetchAsBytes(url);
        // Convert all links in article to absolute links
        return AbsoluteUrlDocumentConverter.convert(
                new URL(url),
                articleContent);
    }
}
