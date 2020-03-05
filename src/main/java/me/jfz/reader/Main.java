package me.jfz.reader;

import static me.jfz.reader.RssData.deserializeNameAndContentModelsMap;
import static me.jfz.reader.RssData.getSubscribeModelMapFromJson;
import static me.jfz.reader.RssData.getUrlMapFromSubscribeMap;
import static me.jfz.reader.RssData.idAndSubscibeModelMap;
import static me.jfz.reader.RssData.nameAndUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
        File dir = new File("./tmpHtml/");
        if (!dir.exists()) {
            boolean b = dir.mkdirs();
            logger.warn("创建目录tmpHtml：{}", b);
        }

        // 反序列化已有的内容数据
        deserializeNameAndContentModelsMap();

        // 取json中feed重新检查新的订阅
        getSubscribeModelMapFromJson(idAndSubscibeModelMap);

        // SubscibeModel数据转为nameAndUrl Map存储
        getUrlMapFromSubscribeMap(nameAndUrl, idAndSubscibeModelMap);
    }

}
