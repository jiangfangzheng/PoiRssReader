package me.jfz.reader.model;

import com.rometools.rome.feed.synd.SyndEntry;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/19 0019
 */
public class FeedModel {
    SyndEntry syndEntry;

    String title;

    public FeedModel(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;
        this.title = syndEntry.getTitle();
    }

    @Override
    public String toString() {
        return title;
    }

    public SyndEntry getSyndEntry() {
        return syndEntry;
    }

    public void setSyndEntry(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
