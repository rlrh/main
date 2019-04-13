package seedu.address.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Converts all the urls in a document to its absolute equivalent,
 * so it can be saved on disk and used again later.
 */
public abstract class AbsoluteUrlDocumentConverter {

    /**
     * Converts all the urls in a document to its absolute equivalent,
     * so it can be saved on disk and used again later.
     */
    public static byte[] convert(URL baseUrl, byte[] articleContent) {
        Document document = Jsoup.parse(new String(articleContent));
        return convert(baseUrl, document).normalise().toString().getBytes();
    }

    /**
     * Converts all the urls in a document to its absolute equivalent,
     * so it can be saved on disk and used again later.
     */
    private static Document convert(URL baseUrl, Document document) {
        // Convert all href and src attributes
        for (Element e : document.getAllElements()) {

            if (e.hasAttr("href")) {
                String link = e.attr("href");
                e.attr("href", convertUrl(baseUrl, link));
            }

            if (e.hasAttr("src")) {
                String link = e.attr("src");
                e.attr("src", convertUrl(baseUrl, link));
            }

        }

        return document;
    }

    /**
     * Converts a URL to absolute form.
     */
    private static String convertUrl(URL baseUrl, String url) {
        try {
            return new URL(baseUrl, url).toExternalForm();
        } catch (MalformedURLException mue) {
            // Do nothing if URL is malformed, basically don't convert
            return url;
        }
    }

}
