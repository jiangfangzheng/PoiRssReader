package me.jfz.reader;

import static me.jfz.reader.data.RssData.idAndSubscibeModelMap;
import static me.jfz.reader.data.RssData.nameAndContentModelsMap;
import static me.jfz.reader.data.RssData.serializeNameAndContentModelsMap;

import me.jfz.reader.model.ContentModel;
import me.jfz.reader.model.SubscibeModel;
import me.jfz.reader.thread.SubscibeThread;

import com.formdev.flatlaf.FlatLightLaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

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

    private JProgressBar progressBar1;

    private JButton button4;

    private JButton button5;

    private JButton button6;

    private JScrollPane scrollPanel2;

    public static void main(String[] args) {
        // FlatLightLaf 主题
        FlatLightLaf.install();

        // JFrame配置
        JFrame frame = new JFrame("PoiRssReader");
        MainGui mainGui = new MainGui();
        frame.setContentPane(mainGui.panel1);
        frame.setSize(1850, 975);
        // 一个很简单就能让窗体居中的方法, 当参数为null时窗体处于屏幕正中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                serializeNameAndContentModelsMap();
                logger.warn("窗口关闭，程序关闭！");
                System.exit(0);
            }
        });
        JComponent[] jComponents = {mainGui.label2, mainGui.progressBar1};

        // 界面自定义的一些初始化
        mainGui.createUIComponents(jComponents);
        // 异步任务初始化
        initAsync(jComponents);
    }

    private static void initAsync(JComponent[] jComponents) {
        logger.info("initAsync()");
        Thread thread = new SubscibeThread(jComponents);
        thread.start();
    }

    private void createUIComponents(JComponent[] jComponents) {
        // 界面文字
        setUiInitTextMsg();

        // JTree动态加载RSS订阅源
        TreeModel newModel = getJTreeClassFeedModelData();
        tree1.setModel(newModel);

        // 文章列表容器加边框区别
        panel2.setBorder(BorderFactory.createLineBorder(Color.gray));
        // 文件内容滚动条附着
        scrollPane1.setViewportView(editorPane1);
        editorPane1.setEditable(false);
        // 设置垂直水平滚动条时刻显示
        // scrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        // scrollPane1.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);

        scrollPanel2.setViewportView(list1);
        // scrollPanel2.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        // scrollPanel2.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);

        // 事件处理
        button1.addActionListener(e -> logger.info("所有 按钮被点击"));
        button2.addActionListener(e -> logger.info("未读 按钮被点击"));
        button3.addActionListener(e -> logger.info("星标 按钮被点击"));
        button4.addActionListener(e -> {
            logger.info("刷新 按钮被点击");
            initAsync(jComponents);
        });
        button5.addActionListener(e -> logger.info("新增订阅... 按钮被点击"));
        button6.addActionListener(e -> logger.info("搜索... 按钮被点击"));
        tree1.addTreeSelectionListener(e -> {
            String name = e.getPath().getLastPathComponent().toString();
            logger.info("当前被选中的节点:{}", name);

            DefaultListModel<ContentModel> defaultListModel = new DefaultListModel<>();
            Set<ContentModel> contentModels = nameAndContentModelsMap.get(name);
            if (contentModels == null) {
                list1.setModel(defaultListModel);
                return;
            }
            int i = 0;
            for (ContentModel contentModel : contentModels) {
                defaultListModel.add(i++, contentModel);
            }
            list1.setModel(defaultListModel);
        });
        list1.addListSelectionListener(e -> {
            // 设置只有释放鼠标时才触发
            if (!list1.getValueIsAdjusting()) {
                ContentModel contentModel = (ContentModel) list1.getSelectedValue();
                if (contentModel == null) {
                    logger.error("feedModel is null");
                    return;
                }
                logger.info(contentModel.getTitle());
                File file = new File("./tmpHtml/" + UUID.randomUUID().toString() + ".html");
                try {
                    FileWriter fw = new FileWriter(file, false);
                    fw.write("<html>");
                    fw.write("<head>");
                    fw.write("</head>");
                    fw.write("<body>");
                    fw.write("<h1>" + contentModel.getTitle() + "</h1>");
                    fw.write("<h1>" + contentModel.getAuthor() + "</h1>");
                    fw.write("<h1>" + contentModel.getFeedId() + "</h1>");
                    fw.write("<h1>" + contentModel.getId() + "</h1>");
                    fw.write("<h1>" + contentModel.getLink() + "</h1>");
                    fw.write("<h1>" + contentModel.getTime() + "</h1>");
                    if (contentModel.getContent() != null) {
                        fw.write(contentModel.getContent());
                    } else {
                        fw.write("");
                        logger.error("feed 文件内容解析失败");
                    }
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

    private void setUiInitTextMsg() {
        label1.setText("概览");
        label2.setText("订阅项：0   总条数：0");
        button1.setText("所有  ( ... )");
        button2.setText("未读  ( ... )");
        button3.setText("星标  ( ... )");
        button4.setText("刷新");
        button5.setText("新增订阅...");
        button6.setText("搜索...");
    }

    /**
     * 生成JTree中的数据，包括分类、订阅源列表
     *
     * @return reeModel
     */
    private TreeModel getJTreeClassFeedModelData() {
        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new SubscibeModel("root", "所有订阅", "", -1, null));

        // 2级节点
        Set<SubscibeModel> collectType = idAndSubscibeModelMap.values()
            .stream()
            .filter(e -> e.getType() == 0)
            .collect(Collectors.toSet());
        for (SubscibeModel typeModel : collectType) {
            DefaultMutableTreeNode tmp2Node = new DefaultMutableTreeNode(typeModel);
            // 3级节点
            Set<SubscibeModel> subscibeModelSet = idAndSubscibeModelMap.values()
                .stream()
                .filter(e -> e.getType() == 1)
                .filter(e -> e.getPreNode().equals(typeModel.getId()))
                .collect(Collectors.toSet());
            for (SubscibeModel sub : subscibeModelSet) {
                tmp2Node.add(new DefaultMutableTreeNode(sub));
            }
            rootNode.add(tmp2Node);
        }

        return new DefaultTreeModel(rootNode);
    }

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setMinimumSize(new Dimension(-1, -1));
        panel1.setName("panel1");
        panel1.setVisible(true);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.WEST);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setMinimumSize(new Dimension(300, 209));
        panel4.setPreferredSize(new Dimension(300, 600));
        panel3.add(panel4, BorderLayout.WEST);
        label1 = new JLabel();
        label1.setText("Label");
        panel4.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Button");
        panel4.add(button1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(270, -1), 0,
            false));
        button2 = new JButton();
        button2.setText("Button");
        panel4.add(button2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(270, -1), 0,
            false));
        button3 = new JButton();
        button3.setText("Button");
        panel4.add(button3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(270, -1), 0,
            false));
        label2 = new JLabel();
        label2.setText("Label");
        panel4.add(label2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 8, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tree1 = new JTree();
        panel4.add(tree1, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0,
            false));
        progressBar1 = new JProgressBar();
        panel4.add(progressBar1, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        button4 = new JButton();
        button4.setText("Button");
        panel5.add(button4, BorderLayout.WEST);
        button5 = new JButton();
        button5.setText("Button");
        panel5.add(button5, BorderLayout.CENTER);
        button6 = new JButton();
        button6.setText("Button");
        panel5.add(button6, BorderLayout.EAST);
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setMinimumSize(new Dimension(300, 54));
        panel2.setPreferredSize(new Dimension(300, 54));
        panel3.add(panel2, BorderLayout.CENTER);
        list1 = new JList();
        panel2.add(list1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0,
            false));
        scrollPanel2 = new JScrollPane();
        panel2.add(scrollPanel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        panel6.setPreferredSize(new Dimension(300, 24));
        panel1.add(panel6, BorderLayout.CENTER);
        editorPane1 = new JEditorPane();
        panel6.add(editorPane1, BorderLayout.WEST);
        scrollPane1 = new JScrollPane();
        panel6.add(scrollPane1, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
