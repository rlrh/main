package seedu.address.model.entry;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class LinkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Link((URL) null));
        Assert.assertThrows(NullPointerException.class, () -> new Link((String) null));
    }

    @Test
    public void constructor_invalidLink_throwsIllegalArgumentException() {
        // Test invalid links
        Assert.assertThrows(MalformedURLException.class, () -> new Link(""));
        Assert.assertThrows(MalformedURLException.class, () -> new Link("badprotocol://foo.bar/"));
        Assert.assertThrows(MalformedURLException.class, () -> new Link("https://foo.bar:badport/"));
    }

}
