package seedu.address.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import seedu.address.commons.core.LogsCenter;

/**
 * Manager of Network component
 */
public abstract class Network {

    private static final Logger logger = LogsCenter.getLogger(Network.class);

    private static final int DEFAULT_NUM_REDIRECTS = 15;
    private static final int CONNECTION_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final int READ_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final int REQUEST_TIMEOUT_MILLIS = 1000 * 60; // 60 seconds

    private static final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient(Dsl.config()
        .setConnectTimeout(CONNECTION_TIMEOUT_MILLIS)
        .setReadTimeout(READ_TIMEOUT_MILLIS)
        .setRequestTimeout(REQUEST_TIMEOUT_MILLIS));

    /**
     * Fetches the resource (i.e. webpage) at url asynchronously,
     * redirecting at most maxRedirect times,
     * returning it as a Response
     */
    private static CompletableFuture<Response> fetchAsResponseAsync(URL url, int maxRedirects) {
        logger.info("Initiating network response to fetch: " + url + " with at most " + maxRedirects + " redirects");
        return asyncHttpClient.prepareGet(url.toString())
            .execute()
            .toCompletableFuture()
            .thenCompose(response -> {
                if (maxRedirects > 0) {
                    switch (response.getStatusCode()) {
                    case 301:
                    case 302:
                    case 303:
                    case 307:
                    case 308:
                        // HTTP Redirect
                        try {
                            URL newUrl = new URL(url, response.getHeader("Location"));
                            logger.info("While fetching " + url + ", we got redirected to " + newUrl);
                            return fetchAsResponseAsync(newUrl, maxRedirects - 1);
                        } catch (MalformedURLException mue) {
                            // If the redirect was invalid, just give up
                            break;
                        }
                    default:
                        break;
                    }
                }
                logger.info("Successfully fetched " + url);
                return CompletableFuture.completedFuture(response);
            });
    }

    /**
     * Fetches the resource (i.e. webpage) at url asynchronously,
     * returning it as a Response
     */
    private static CompletableFuture<Response> fetchAsResponseAsync(URL url) {
        return fetchAsResponseAsync(url, DEFAULT_NUM_REDIRECTS);
    }

    /**
     * Fetches the resource (i.e. webpage) at url asynchronously, returning it as an InputStream.
     */
    public static CompletableFuture<InputStream> fetchAsStreamAsync(URL url, int maxRedirects) {
        if (!url.getProtocol().equals("http")
            && !url.getProtocol().equals("https")) {
            // Fallback to direct java API
            try {
                return CompletableFuture.completedFuture(url.openStream());
            } catch (IOException ioe) {
                return CompletableFuture.failedFuture(ioe);
            }
        }
        return fetchAsResponseAsync(url, maxRedirects).thenApply(Response::getResponseBodyAsStream);
    }

    /**
     * Fetches the resource (i.e. webpage) at url asynchronously, returning it as an InputStream.
     */
    public static CompletableFuture<InputStream> fetchAsStreamAsync(URL url) {
        return fetchAsStreamAsync(url, DEFAULT_NUM_REDIRECTS);
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as an InputStream.
     * @param url
     * @return The input stream containing the content fetched.
     * @throws IOException
     */
    public static InputStream fetchAsStream(URL url) throws IOException {
        try {
            return fetchAsStreamAsync(url).get();
        } catch (ExecutionException | InterruptedException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw new IOException(e.getCause());
            }
        }
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a byte array
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static byte[] fetchAsBytes(URL url) throws IOException {
        return fetchAsStream(url).readAllBytes();
    }

    /**
     * Fetches the resource (i.e. webpage) at url, returning it as a String.
     * @param url
     * @return The content fetched.
     * @throws IOException
     */
    public static String fetchAsString(URL url) throws IOException {
        return new String(fetchAsBytes(url));
    }

    /**
     * Fetches the resource (i.e. webpage) at url asynchronously, returning it as a byte array
     * @param url
     * @return The CompleteableFuture that completes with the resource content
     */
    public static CompletableFuture<byte[]> fetchAsBytesAsync(URL url) {
        return fetchAsStreamAsync(url)
            .thenCompose(stream -> {
                try {
                    return CompletableFuture.completedFuture(stream.readAllBytes());
                } catch (IOException ioe) {
                    return CompletableFuture.failedFuture(ioe);
                }
            });
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
     * Asynchronously fetches the article linked at the URL and returns it as a CompleteableFuture,
     * but first pre-processing it by converting all links to absolute form.
     */
    public static CompletableFuture<byte[]> fetchArticleAsync(URL url) {
        return fetchAsBytesAsync(url)
            // Convert all links in article to absolute links
            .thenApply(articleContent -> AbsoluteUrlDocumentConverter.convert(url, articleContent));
    }

    /** Cleans up by closing the AsyncHttpClient. */
    public static void stop() throws IOException {
        asyncHttpClient.close();
    }
}
