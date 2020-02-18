package me.jfz.reader;

import me.jfz.reader.thread.SubscibeThread;

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
        // 启动GUI
        MainGui.main(args);
        // 预处理
        init();
    }

    private static void init() {
        logger.info("init()");
        File dir = new File("./tmpHtml/");
        if(!dir.exists()){
            boolean b = dir.mkdirs();
            logger.warn("创建目录tmpHtml：{}", b);
        }
    }

}
