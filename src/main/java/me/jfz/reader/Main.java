package me.jfz.reader;

import static me.jfz.reader.data.RssData.deserializeNameAndContentModelsMap;
import static me.jfz.reader.data.RssData.getSubscribeModelMapFromJson;
import static me.jfz.reader.data.RssData.getUrlMapFromSubscribeMap;
import static me.jfz.reader.data.RssData.idAndSubscibeModelMap;
import static me.jfz.reader.data.RssData.nameAndUrl;

import me.jfz.reader.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/18 0018
 */
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("PoiRssReader start...");
        // 预处理
        init();
        // 启动GUI
        MainGui.main(args);
    }

    private static void init() {
        logger.info("init()");

        // 创建临时html目录，存储每篇文章的渲染样式
        FileUtil.mkDirs("./tmpHtml/");

        // 反序列化已有的内容数据
        deserializeNameAndContentModelsMap();

        // 取json中feed重新检查新的订阅
        getSubscribeModelMapFromJson("feedData.json", idAndSubscibeModelMap);

        // SubscibeModel数据转为nameAndUrl Map存储
        getUrlMapFromSubscribeMap(nameAndUrl, idAndSubscibeModelMap);
    }

}
