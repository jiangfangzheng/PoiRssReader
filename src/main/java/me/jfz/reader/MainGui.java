package me.jfz.reader;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static me.jfz.reader.RssData.nameAndSyndFeedMap;
import static me.jfz.reader.RssData.nameAndUrl;
import static me.jfz.reader.RssData.titleAndContentMap;

import me.jfz.reader.thread.SubscibeThread;

import com.formdev.flatlaf.FlatLightLaf;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/15 0015
 */
public class MainGui {

    static Logger logger = LoggerFactory.getLogger(MainGui.class);

    private JPanel panel1;

    private JButton button1;

    private JButton button2;

    private JButton button3;

    private JTree tree1;

    private JPanel panel2;

    private JList list1;

    private JEditorPane editorPane1;

    private JScrollPane scrollPane1;

    private JLabel label1;

    private JLabel label2;

    public static void main(String[] args) {
        // FlatLightLaf 主题
        FlatLightLaf.install();

        // JFrame配置
        JFrame frame = new JFrame("PoiRssReader");
        MainGui mainGui = new MainGui();
        frame.setContentPane(mainGui.panel1);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(1850, 975);
        // 一个很简单就能让窗体居中的方法, 当参数为null时窗体处于屏幕正中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // 界面自定义的一些初始化
        mainGui.createUIComponents();

        // 异步任务初始化
        initAsync(mainGui.label2);
    }

    private static void initAsync(JLabel label) {
        logger.info("initAsync()");
        Thread thread = new SubscibeThread(label);
        thread.start();
    }

    private void createUIComponents() {
        // 界面文字
        label1.setText("概览");
        label2.setText("订阅项：0   总条数：0   处理中...");
        button1.setText("所有  ( ... )");
        button2.setText("未读  ( ... )");
        button3.setText("星标  ( ... )");

        // 文章列表容器加边框区别
        panel2.setBorder(BorderFactory.createLineBorder(Color.gray));
        // 文件内容滚动条附着
        scrollPane1.setViewportView(editorPane1);
        editorPane1.setEditable(false);
        // 设置垂直水平滚动条时刻显示
        scrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane1.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);

        // 事件处理
        button1.addActionListener(e -> logger.info("所有 按钮被点击"));
        button2.addActionListener(e -> logger.info("未读 按钮被点击"));
        button3.addActionListener(e -> logger.info("星标 按钮被点击"));
        tree1.addTreeSelectionListener(e -> {
            String name = e.getPath().getLastPathComponent().toString();
            logger.info("当前被选中的节点:{}", name);

            DefaultListModel<String> defaultListModel = new DefaultListModel<>();
            SyndFeed syndFeed = nameAndSyndFeedMap.get(name);
            if (syndFeed == null) {
                return;
            }
            int i = 0;
            for (SyndEntry entry : syndFeed.getEntries()) {
                // System.out.println(entry);
                defaultListModel.add(i++, entry.getTitle());
                if (entry.getContents() != null && !entry.getContents().isEmpty() && entry.getContents().get(0) != null) {
                    titleAndContentMap.putIfAbsent(entry.getTitle(), entry.getContents().get(0).getValue());
                }
            }
            list1.setModel(defaultListModel);
        });
        list1.addListSelectionListener(e -> {
            // 设置只有释放鼠标时才触发
            if (!list1.getValueIsAdjusting()) {
                String title = (String) list1.getSelectedValue();
                logger.info(title);
                File file = new File("./tmpHtml/" + UUID.randomUUID().toString() + ".html");
                try {
                    FileWriter fw = new FileWriter(file, false);
                    fw.write("<html>");
                    fw.write("<head>");
                    fw.write("</head>");
                    fw.write("<body>");
                    fw.write(titleAndContentMap.get(title) + "");
                    fw.write("</body></html>");
                    //清理操作
                    fw.flush();
                    fw.close();
                    String str = file.getAbsolutePath();
                    str = "file:" + str;
                    editorPane1.setPage(str);
                } catch (IOException ex) {
                    logger.error("IOException:", ex);
                }
            }
        });

    }
}
