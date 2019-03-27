package seedu.address.logic.commands;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BingWebSearchCommand extends FeedCommand {

    public static final String COMMAND_WORD = "bing";

    public BingWebSearchCommand(String keywords) throws UnsupportedEncodingException {
        super(getBingWebSearchRssLink(keywords));
    }

    private static String getBingWebSearchRssLink(String keywords) throws UnsupportedEncodingException {
        return String.format("https://www.bing.com/search?q=%s&format=rss", URLEncoder.encode(keywords, "UTF-8"));
    }
}
