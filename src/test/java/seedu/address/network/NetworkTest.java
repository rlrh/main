package seedu.address.network;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.network.Network.fetchAsStream;
import static seedu.address.network.Network.fetchAsString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.MainApp;

public class NetworkTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String localTestContents = "<!DOCTYPE html>\n<html>\n</html>\n";

    @Test
    public void fetchAsStream_success() throws IOException {
        InputStream httpsContent = fetchAsStream("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/");
        byte[] httpsContentBytes = httpsContent.readAllBytes();
        assertTrue(httpsContentBytes.length > 0);
        assertTrue(new String(httpsContentBytes, StandardCharsets.UTF_8).contains("<p>It works!</p>"));

        InputStream httpContent = fetchAsStream("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/");
        assertTrue(httpContent.readAllBytes().length > 0);

        InputStream localContent = fetchAsStream(MainApp.class.getResource(
                "/view/NetworkTest/default.html").toExternalForm());
        byte[] localContentBytes = localContent.readAllBytes();
        assertTrue(localContentBytes.length > 0);
        assertArrayEquals(localContentBytes, localTestContents.getBytes());
    }

    @Test
    public void fetchAsStream_invalidUrl_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsStream("https://abc.``ILLEGAL_CHARS.com");
    }

    @Test
    public void fetchAsStream_invalidWebsite_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsStream("https://thiswebsite.does.not.exist.definitely");
    }

    @Test
    public void fetchAsString_success() {
        try {
            String httpsContent = fetchAsString("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/");
            assertTrue(httpsContent.length() > 0);
            assertTrue(httpsContent.contains("<p>It works!</p>"));

            String httpContent = fetchAsString("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/");
            assertTrue(httpContent.length() > 0);

            String localContent = fetchAsString(
                    MainApp.class.getResource("/view/NetworkTest/default.html").toExternalForm());
            assertTrue(localContent.length() > 0);

            assertEquals(localContent, localTestContents);
        } catch (IOException e) {
            fail("Fetching valid URL failed.");
        }
    }

    @Test
    public void fetchAsString_invalidUrl_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsString("https://abc.``ILLEGAL_CHARS.com");
    }

    @Test
    public void fetchAsString_invalidWebsite_throwsIoexception() throws IOException {
        thrown.expect(IOException.class);
        fetchAsString("https://thiswebsite.does.not.exist.definitely");
    }

}
