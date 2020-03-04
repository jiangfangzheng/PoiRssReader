package me.jfz.reader.model;

import java.util.Objects;
import java.util.UUID;

/**
 * 订阅feed和分类模型
 *
 * @author Sandeepin
 * @since 2020/2/20 0020
 */
public class SubscibeModel implements Comparable<SubscibeModel> {
    private String id;

    private String name;

    private String url;

    // -1根 0分类 1订阅源
    private int type;

    private String preNode;

    public SubscibeModel(String id, String name, String url, int type, String preNode) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
        this.preNode = preNode;
    }

    public SubscibeModel(String name, String url, int type, String preNode) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.url = url;
        this.type = type;
        this.preNode = preNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPreNode() {
        return preNode;
    }

    public void setPreNode(String preNode) {
        this.preNode = preNode;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubscibeModel that = (SubscibeModel) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public int compareTo(SubscibeModel o) {
        return this.name.compareTo(o.name);
    }
}
