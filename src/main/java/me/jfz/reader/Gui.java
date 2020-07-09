package me.jfz.reader;

import static me.jfz.reader.data.ConstData.rssData;

import me.jfz.reader.handle.RssDataHandle;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/7/9 0009
 */
public class Gui {

    private static final Logger logger = LoggerFactory.getLogger(Gui.class);

    private JPanel panelRssRoot = new JPanel(new BorderLayout());

    private JPanel p0 = new JPanel(new BorderLayout());

    private JPanel p1 = new JPanel(new BorderLayout());

    private JPanel p2 = new JPanel(new BorderLayout());

    private JPanel p3 = new JPanel(new BorderLayout());

    private JPanel p4 = new JPanel(new MigLayout("fill, wrap 3"));

    private JPanel p5 = new JPanel(new BorderLayout());

    private JButton buttonRefresh = new JButton("刷新");

    private JButton buttonAdd = new JButton("新增订阅...");

    private JButton buttonSearch = new JButton("搜索...");

    private JLabel labelOverview = new JLabel("概览");

    private JLabel labelCount = new JLabel("订阅项：0   总条数：0");

    private JButton buttonAll = new JButton("所有  (...)");

    private JButton buttonUnread = new JButton("未读  (...)");

    private JButton buttonStar = new JButton("星标  (...)");

    private JProgressBar progressBarFeed = new JProgressBar(0);

    private JTree treeFeed = new JTree();

    private JList listContents = new JList();

    private JEditorPane editorPane1 = new JEditorPane();

    public static void main(String[] args) {
        // FlatLightLaf 主题
        FlatLightLaf.install();

        // JFrame配置
        JFrame frame = new JFrame("PoiRssReader");
        Gui gui = new Gui();
        frame.setContentPane(gui.panelRssRoot);
        frame.setSize(1850, 975);
        // 一个很简单就能让窗体居中的方法, 当参数为null时窗体处于屏幕正中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // serializeNameAndContentModelsMap();
                RssDataHandle.serializeRssData(rssData);
                logger.warn("窗口关闭，程序关闭！");
                System.exit(0);
            }
        });
        // 界面布局
        gui.rssReaderLayout();
        // 界面参数和数据填充
        gui.rssReaderProperty();

        // JComponent[] jComponents = {mainGui.label2, mainGui.progressBar1};

        // 界面自定义的一些初始化
        // mainGui.createUIComponents(jComponents);
        // 异步任务初始化
        // initAsync(jComponents);
    }

    /**
     * 布局设置
     */
    public void rssReaderLayout() {
        // panel布局层级
        panelRssRoot.add(p0, BorderLayout.WEST);
        panelRssRoot.add(p1, BorderLayout.CENTER);
        p0.add(p2, BorderLayout.WEST);
        p0.add(p3, BorderLayout.CENTER);
        p2.add(p4, BorderLayout.NORTH);
        p2.add(p5, BorderLayout.CENTER);

        // 文章显示面板
        p1.add(editorPane1);
        // 文章列表面板
        p3.add(listContents);

        // 按钮相关面板
        p4.add(labelOverview, "growx, span");
        p4.add(buttonRefresh);
        p4.add(buttonAdd);
        p4.add(buttonSearch);
        p4.add(buttonAll, "growx, span");
        p4.add(buttonUnread, "growx, span");
        p4.add(buttonStar, "growx, span");
        p4.add(labelCount, "growx, span");
        p4.add(progressBarFeed, "growx, span");
        // Feed树面板
        p5.add(treeFeed);

    }

    /**
     * 细节参数和内容设置
     */
    public void rssReaderProperty() {

        // 设置一下首选大小
        listContents.setBackground(new Color(247, 247, 247));
        listContents.setPreferredSize(new Dimension(200, 100));
        // 允许可间断的多选
        listContents.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 设置选项数据（内部将自动封装成 ListModel ）
        listContents.setListData(new String[] {"香蕉", "雪梨", "苹果", "荔枝"});

        editorPane1.setEditable(false);
        editorPane1.setContentType("text/html");
        editorPane1.setText("<p> 蒋方正 poi 123 </p>");

    }

}

