package seedu.address.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

/**
 * Manager of Network component
 */
public abstract class Network {

    private static final int CONNECTION_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final int READ_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final int REQUEST_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds

    private static final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient(Dsl.config()
        .setConnectTimeout(CONNECTION_TIMEOUT_MILLIS)
        .setReadTimeout(READ_TIMEOUT_MILLIS)
        .setRequestTimeout(REQUEST_TIMEOUT_MILLIS));;

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as an InputStream.
     * @param url
     * @return The input stream containing the content fetched.
     * @throws IOException
     */
    public static InputStream fetchAsStream(URL url) throws IOException {
        return url.openStream();
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a String.
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static String fetchAsString(URL url) throws IOException {
        return new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a byte array
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static byte[] fetchAsBytes(URL url) throws IOException {
        return url.openStream().readAllBytes();
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a byte array
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static CompletableFuture<byte[]> fetchAsBytesAsync(URL url) {
        return asyncHttpClient
            .prepareGet(url.toString())
            .execute()
            .toCompletableFuture()
            .thenApply(Response::getResponseBody)
            .thenApply(String::getBytes);
    }

    /**
     * Fetches the article linked at the URL and returns it,
     * but first pre-processing it by converting all links to absolute form.
     */
    public static Optional<byte[]> fetchArticleOptional(URL url) {
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
    public static byte[] fetchArticle(URL url) throws IOException {
        // Download article content to local storage
        byte[] articleContent = Network.fetchAsBytes(url);
        // Convert all links in article to absolute links
        return AbsoluteUrlDocumentConverter.convert(
                url,
                articleContent);
    }

    /**
     * Fetches the article linked at the URL and returns it,
     * but first pre-processing it by converting all links to absolute form.
     */
    public static CompletableFuture<byte[]> fetchArticleAsync(URL url) {
        return fetchAsBytesAsync(url)
            // Convert all links in article to absolute links
            .thenApply(articleContent -> AbsoluteUrlDocumentConverter.convert(url, articleContent));
    }

}
