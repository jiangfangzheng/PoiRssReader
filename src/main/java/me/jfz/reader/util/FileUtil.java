package me.jfz.reader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/3/8 0008
 */
public final class FileUtil {

    static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    public static void mkDirs(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            boolean b = dir.mkdirs();
            logger.warn("创建目录tmpHtml：{}", b);
        }
    }
}
