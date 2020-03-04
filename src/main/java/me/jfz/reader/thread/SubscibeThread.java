package me.jfz.reader.thread;

import static me.jfz.reader.RssData.nameAndContentModelsMap;
import static me.jfz.reader.RssData.nameAndUrl;

import me.jfz.reader.model.ContentModel;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/18 0018
 */
public class SubscibeThread extends Thread {
    static Logger logger = LoggerFactory.getLogger(SubscibeThread.class);

    private JComponent[] jComponents;

    public SubscibeThread(JComponent[] jComponents) {
        this.jComponents = jComponents;
    }

    @Override
    public void run() {
        logger.info("SubscibeThread() run start.");
        JLabel label = (JLabel) jComponents[0];
        JProgressBar progressBar = (JProgressBar) jComponents[1];
        progressBar.setMinimum(0);
        progressBar.setMaximum(nameAndUrl.size());
        progressBar.setValue(0);

        int index = 0;
        int sum = 0;
        for (Map.Entry<String, String> entry1 : nameAndUrl.entrySet()) {
            index++;
            String name = entry1.getKey();
            String url = entry1.getValue();
            if (url != null) {
                try {
                    try (XmlReader reader = new XmlReader(new URL(url))) {
                        SyndFeed feed = new SyndFeedInput().build(reader);
                        List<ContentModel> contentModels = new ArrayList<>();
                        for (SyndEntry entry : feed.getEntries()) {
                            ContentModel feedModel = new ContentModel(name,  entry.getTitle(),entry.getLink(),entry.getAuthor(), entry.getPublishedDate() + "",  entry.getDescription()+ "~"+entry.getContents() );
                           if (!contentModels.contains(feedModel)) {
                               contentModels.add(feedModel);
                           }
                        }
                        nameAndContentModelsMap.put(name, contentModels);
                        sum += feed.getEntries().size();
                    }
                } catch (Exception e) {
                    logger.error("SubscibeThread() run Exception:", e);
                }
            }
            progressBar.setValue(index);
            label.setText("订阅项：" + nameAndContentModelsMap.size() + "   总条数：" + sum);
        }
        logger.info("SubscibeThread() run finished.");

        label.setText("订阅项：" + nameAndContentModelsMap.size() + "   总条数：" + sum);
    }
}
