package seedu.address.network;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import org.asynchttpclient.*;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;

/**
 * A wrapper around the {@link AsyncHttpClient} class from async-http-client.
 */
public abstract class Network {

    private static final int CONNECTION_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final int READ_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final int REQUEST_TIMEOUT_MILLIS = 1000 * 10; // 10 seconds
    private static final long DELAY_ON_EXCEPTION = 300L;

    private static final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient(Dsl.config()
            .setConnectTimeout(CONNECTION_TIMEOUT_MILLIS)
            .setReadTimeout(READ_TIMEOUT_MILLIS)
            .setRequestTimeout(REQUEST_TIMEOUT_MILLIS));;

    /**
     * Asynchronously executes a HTTP GET request to the specified url.
     */
    public static CompletableFuture<String> makeGetRequestAsString(String url) throws CompletionException {
        return asyncHttpClient
                .prepareGet(url)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody)
                .handleAsync(Network::waitOnException);
    }

    /**
     * Asynchronously executes a HTTP GET request to the specified url.
     */
    public static CompletableFuture<InputStream> makeGetRequestAsStream(String url) throws CompletionException {
        return asyncHttpClient
                .prepareGet(url)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBodyAsStream)
                .handleAsync(Network::waitOnException);
    }

    /**
     * Waits for a short period of time in the event of exception.
     * Prevents API methods from completing before necessary pre-processing is completed.
     */
    private static <T> T waitOnException(T result, Throwable error) {
        if (error != null) {
            try {
                Thread.sleep(DELAY_ON_EXCEPTION);
            } catch (InterruptedException ie) {
                //logger.warning(StringUtil.getDetails(ie));
            }
            throw new CompletionException(error);
        }
        return result;
    }

    /**
     * Stops and closes the underlying {@link AsyncHttpClient}.
     */
    public static void close() {
        try {
            if (!asyncHttpClient.isClosed()) {
                asyncHttpClient.close();
            }
        } catch (IOException e) {
            //logger.warning("Failed to shut down AsyncHttpClient.");
        }
    }
}
