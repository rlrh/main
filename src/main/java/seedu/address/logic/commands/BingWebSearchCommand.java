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

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Searches the web using Bing. "
            + "Parameters: [KEYWORDS]\n"
            + "Example: bing trump";

    private static final String BING_WEB_SEARCH_RSS_LINK = "https://www.bing.com/search?q=%s&format=rss";

    public BingWebSearchCommand(String keywords) throws UnsupportedEncodingException, MalformedURLException {
        super(new URL(String.format(BING_WEB_SEARCH_RSS_LINK, URLEncoder.encode(keywords, "UTF-8"))));
    }
}
