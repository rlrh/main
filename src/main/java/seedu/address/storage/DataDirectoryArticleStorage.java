package seedu.address.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base32;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;

/**
 * A class to access UserPrefs stored in the hard disk as a json file
 */
public class DataDirectoryArticleStorage implements ArticleStorage {

    private Path directoryPath;
    private Logger logger = LogsCenter.getLogger(DataDirectoryArticleStorage.class);

    public DataDirectoryArticleStorage(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        return directoryPath;
    }

    @Override
    public void addArticle(String url, byte[] articleContent) throws IOException {
        Path targetPath = getArticlePath(url);

        // Ensure data directory exists
        FileUtil.createDirectory(directoryPath);

        FileUtil.writeToFile(targetPath, articleContent);
    }

    /**
     * Converts the given url to a filename that will be used to write to.
     */
    private String urlToFilename(String url) throws NoSuchAlgorithmException {
        String lowercaseUrl = url.toLowerCase();

        try {
            // We hash the URL with sha-256, truncate it to 128 bits so it's shorter, then encode it in base32
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(lowercaseUrl.getBytes(StandardCharsets.UTF_8));
            byte[] truncatedHash = new byte[16];
            System.arraycopy(encodedHash, 0, truncatedHash, 0, 16);
            byte[] hashInBase32 = new Base32().encode(truncatedHash);
            return new String(hashInBase32, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException nsae) {
            logger.severe("SHA-256 hash not supported on this system. Saving links cannot be done");
            throw nsae;
        }
    }

    public Path getArticlePath(String url) {
        try {
            return directoryPath.resolve(urlToFilename(url));
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
    }

}
