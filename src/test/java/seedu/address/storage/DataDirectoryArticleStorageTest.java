package seedu.address.storage;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class DataDirectoryArticleStorageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void addArticle_nullDirectoryPath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        DataDirectoryArticleStorage ddas = new DataDirectoryArticleStorage(null);
        ddas.addArticle("https://test.com", "test".getBytes());
    }

    @Test
    public void addArticle_contentSavedAtPathCorrectly() throws IOException {
        DataDirectoryArticleStorage ddas = new DataDirectoryArticleStorage(testFolder.getRoot().toPath());

        // Save a bunch of articles then fetch them. They shouldn't overwrite each other.

        ddas.addArticle("https://test.com", "test1".getBytes());
        ddas.addArticle("https://test.com/article", "test2".getBytes());
        ddas.addArticle("https://test.com/article.html", "test3".getBytes());
        ddas.addArticle("https://test.io", "test4".getBytes());
        ddas.addArticle("https://test.io/article", "test5".getBytes());
        ddas.addArticle("https://test.io/article.html", "test6".getBytes());
        ddas.addArticle("http://test.io", "test7".getBytes());
        ddas.addArticle("http://test.io/article", "test8".getBytes());
        ddas.addArticle("http://test.io/article.html", "test9".getBytes());

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

        // Save a bunch of articles then fetch them. They shouldn't overwrite each other.

        ddas.addArticle("https://test.com", "test1".getBytes());
        assertFetchSuccess(ddas, "https://test.com", "test1".getBytes());

        ddas.addArticle("https://test.com", "test2".getBytes());
        assertFetchSuccess(ddas, "https://test.com", "test2".getBytes());
    }

    /**
     * Checks that the content saved for the URL matches.
     */
    private void assertFetchSuccess(DataDirectoryArticleStorage ddas, String url, byte[] content) throws IOException {
        Path savedLocation = ddas.getArticlePath(url);
        assertArrayEquals(Files.readAllBytes(savedLocation), content);
    }
}
