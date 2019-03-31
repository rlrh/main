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
                try {
                    String absLink = new URL(baseUrl, link).toExternalForm();
                    e.attr("href", absLink);
                } catch (MalformedURLException mue) {
                    // do nothing because if the URL is malformed,
                    // we simply need not convert the URL.
                }

            } else if (e.hasAttr("src")) {
                String link = e.attr("src");
                try {
                    String absLink = new URL(baseUrl, link).toExternalForm();
                    e.attr("src", absLink);
                } catch (MalformedURLException mue) {
                    // do nothing because if the URL is malformed,
                    // we simply need not convert the URL.
                }
            }

        }

        return document;
    }

}
