package seedu.address.storage;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.testutil.TestUtil;

public class DataDirectoryArticleStorageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void addArticle_nullDirectoryPath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        DataDirectoryArticleStorage ddas = new DataDirectoryArticleStorage(null);
        ddas.addArticle(TestUtil.toUrl("https://test.com"), "test".getBytes());
    }

    @Test
    public void addArticle_contentSavedAtPathCorrectly() throws IOException {
        DataDirectoryArticleStorage ddas = new DataDirectoryArticleStorage(testFolder.getRoot().toPath());

        // Save a bunch of articles then fetch them. They shouldn't overwrite each other.

        ddas.addArticle(TestUtil.toUrl("https://test.com"), "test1".getBytes());
        ddas.addArticle(TestUtil.toUrl("https://test.com/article"), "test2".getBytes());
        ddas.addArticle(TestUtil.toUrl("https://test.com/article.html"), "test3".getBytes());
        ddas.addArticle(TestUtil.toUrl("https://test.io"), "test4".getBytes());
        ddas.addArticle(TestUtil.toUrl("https://test.io/article"), "test5".getBytes());
        ddas.addArticle(TestUtil.toUrl("https://test.io/article.html"), "test6".getBytes());
        ddas.addArticle(TestUtil.toUrl("http://test.io"), "test7".getBytes());
        ddas.addArticle(TestUtil.toUrl("http://test.io/article"), "test8".getBytes());
        ddas.addArticle(TestUtil.toUrl("http://test.io/article.html"), "test9".getBytes());

        assertFetchSuccess(ddas, "https://test.com", "test1".getBytes());
        assertFetchSuccess(ddas, "https://test.com/article", "test2".getBytes());
        assertFetchSuccess(ddas, "https://test.com/article.html", "test3".getBytes());
        assertFetchSuccess(ddas, "https://test.io", "test4".getBytes());
        assertFetchSuccess(ddas, "https://test.io/article", "test5".getBytes());
        assertFetchSuccess(ddas, "https://test.io/article.html", "test6".getBytes());
        assertFetchSuccess(ddas, "http://test.io", "test7".getBytes());
        assertFetchSuccess(ddas, "http://test.io/article", "test8".getBytes());
        assertFetchSuccess(ddas, "http://test.io/article.html", "test9".getBytes());
    }

    @Test
    public void addArticle_addingTwiceOverwritesContent() throws IOException {
        DataDirectoryArticleStorage ddas = new DataDirectoryArticleStorage(testFolder.getRoot().toPath());

        // If re-adding an article, the content should be overwritten.

        ddas.addArticle(TestUtil.toUrl("https://test.com"), "test1".getBytes());
        assertFetchSuccess(ddas, "https://test.com", "test1".getBytes());

        ddas.addArticle(TestUtil.toUrl("https://test.com"), "test2".getBytes());
        assertFetchSuccess(ddas, "https://test.com", "test2".getBytes());
    }

    @Test
    public void getOfflineLink() throws IOException {
        DataDirectoryArticleStorage ddas = new DataDirectoryArticleStorage(testFolder.getRoot().toPath());

        ddas.addArticle(TestUtil.toUrl("https://test.com"), "test1".getBytes());

        // Test that getting an offline link returns Optional.empty() if article was not added.
        assertTrue(ddas.getOfflineLink(TestUtil.toUrl("https://test.com")).isPresent());
        assertFalse(ddas.getOfflineLink(TestUtil.toUrl("http://test.com")).isPresent());
    }

    /**
     * Checks that the content saved for the URL matches.
     */
    private void assertFetchSuccess(DataDirectoryArticleStorage ddas, String url, byte[] content) throws IOException {
        Path savedLocation = ddas.getArticlePath(TestUtil.toUrl(url));
        assertArrayEquals(Files.readAllBytes(savedLocation), content);
    }
}
