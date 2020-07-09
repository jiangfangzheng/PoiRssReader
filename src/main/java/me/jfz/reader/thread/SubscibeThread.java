package me.jfz.reader.thread;

import static me.jfz.reader.data.ConstData.rssData;
import static me.jfz.reader.handle.RssDataHandle.refreshFeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/18 0018
 */
public class SubscibeThread extends Thread {
    static Logger logger = LoggerFactory.getLogger(SubscibeThread.class);

    private JComponent[] jComponents;

    public SubscibeThread(JComponent[] jComponents) {
        this.jComponents = jComponents;
    }

    @Override
    public void run() {
        logger.info("SubscibeThread() run start.");
        JLabel label = (JLabel) jComponents[0];
        JProgressBar progressBar = (JProgressBar) jComponents[1];
        progressBar.setMinimum(0);
        progressBar.setMaximum(rssData.getFeedMap().size());
        progressBar.setValue(0);

        refreshFeed(rssData);
        logger.info("SubscibeThread() run finished.");

        label.setText("订阅项：" + rssData.getFeedMap().size() + "   总条数：" + "");
    }
}
