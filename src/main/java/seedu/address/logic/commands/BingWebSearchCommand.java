package seedu.address.logic.commands;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Searches Bing (web search) with the given keywords.
 * Uses Bing's RSS feature.
 */
public class BingWebSearchCommand extends FeedCommand {

    public static final String COMMAND_WORD = "bing";

    public BingWebSearchCommand(String keywords) throws UnsupportedEncodingException, MalformedURLException {
        super(getBingWebSearchRssLink(keywords));
    }

    private static URL getBingWebSearchRssLink(String keywords)
            throws UnsupportedEncodingException, MalformedURLException {
        return new URL(String.format(
            "https://www.bing.com/search?q=%s&format=rss",
            URLEncoder.encode(keywords, "UTF-8")));
    }
}
