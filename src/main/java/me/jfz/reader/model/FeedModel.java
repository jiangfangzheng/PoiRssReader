package me.jfz.reader.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 订阅feed和分类模型
 *
 * @author Sandeepin
 * @since 2020/2/20 0020
 */
public class FeedModel implements Comparable<FeedModel>, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    // 首页
    private String home;

    // feed url
    private String url;

    private String group;

    public FeedModel(String id, String name, String home, String url, String group) {
        this.id = id;
        this.name = name;
        this.home = home;
        this.url = url;
        this.group = group;
    }

    @Override
    public int compareTo(FeedModel o) {
        if (!Objects.equals(this.name, o.name)) {
            return this.name.compareTo(o.name);
        }
        return this.url.compareTo(o.url);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeedModel feedModel = (FeedModel) o;
        return Objects.equals(url, feedModel.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return name;
    }
}
