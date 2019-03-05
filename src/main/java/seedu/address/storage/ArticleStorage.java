package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents a storage for articles.
 */
public interface ArticleStorage {

    /**
     * Returns the data directory path where articles are stored.
     */
    Path getArticleDataDirectoryPath();

    /**
     * Saves the given article to the storage.
     * @param articleContent cannot be null.
     * @param url cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void addArticle(String url, byte[] articleContent) throws IOException;

    /**
     * Converts a given url to a Path where the article would be stored.
     */
    Path getArticlePath(String url);
}
