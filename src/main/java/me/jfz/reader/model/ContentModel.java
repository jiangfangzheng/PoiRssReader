package me.jfz.reader.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 文章内容模型
 *
 * @author Sandeepin
 * @since 2020/3/4 0004
 */
public class ContentModel implements Comparable<ContentModel>, Serializable {

    private String id;

    private String feedId;

    private String title;

    private String link;

    private String author;

    private String time;

    private String updateTime;

    private String content;

    private boolean isStar;

    public ContentModel(String feedId, String title, String link, String author, String time, String updateTime, String content) {
        this.id = UUID.randomUUID().toString();
        this.feedId = feedId;
        this.title = title;
        this.link = link;
        this.author = author;
        this.time = time;
        this.updateTime = updateTime;
        this.content = content;
        this.isStar = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentModel that = (ContentModel) o;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }

    @Override
    public int compareTo(ContentModel o) {
        if (!Objects.equals(this.time, o.time)) {
            // 时间倒序
            return o.time.compareTo(this.time);
        }
        // 字典顺序顺序
        return this.title.compareTo(o.title);
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }
}
