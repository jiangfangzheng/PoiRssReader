package me.jfz.reader;

import me.jfz.reader.model.SubscibeModel;

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
        nameAndUrl.put("福利吧", "https://fulibus.net/feed");
        nameAndUrl.put("阮一峰", "http://www.ruanyifeng.com/blog/atom.xml");
        nameAndUrl.put("ghpym", "https://www.ghpym.com/feed");
        nameAndUrl.put("异想萌域", "http://poi.ooo/index.xml");
        nameAndUrl.put("landiannews", "https://www.landiannews.com/feed");
        nameAndUrl.put("Sandeepin博客园", "http://feed.cnblogs.com/blog/u/72021/rss/");
    }

    public static Map<String, SyndFeed> nameAndSyndFeedMap = new HashMap<>(128);

    public static Map<String, SubscibeModel> idAndSubscibeModelMap = new HashMap<>(128);

    static {
        SubscibeModel subscibeModelType1 = new SubscibeModel("我的博客", "", 0, "root");
        SubscibeModel subscibeModelType2 = new SubscibeModel("科技IT", "", 0, "root");
        SubscibeModel subscibeModelType3 = new SubscibeModel("其它收藏", "", 0, "root");

        SubscibeModel subscibeModel1 = new SubscibeModel("Sandeepin博客园", "http://feed.cnblogs.com/blog/u/72021/rss/", 1, subscibeModelType1.getId());
        SubscibeModel subscibeModel2 = new SubscibeModel("异想萌域", "http://poi.ooo/index.xml", 1, subscibeModelType1.getId());

        SubscibeModel subscibeModel3 = new SubscibeModel("landiannews", "https://www.landiannews.com/feed", 1, subscibeModelType2.getId());
        SubscibeModel subscibeModel4 = new SubscibeModel("ghpym", "https://www.ghpym.com/feed", 1, subscibeModelType2.getId());
        SubscibeModel subscibeModel5 = new SubscibeModel("阮一峰", "http://www.ruanyifeng.com/blog/atom.xml", 1, subscibeModelType2.getId());

        SubscibeModel subscibeModel6 = new SubscibeModel("福利吧", "https://fulibus.net/feed", 1, subscibeModelType3.getId());

        idAndSubscibeModelMap.putIfAbsent(subscibeModelType1.getId(), subscibeModelType1);
        idAndSubscibeModelMap.putIfAbsent(subscibeModelType2.getId(), subscibeModelType2);
        idAndSubscibeModelMap.putIfAbsent(subscibeModelType3.getId(), subscibeModelType3);

        idAndSubscibeModelMap.putIfAbsent(subscibeModel1.getId(), subscibeModel1);
        idAndSubscibeModelMap.putIfAbsent(subscibeModel2.getId(), subscibeModel2);
        idAndSubscibeModelMap.putIfAbsent(subscibeModel3.getId(), subscibeModel3);
        idAndSubscibeModelMap.putIfAbsent(subscibeModel4.getId(), subscibeModel4);
        idAndSubscibeModelMap.putIfAbsent(subscibeModel5.getId(), subscibeModel5);
        idAndSubscibeModelMap.putIfAbsent(subscibeModel6.getId(), subscibeModel6);
    }

}
