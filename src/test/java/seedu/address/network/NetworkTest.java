package seedu.address.network;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.network.Network.fetchAsStream;
import static seedu.address.network.Network.fetchAsString;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.MainApp;

public class NetworkTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void fetchAsStream_success() throws IOException {
        InputStream httpsContent = fetchAsStream("https://se-education.org/");
        assertTrue(httpsContent.readAllBytes().length > 0);

        InputStream httpContent = fetchAsStream("http://se-education.org/");
        assertTrue(httpContent.readAllBytes().length > 0);

        InputStream localContent = fetchAsStream("file://"
            + MainApp.class.getResource("/view/BrowserPanelTest/default.html").toExternalForm().substring(5));
        assertTrue(localContent.readAllBytes().length > 0);
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
            String s = fetchAsString("https://se-education.org/");
            assertTrue(s.length() > 0);
            String httpsContent = fetchAsString("https://se-education.org/");
            assertTrue(httpsContent.length() > 0);

            String httpContent = fetchAsString("http://se-education.org/");
            assertTrue(httpContent.length() > 0);

            String localContent = fetchAsString("file://"
                + MainApp.class.getResource("/view/BrowserPanelTest/default.html").toExternalForm().substring(5));
            assertTrue(localContent.length() > 0);
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
