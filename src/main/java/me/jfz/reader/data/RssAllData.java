package me.jfz.reader.data;

import me.jfz.reader.model.ContentModel;
import me.jfz.reader.model.FeedModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/3/29 0029
 */
public class RssAllData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> infoMap;

    private Map<String, Set<String>> groupMap;

    private Map<String, FeedModel> feedMap;

    private Map<String, Set<ContentModel>> contentMap;

    public RssAllData() {
        this.infoMap = new HashMap<>();
        this.groupMap = new TreeMap<>();
        this.feedMap = new HashMap<>();
        this.contentMap = new HashMap<>();
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }

    public Map<String, Set<String>> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, Set<String>> groupMap) {
        this.groupMap = groupMap;
    }

    public Map<String, FeedModel> getFeedMap() {
        return feedMap;
    }

    public void setFeedMap(Map<String, FeedModel> feedMap) {
        this.feedMap = feedMap;
    }

    public Map<String, Set<ContentModel>> getContentMap() {
        return contentMap;
    }

    public void setContentMap(Map<String, Set<ContentModel>> contentMap) {
        this.contentMap = contentMap;
    }
}
