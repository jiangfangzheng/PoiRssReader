package me.jfz.reader;

import static me.jfz.reader.data.ConstData.rssData;
import static me.jfz.reader.handle.RssDataHandle.deserializeRssData;

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
        Gui.main(args);
    }

    private static void init() {
        logger.info("init()");
        // 反序列化已有的数据
        rssData = deserializeRssData();
    }

}
