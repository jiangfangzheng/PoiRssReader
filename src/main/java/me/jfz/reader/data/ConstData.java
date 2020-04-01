package me.jfz.reader.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/3/8 0008
 */
public final class ConstData {

   public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static RssAllData rssData = new RssAllData();

    private ConstData() {
    }
}
