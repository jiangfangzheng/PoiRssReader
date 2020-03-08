package me.jfz.reader.thread;

import static me.jfz.reader.data.ConstData.DATE_FORMAT;
import static me.jfz.reader.data.RssData.nameAndContentModelsMap;
import static me.jfz.reader.data.RssData.nameAndUrl;

import me.jfz.reader.model.ContentModel;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Date;
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
                    // 从feed源中读取最新文章
                    try (XmlReader reader = new XmlReader(new URL(url))) {
                        SyndFeed feed = new SyndFeedInput().build(reader);
                        Set<ContentModel> contentModels = new TreeSet<>();
                        for (SyndEntry entry : feed.getEntries()) {
                            // 发布时间
                            Date publishedDate = entry.getPublishedDate();
                            String publishedTime = "";
                            if (publishedDate != null) {
                                publishedTime = DATE_FORMAT.format(publishedDate);
                            }
                            // 更新时间
                            Date updatedDate = entry.getUpdatedDate();
                            String updatedTime = "";
                            if (updatedDate != null) {
                                updatedTime = DATE_FORMAT.format(updatedDate);
                            }
                            // 构造文章模型
                            ContentModel feedModel = new ContentModel(name, entry.getTitle(), entry.getLink(),
                                entry.getAuthor(), publishedTime, updatedTime,
                                entry.getDescription() + "~" + entry.getContents());
                            if (!contentModels.contains(feedModel)) {
                                contentModels.add(feedModel);
                            }
                        } Set<ContentModel> oldContentModels = nameAndContentModelsMap.get(name);
                        if (oldContentModels == null) {
                            nameAndContentModelsMap.put(name, contentModels);
                            oldContentModels = contentModels;
                        } else {
                            oldContentModels.addAll(contentModels);
                        }
                        sum += oldContentModels.size();
                    }
                } catch (Exception e) {
                    logger.error("SubscibeThread() run Exception:", e);
                }
            } progressBar.setValue(index);
            label.setText("订阅项：" + nameAndContentModelsMap.size() + "   总条数：" + sum);
        } logger.info("SubscibeThread() run finished.");

        label.setText("订阅项：" + nameAndContentModelsMap.size() + "   总条数：" + sum);
    }
}
