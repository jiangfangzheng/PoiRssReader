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

    private String content;

    public ContentModel(String feedId, String title, String link, String author, String time, String content) {
        this.id = UUID.randomUUID().toString();
        this.feedId = feedId;
        this.title = title;
        this.link = link;
        this.author = author;
        this.time = time;
        this.content = content;
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
        return this.link.compareTo(o.link);
    }
}
