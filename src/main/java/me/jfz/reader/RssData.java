package me.jfz.reader;

import com.rometools.rome.feed.synd.SyndFeed;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/18 0018
 */
public class RssData {

    public static Map<String, String> nameAndUrl = new HashMap<>(128);

    static {
        nameAndUrl.put("football", "https://fulibus.net/feed");
        nameAndUrl.put("hockey", "http://www.ruanyifeng.com/blog/atom.xml");
        nameAndUrl.put("hot dogs", "https://www.ghpym.com/feed");
        nameAndUrl.put("pizza", "http://poi.ooo/index.xml");
        nameAndUrl.put("ravioli", "https://www.landiannews.com/feed");
        nameAndUrl.put("bananas", "http://feed.cnblogs.com/blog/u/72021/rss/");
    }

    public static Map<String, String> titleAndContentMap = new HashMap<>(128);

    public static Map<String, SyndFeed> nameAndSyndFeedMap = new HashMap<>(128);
}
