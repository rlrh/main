package seedu.address.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEntries.FILE_TEST_CONTENTS;
import static seedu.address.testutil.TypicalEntries.VALID_FILE_LINK;
import static seedu.address.testutil.TypicalEntries.VALID_HTTPS_LINK;
import static seedu.address.testutil.TypicalEntries.VALID_HTTP_LINK;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.MainApp;
import seedu.address.testutil.TestUtil;

public class NetworkTest {
    private static final URL HTTPS_TEST_URL = VALID_HTTPS_LINK.getLink().value;
    private static final URL HTTP_TEST_URL = VALID_HTTP_LINK.getLink().value;
    private static final URL FILE_TEST_URL = VALID_FILE_LINK.getLink().value;
    private static final URL REDIRECTING_URL = TestUtil.toUrl("http://arxiv.org/abs/1904.02379");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void fetchAsStream_followsRedirects() throws IOException, ExecutionException, InterruptedException {
        // Ensure that the REDIRECTING_URL actually is a redirect
        InputStream redirectingContent = Network.fetchAsStreamAsync(REDIRECTING_URL, 0).get();
        byte[] redirectingContentBytes = redirectingContent.readAllBytes();
        assertTrue(redirectingContentBytes.length > 0);
        assertTrue(new String(redirectingContentBytes, StandardCharsets.UTF_8).contains("301 Moved Permanently"));

        // Ensure that if we follow redirects, then we no longer see a redirect error in the content
        InputStream redirectedContent = Network.fetchAsStreamAsync(REDIRECTING_URL).get();
        byte[] redirectedContentBytes = redirectedContent.readAllBytes();
        assertTrue(redirectedContentBytes.length > 0);
        assertFalse(new String(redirectedContentBytes, StandardCharsets.UTF_8).contains("301 Moved Permanently"));
    }

    @Test
    public void fetchAsStream_success() throws IOException {
        InputStream httpsContent = Network.fetchAsStream(HTTPS_TEST_URL);
        byte[] httpsContentBytes = httpsContent.readAllBytes();
        assertTrue(httpsContentBytes.length > 0);
        assertTrue(new String(httpsContentBytes, StandardCharsets.UTF_8).contains("<p>It works!</p>"));

        InputStream httpContent = Network.fetchAsStream(HTTP_TEST_URL);
        assertTrue(httpContent.readAllBytes().length > 0);

        InputStream localContent = Network.fetchAsStream(FILE_TEST_URL);
        byte[] localContentBytes = localContent.readAllBytes();
        assertTrue(localContentBytes.length > 0);
        assertArrayEquals(localContentBytes, FILE_TEST_CONTENTS.getBytes());
    }

    @Test
    public void fetchAsStream_invalidUrl_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        Network.fetchAsStream(TestUtil.toUrl("https://abc.``ILLEGAL_CHARS.com"));
    }

    @Test
    public void fetchAsStream_invalidWebsite_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        Network.fetchAsStream(TestUtil.toUrl("https://thiswebsite.does.not.exist.definitely"));
    }

    @Test
    public void fetchAsString_success() {
        try {
            String httpsContent = Network.fetchAsString(
                TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/"));
            assertTrue(httpsContent.length() > 0);
            assertTrue(httpsContent.contains("<p>It works!</p>"));

            String httpContent = Network.fetchAsString(
                TestUtil.toUrl("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/"));
            assertTrue(httpContent.length() > 0);
            assertTrue(httpsContent.contains("<p>It works!</p>"));

            String localContent = Network.fetchAsString(
                    MainApp.class.getResource("/NetworkTest/default.html"));
            assertTrue(localContent.length() > 0);
            assertEquals(localContent, FILE_TEST_CONTENTS);
        } catch (IOException e) {
            fail("Fetching valid URL failed.");
        }
    }

    @Test
    public void fetchAsString_invalidUrl_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        Network.fetchAsString(TestUtil.toUrl("https://abc.``ILLEGAL_CHARS.com"));
    }

    @Test
    public void fetchAsString_invalidWebsite_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        Network.fetchAsString(TestUtil.toUrl("https://thiswebsite.does.not.exist.definitely"));
    }

    @Test
    public void fetchAsOptionalString_returnsStringOptional() {
        Optional<String> httpsContent = Network.fetchAsOptionalString(
                TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/"));
        assertTrue(httpsContent.isPresent());
        assertTrue(httpsContent.get().contains("<p>It works!</p>"));

        Optional<String> httpContent = Network.fetchAsOptionalString(
                TestUtil.toUrl("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/"));
        assertTrue(httpContent.isPresent());
        assertTrue(httpsContent.get().contains("<p>It works!</p>"));

        Optional<String> localContent = Network.fetchAsOptionalString(
                MainApp.class.getResource("/NetworkTest/default.html"));
        assertTrue(localContent.isPresent());
        assertEquals(localContent.get(), FILE_TEST_CONTENTS);
    }

    @Test
    public void fetchAsOptionalString_invalidUrl_returnsEmptyOptional() {
        assertFalse(Network.fetchAsOptionalString(TestUtil.toUrl("https://abc.``ILLEGAL_CHARS.com")).isPresent());
    }

    @Test
    public void fetchAsOptionalString_invalidWebsite_returnsEmptyOptional() {
        assertFalse(Network.fetchAsOptionalString(
                TestUtil.toUrl("https://thiswebsite.does.not.exist.definitely")).isPresent());
    }

    @Test
    public void fetchArticle_success() throws IOException {
        String httpsContent = new String(Network.fetchArticle(
            TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/")));
        assertTrue(httpsContent.length() > 0);
        assertTrue(httpsContent.contains("<p>It works!</p>"));

        String httpContent = new String(Network.fetchArticle(
            TestUtil.toUrl("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/")));
        assertTrue(httpContent.length() > 0);

        String localContent = new String(Network.fetchArticle(
            MainApp.class.getResource("/NetworkTest/default.html")));
        assertTrue(localContent.length() > 0);
    }

    @Test
    public void fetchAsBytesAsync_success() throws ExecutionException, InterruptedException {
        Network.fetchAsBytesAsync(
            TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/"))
            .thenAccept(content -> {
                String httpsContent = new String(content);
                assertTrue(httpsContent.length() > 0);
                assertTrue(httpsContent.contains("<p>It works!</p>"));
            })
            .get();

        Network.fetchAsBytesAsync(
            TestUtil.toUrl("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/"))
            .thenAccept(content -> {
                String httpContent = new String(content);
                assertTrue(httpContent.length() > 0);
            })
            .get();

        Network.fetchAsBytesAsync(
            MainApp.class.getResource("/NetworkTest/default.html"))
            .thenAccept(content -> {
                String localContent = new String(content);
                assertTrue(localContent.length() > 0);
            })
            .get();
    }

    @Test
    public void fetchAsBytesAsync_invalidWebsite_failsFuture() throws ExecutionException, InterruptedException {
        thrown.expect(ExecutionException.class);
        Network.fetchAsBytesAsync(
            TestUtil.toUrl("https://thiswebsite.does.not.exist.definitely"))
            .get();
    }

    @Test
    public void fetchAsBytesAsync_invalidLocalFile_failsFuture() throws ExecutionException, InterruptedException {
        thrown.expect(ExecutionException.class);
        Network.fetchAsBytesAsync(
            TestUtil.toUrl("file:/example/bob/fake/file"))
            .get();
    }

}
