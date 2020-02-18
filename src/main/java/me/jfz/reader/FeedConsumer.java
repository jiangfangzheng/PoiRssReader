package me.jfz.reader;


import java.net.URL;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;


/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/17 0017
 */
public class FeedConsumer {
    public static void main(String[] args) {

        try {
            String url = "http://feed.cnblogs.com/blog/u/72021/rss/";
            try (XmlReader reader = new XmlReader(new URL(url))) {
                SyndFeed feed = new SyndFeedInput().build(reader);
                System.out.println(feed.getTitle());
                System.out.println("***********************************");
                for (SyndEntry entry : feed.getEntries()) {
                    System.out.println(entry);
                    System.out.println("***********************************");
                }
                System.out.println("Done");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}