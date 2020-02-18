package me.jfz.reader.thread;

import static me.jfz.reader.RssData.nameAndSyndFeedMap;
import static me.jfz.reader.RssData.nameAndUrl;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;

import javax.swing.JLabel;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/18 0018
 */
public class SubscibeThread extends Thread {
    static Logger logger = LoggerFactory.getLogger(SubscibeThread.class);

    private JLabel label;

    public SubscibeThread(JLabel label) {
        this.label = label;
    }

    @Override
    public void run() {
        logger.info("SubscibeThread() run start.");
        int sum = 0;
        for (Map.Entry<String, String> entry1 : nameAndUrl.entrySet()) {
            String name = entry1.getKey();
            String url = entry1.getValue();
            if (url != null) {
                try {
                    try (XmlReader reader = new XmlReader(new URL(url))) {
                        SyndFeed feed = new SyndFeedInput().build(reader);
                        nameAndSyndFeedMap.put(name, feed);
                        sum += feed.getEntries().size();
                    }
                } catch (Exception e) {
                    logger.error("SubscibeThread() run Exception:", e);
                }
            }
        }
        logger.info("SubscibeThread() run finished.");
        label.setText("订阅项：" + nameAndSyndFeedMap.size() + "   总条数：" + sum);
    }
}
