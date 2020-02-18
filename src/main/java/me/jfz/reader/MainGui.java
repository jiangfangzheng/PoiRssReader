package me.jfz.reader;

import com.formdev.flatlaf.FlatLightLaf;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
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

    private static Map<String, String> titleAndContentMap = new HashMap<>(128);

    private static Map<String, String> nameAndUrl = new HashMap<>(128);

    static {

        nameAndUrl.put("football", "https://fulibus.net/feed");
        nameAndUrl.put("hockey", "http://www.ruanyifeng.com/blog/atom.xml");
        nameAndUrl.put("hot dogs", "https://www.ghpym.com/feed");
        nameAndUrl.put("pizza", "http://poi.ooo/index.xml");
        nameAndUrl.put("ravioli", "https://www.landiannews.com/feed");
        nameAndUrl.put("bananas", "http://feed.cnblogs.com/blog/u/72021/rss/");


    }

    public static void main(String[] args) {
        FlatLightLaf.install();
        JFrame frame = new JFrame("PoiRssReader");
        MainGui mainGui = new MainGui();
        frame.setContentPane(mainGui.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setSize(1850, 975);
        // 一个很简单就能让窗体居中的方法, 当参数为null时窗体处于屏幕正中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mainGui.createUIComponents();
    }

    private JPanel panel1;

    private JButton button1;

    private JButton button2;

    private JButton button3;

    private JTree tree1;

    // private JTextPane textPane1;

    private JPanel panel2;

    private JList list1;

    private JEditorPane editorPane1;

    private JScrollPane scrollPane1;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel2.setBorder(BorderFactory.createLineBorder(Color.gray));

        scrollPane1.setViewportView(editorPane1);

        editorPane1.setEditable(false);
        //设置垂直水平滚动条时刻显示
        scrollPane1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        button1.addActionListener(e -> System.out.println("按钮被点击"));

        // table1.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         System.out.println("111");
        //         textPane1.setText("先走出去看看再说\n"+"一切都会好起来的\n"+"我为什么要听它的\n");
        //     }
        // });

        list1.addListSelectionListener(e -> {
            // 设置只有释放鼠标时才触发
            if (!list1.getValueIsAdjusting()) {
                String title = (String) list1.getSelectedValue();

                System.out.println(title);
                File file = new File(UUID.randomUUID().toString() + ".html");

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
                    ex.printStackTrace();
                }

                // editorPane1.setText("<html>" + titleAndContentMap.get(title) +"</html>");
            }
        });

        tree1.addTreeSelectionListener(e -> {
            String name = e.getPath().getLastPathComponent().toString();
            System.out.println("当前被选中的节点: " + name);
            // if ("bananas".equals(name)) {
            System.out.println("rss");
            DefaultListModel<String> defaultListModel = new DefaultListModel<>();
            String url = nameAndUrl.get(name);
            if (null != url) {
                try {
                    try (XmlReader reader = new XmlReader(new URL(url))) {
                        SyndFeed feed = new SyndFeedInput().build(reader);
                        System.out.println(feed.getTitle());
                        System.out.println("***********************************");
                        int i = 0;
                        for (SyndEntry entry : feed.getEntries()) {
                            System.out.println(entry);

                            defaultListModel.add(i++, entry.getTitle());
                            titleAndContentMap.putIfAbsent(entry.getTitle(), entry.getContents().get(0).getValue());

                            System.out.println("***********************************");
                        }
                        System.out.println("Done");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                list1.setModel(defaultListModel);
            }

            // Vector vData = new Vector();
            // Vector vName = new Vector();
            // vName.add("column1");
            // vName.add("column2");
            // Vector vRow = new Vector();
            // vRow.add("cell 0 0");
            // vRow.add("cell 0 1");
            // vData.add(vRow.clone());
            // vData.add(vRow.clone());
            // DefaultTableModel model = new DefaultTableModel(vData, vName);
            // table1.setModel(model);
            // }
        });

    }
}
