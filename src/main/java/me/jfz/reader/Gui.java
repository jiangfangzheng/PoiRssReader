package me.jfz.reader;

import static me.jfz.reader.data.ConstData.rssData;
import static me.jfz.reader.handle.RssDataHandle.allSubscribeCount;
import static me.jfz.reader.handle.RssDataHandle.getAllContents;
import static me.jfz.reader.handle.RssDataHandle.getReadContents;
import static me.jfz.reader.handle.RssDataHandle.getStarContents;
import static me.jfz.reader.handle.RssDataHandle.unreadSubscribeCount;
import static me.jfz.reader.handle.RssDataHandle.unstarSubscribeCount;

import me.jfz.reader.data.RssAllData;
import me.jfz.reader.handle.RssDataHandle;
import me.jfz.reader.model.ContentModel;
import me.jfz.reader.model.FeedModel;
import me.jfz.reader.model.SubscibeModel;
import me.jfz.reader.thread.SubscibeThread;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

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

    private JScrollPane scrollPaneTree = new JScrollPane();

    private JList listContents = new JList();

    private JScrollPane scrollPaneLists = new JScrollPane();

    private JEditorPane editorPaneContent = new JEditorPane();

    private JScrollPane scrollPaneContent = new JScrollPane();


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
        // 异步任务初始化
        gui.initAsync();
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
        p1.add(editorPaneContent, BorderLayout.WEST);
        p1.add(scrollPaneContent, BorderLayout.CENTER);
        // 文章列表面板
        p3.add(listContents, BorderLayout.WEST);
        p3.add(scrollPaneLists, BorderLayout.CENTER);

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
        p5.add(treeFeed, BorderLayout.WEST);
        p5.add(scrollPaneTree, BorderLayout.CENTER);

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
        // listContents.setListData(new String[] {"香蕉", "雪梨", "苹果", "荔枝"});

        editorPaneContent.setEditable(false);
        editorPaneContent.setContentType("text/html");
        editorPaneContent.setText("");

        // 滚动条附着
        scrollPaneLists.setViewportView(listContents);
        scrollPaneContent.setViewportView(editorPaneContent);
        scrollPaneTree.setViewportView(treeFeed);

        buttonAll.setText("所有  ( " + allSubscribeCount(rssData) + " )");
        buttonUnread.setText("未读  ( " + unreadSubscribeCount(rssData) + " )");
        buttonStar.setText("星标  ( " + unstarSubscribeCount(rssData) + " )");

        // JTree动态加载RSS订阅源
        TreeModel newModel = getJTreeClassFeedModelData(rssData);
        treeFeed.setModel(newModel);

        // 事件处理
        buttonAll.addActionListener(e -> {
            logger.info("所有 按钮被点击");
            Set<ContentModel> allContents = getAllContents(rssData);
            addContentModels2JList(allContents);
        });
        buttonUnread.addActionListener(e -> {
            logger.info("未读 按钮被点击");
            Set<ContentModel> readContents = getReadContents(rssData, false);
            addContentModels2JList(readContents);
        });
        buttonStar.addActionListener(e -> {
            logger.info("星标 按钮被点击");
            Set<ContentModel> starContents = getStarContents(rssData, true);
            addContentModels2JList(starContents);
        });
        buttonRefresh.addActionListener(e -> {
            logger.info("刷新 按钮被点击");
            initAsync();
        });
        buttonAdd.addActionListener(e -> logger.info("新增订阅... 按钮被点击"));
        buttonSearch.addActionListener(e -> logger.info("搜索... 按钮被点击"));
        // 列表树事件
        treeFeed.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
            if (!(defaultMutableTreeNode.getUserObject() instanceof FeedModel)) {
                logger.warn("当前树节点不是 FeedModel");
                return;
            }

            FeedModel feedModel = (FeedModel) defaultMutableTreeNode.getUserObject();
            logger.info("当前被选中的节点:{}", feedModel.getName());

            DefaultListModel<ContentModel> defaultListModel = new DefaultListModel<>();
            Set<ContentModel> contentModels = rssData.getContentMap().get(feedModel.getId());
            if (contentModels == null) {
                listContents.setModel(defaultListModel);
                return;
            }
            int i = 0;
            for (ContentModel contentModel : contentModels) {
                defaultListModel.add(i++, contentModel);
            }
            // 加载样式
            listContents.setModel(defaultListModel);
        });
        MyCellRenderer myCellRenderer = new MyCellRenderer();
        listContents.setCellRenderer((ListCellRenderer) myCellRenderer.getListCellRendererComponent(listContents, "", 0, true, true));

        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem starMenuItem = new JMenuItem("添加星标");
        JMenuItem unreadMenuItem = new JMenuItem("标记为未读");
        jPopupMenu.add(starMenuItem);
        jPopupMenu.add(unreadMenuItem);

        // 添加菜单项的点击监听器
        starMenuItem.addActionListener(e -> {
            ContentModel contentModel = (ContentModel) listContents.getSelectedValue();
            contentModel.setStar(true);
            System.out.println("添加星标 被点击");
        });
        unreadMenuItem.addActionListener(e -> {
            ContentModel contentModel = (ContentModel) listContents.getSelectedValue();
            contentModel.setRead(false);
            System.out.println("标记为未读 被点击");
        });
        listContents.add(jPopupMenu);
        listContents.addMouseListener(new myJListListener(listContents, jPopupMenu));
        // 列表文章事件
        listContents.addListSelectionListener(e -> {
            // 设置只有释放鼠标时才触发
            if (!listContents.getValueIsAdjusting()) {
                ContentModel contentModel = (ContentModel) listContents.getSelectedValue();
                if (contentModel == null) {
                    logger.error("feedModel is null");
                    return;
                }
                contentModel.setRead(true);
                logger.info("点击文章:{}", contentModel.getTitle());
                // html字符串直接显示
                String content = contentModel2Html(contentModel);
                editorPaneContent.setContentType("text/html");
                editorPaneContent.setText(content);
            }
        });

        // 超链接点击事件
        editorPaneContent.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    // todo 此写法并未跨平台
                    String command = "explorer.exe " + e.getURL().toString();
                    Runtime.getRuntime().exec(command);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    /**
     * 异步任务初始化
     */
    private void initAsync() {
        logger.info("initAsync() 刷新feed源");
        JComponent[] jComponents = {labelCount, progressBarFeed};
        Thread thread = new SubscibeThread(jComponents);
        thread.start();
    }

    /**
     * 生成JTree中的数据，包括分类、订阅源列表
     *
     * @return reeModel
     */
    private TreeModel getJTreeClassFeedModelData(RssAllData rssData) {
        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new SubscibeModel("root", "所有订阅", "", -1, null));

        Map<String, Set<String>> groupMap = rssData.getGroupMap();
        Map<String, FeedModel> feedMap = rssData.getFeedMap();

        // 2级节点（分组名）
        for (Map.Entry<String, Set<String>> entry : groupMap.entrySet()) {
            String groupName = entry.getKey();
            DefaultMutableTreeNode tmp2Node = new DefaultMutableTreeNode(groupName);

            // 3级节点（Feed名）
            Set<String> feedIds = entry.getValue();
            for (String feedId : feedIds) {
                FeedModel feedModel = feedMap.get(feedId);
                tmp2Node.add(new DefaultMutableTreeNode(feedModel));
            }

            rootNode.add(tmp2Node);
        }

        return new DefaultTreeModel(rootNode);
    }

    /**
     * 将ContentModel列表加入到Jlist中
     *
     * @param readContents
     */
    private void addContentModels2JList(Set<ContentModel> readContents) {
        DefaultListModel<ContentModel> defaultListModel = new DefaultListModel<>();
        int i = 0;
        for (ContentModel contentModel : readContents) {
            defaultListModel.add(i++, contentModel);
        }
        listContents.setModel(defaultListModel);
    }

    /**
     * ContentModel转HTML样式
     *
     * @param contentModel contentModel
     * @return HTML String
     */
    private String contentModel2Html(ContentModel contentModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><head></head><body>");
        stringBuilder.append("<h1>" + contentModel.getTitle() + "</h1>");
        stringBuilder.append("<h3>时间：" + contentModel.getTime() + "　　作者：" + contentModel.getAuthor() + "</h3>");
        stringBuilder.append(
            "<h3>链接：<a href=\"" + contentModel.getLink() + "\">" + contentModel.getLink() + "</a></h3>");
        stringBuilder.append("<h2>正文：</h2>");
        stringBuilder.append(contentModel.getContent());
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }

    static class MyCellRenderer extends DefaultListCellRenderer {
        // JLst渲染器
        // list - 正在绘制的 JList,
        // value - 由 list.getModel().getElementAt(index) 返回的值,
        // index - 单元格索引。
        // isSelected - 如果选择了指定的单元格，则为 true。
        // cellHasFocus - 如果指定的单元格拥有焦点，则为 true。
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ContentModel) {
                ContentModel contentModel = (ContentModel) value;
                // System.out.println(index + " isRead " + contentModel.isRead());
                if (contentModel.isRead()) {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
            }
            return c;
        }
    }

    static class myJListListener extends MouseAdapter {

        private JList list1;

        private JPopupMenu jPopupMenu;

        public myJListListener(JList list1, JPopupMenu jPopupMenu) {
            this.list1 = list1;
            this.jPopupMenu = jPopupMenu;
        }

        //e.getButton() 返回值有 1，2，3。1代表鼠标左键，3代表鼠标右键
        //jList.getSelected() 返回的是选中的JList中的项数。
        //if语句的意思也就是，在JList 中点击了右键而且JList选中了某项，显示右键菜单
        //e.getX() , e.getY() 返回的是鼠标目前的位置！也就是在目前鼠标的位置上弹出右键
        @Override
        public void mouseClicked(MouseEvent e) {
            // System.out.println(e.getButton());
            // System.out.println(list1.getSelectedIndex());
            if (e.getButton() == 3 && list1.getSelectedIndex() >= 0) {
                ContentModel contentModel = (ContentModel) list1.getSelectedValue();
                System.out.println("isRead " + contentModel.isRead());
                System.out.println("isStar " + contentModel.isStar());
                jPopupMenu.show(list1, e.getX(), e.getY());
            }
        }
    }
}

