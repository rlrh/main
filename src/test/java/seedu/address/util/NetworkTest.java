package seedu.address.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEntries.FILE_TEST_CONTENTS;
import static seedu.address.testutil.TypicalEntries.VALID_FILE_LINK;
import static seedu.address.testutil.TypicalEntries.VALID_HTTPS_LINK;
import static seedu.address.testutil.TypicalEntries.VALID_HTTP_LINK;
import static seedu.address.util.Network.fetchAsStream;
import static seedu.address.util.Network.fetchAsString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.MainApp;
import seedu.address.testutil.TestUtil;

public class NetworkTest {
    private static final URL HTTPS_TEST_URL = VALID_HTTPS_LINK.getLink().value;
    private static final URL HTTP_TEST_URL = VALID_HTTP_LINK.getLink().value;
    private static final URL FILE_TEST_URL = VALID_FILE_LINK.getLink().value;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void fetchAsStream_success() throws IOException {
        InputStream httpsContent = fetchAsStream(HTTPS_TEST_URL);
        byte[] httpsContentBytes = httpsContent.readAllBytes();
        assertTrue(httpsContentBytes.length > 0);
        assertTrue(new String(httpsContentBytes, StandardCharsets.UTF_8).contains("<p>It works!</p>"));

        InputStream httpContent = fetchAsStream(HTTP_TEST_URL);
        assertTrue(httpContent.readAllBytes().length > 0);

        InputStream localContent = fetchAsStream(FILE_TEST_URL);
        byte[] localContentBytes = localContent.readAllBytes();
        assertTrue(localContentBytes.length > 0);
        assertArrayEquals(localContentBytes, FILE_TEST_CONTENTS.getBytes());
    }

    @Test
    public void fetchAsStream_invalidUrl_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsStream(TestUtil.toUrl("https://abc.``ILLEGAL_CHARS.com"));
    }

    @Test
    public void fetchAsStream_invalidWebsite_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsStream(TestUtil.toUrl("https://thiswebsite.does.not.exist.definitely"));
    }

    @Test
    public void fetchAsString_success() {
        try {
            String httpsContent = fetchAsString(
                TestUtil.toUrl("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/"));
            assertTrue(httpsContent.length() > 0);
            assertTrue(httpsContent.contains("<p>It works!</p>"));

            String httpContent = fetchAsString(
                TestUtil.toUrl("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/"));
            assertTrue(httpContent.length() > 0);

            String localContent = fetchAsString(
                    TestUtil.toUrl(MainApp.class.getResource("/NetworkTest/default.html").toExternalForm()));
            assertTrue(localContent.length() > 0);

            assertEquals(localContent, FILE_TEST_CONTENTS);
        } catch (IOException e) {
            fail("Fetching valid URL failed.");
        }
    }

    @Test
    public void fetchAsString_invalidUrl_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsString(TestUtil.toUrl("https://abc.``ILLEGAL_CHARS.com"));
    }

    @Test
    public void fetchAsString_invalidWebsite_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsString(TestUtil.toUrl("https://thiswebsite.does.not.exist.definitely"));
    }

}
