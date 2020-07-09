package me.jfz.reader.handle;

import static me.jfz.reader.data.ConstData.DATE_FORMAT;
import static me.jfz.reader.data.ConstData.rssData;

import me.jfz.reader.data.RssAllData;
import me.jfz.reader.model.ContentModel;
import me.jfz.reader.model.FeedModel;
import me.jfz.reader.util.SerializeUtil;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020 /3/28 0028
 */
public class RssDataHandle {

    /**
     * The Logger.
     */
    static Logger logger = LoggerFactory.getLogger(RssDataHandle.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        RssAllData rssAllData = new RssAllData();
        Set<String> set = new HashSet<>();
        set.add("1");
        rssAllData.getGroupMap().put("IT", set);
        Set<String> set2 = new HashSet<>();
        set2.add("2");
        rssAllData.getGroupMap().put("娱乐", set2);

        rssAllData.getFeedMap()
            .put("1",
                new FeedModel("1", "阮一峰", "http://www.ruanyifeng.com/blog/", "http://www.ruanyifeng.com/blog/atom.xml","IT"));
        rssAllData.getFeedMap()
            .put("2",
                new FeedModel("2", "福利吧", "https://fulibus.net/", "https://fulibus.net/feed",
                    "娱乐"));
        serializeRssData(rssAllData);

        // 刷新
        // RssAllData rssAllData = deserializeRssData();
        // Map<String, Set<ContentModel>> contentMap = rssAllData.getContentMap();
        // for (Set<ContentModel> contentModelSet : contentMap.values()) {
        //     for (ContentModel c : contentModelSet) {
        //         logger.warn("标题：{}", c.getTitle());
        //     }
        // }
        // refreshFeed(rssAllData);
        // logger.warn("刷新后");
        // contentMap = rssAllData.getContentMap();
        // for (Set<ContentModel> contentModelSet : contentMap.values()) {
        //     for (ContentModel c : contentModelSet) {
        //         logger.warn("标题：{}", c.getTitle());
        //     }
        // }
        // serializeRssData(rssAllData);

    }

    /**
     * 序列化RssData
     *
     * @param rssData the rss data
     */
    public static void serializeRssData(RssAllData rssData) {
        SerializeUtil.serializeFast("rssData.dat", rssData);
    }

    /**
     * 反序列化RssData
     *
     * @return rss all data
     */
    public static RssAllData deserializeRssData() {
        RssAllData rssAllData = (RssAllData) SerializeUtil.deserializeFast("rssData.dat");
        return rssAllData;
    }

    /**
     * 刷新feed.
     *
     * @param rssAllData the rss all data
     */
    public static void refreshFeed(RssAllData rssAllData) {
        logger.info("refreshFeed() run start...");
        Map<String, FeedModel> feedMap = rssAllData.getFeedMap();
        logger.info("订阅Feed个数：{}", feedMap.size());
        int i = 0;
        for (FeedModel feedModel : feedMap.values()) {
            String feedId = feedModel.getId();
            String url = feedModel.getUrl();
            if (url != null) {
                try {
                    // 从feed源中读取最新文章
                    try (XmlReader reader = new XmlReader(new URL(url))) {
                        SyndFeed feed = new SyndFeedInput().build(reader);
                        Set<ContentModel> contentModels = new TreeSet<>();
                        for (SyndEntry entry : feed.getEntries()) {
                            addContentModelData(feedId, contentModels, entry);
                        }
                        Map<String, Set<ContentModel>> contentMap = rssAllData.getContentMap();
                        Set<ContentModel> oldContentModels = contentMap.get(feedId);
                        if (oldContentModels == null) {
                            contentMap.put(feedId, contentModels);
                        } else {
                            oldContentModels.addAll(contentModels);
                        }
                        logger.info("刷新第 {} 个Feed，文章数：{}", ++i, contentModels.size());
                    }
                } catch (Exception e) {
                    logger.error("refreshFeed() run Exception:", e);
                }
            }
        }
        logger.info("refreshFeed() run finished.");
    }

    public static Set<ContentModel> getAllContents(RssAllData rssAllData) {
        Set<ContentModel> contentModels = new TreeSet<>();
        Map<String, Set<ContentModel>> contentMap = rssAllData.getContentMap();
        for (Set<ContentModel> contentModelSet : contentMap.values()) {
            contentModels.addAll(contentModelSet);
        }
        return contentModels;
    }

    public static Set<ContentModel> getStarContents(RssAllData rssAllData, boolean isStar) {
        Set<ContentModel> contentModels = new TreeSet<>();
        Map<String, Set<ContentModel>> contentMap = rssAllData.getContentMap();
        for (Set<ContentModel> contentModelSet : contentMap.values()) {
            for (ContentModel contentModel : contentModelSet) {
                if (isStar == contentModel.isStar()) {
                    contentModels.add(contentModel);
                }
            }
        }
        return contentModels;
    }

    public static Set<ContentModel> getReadContents(RssAllData rssAllData, boolean isRead) {
        Set<ContentModel> contentModels = new TreeSet<>();
        Map<String, Set<ContentModel>> contentMap = rssAllData.getContentMap();
        for (Set<ContentModel> contentModelSet : contentMap.values()) {
            for (ContentModel contentModel : contentModelSet) {
                if (isRead == contentModel.isRead()) {
                    contentModels.add(contentModel);
                }
            }
        }
        return contentModels;
    }

    public static int allSubscribeCount(RssAllData rssAllData) {
        return getAllContents(rssAllData).size();
    }
    public static int unreadSubscribeCount(RssAllData rssAllData) {
        return getReadContents(rssAllData, false).size();
    }
    public static int unstarSubscribeCount(RssAllData rssAllData){
        return getStarContents(rssAllData, true).size();
    }

    private static void addContentModelData(String feedId, Set<ContentModel> contentModels, SyndEntry entry) {
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
        String content = "";
        if (entry.getContents() != null && !entry.getContents().isEmpty() && entry.getContents().get(0) != null
            && entry.getContents().get(0).getValue() != null) {
            content = entry.getContents().get(0).getValue();
        }
        if ("".equals(content) && entry.getDescription() != null && entry.getDescription().getValue() != null) {
            content = entry.getDescription().getValue();
        }

        ContentModel contentModel = new ContentModel(feedId, entry.getTitle(), entry.getLink(), entry.getAuthor(),
            publishedTime, updatedTime, content);
        if (!contentModels.contains(contentModel)) {
            contentModels.add(contentModel);
        }
    }

}
