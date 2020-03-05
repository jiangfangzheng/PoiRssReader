package me.jfz.reader;

import me.jfz.reader.model.ContentModel;
import me.jfz.reader.model.SubscibeModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/2/18 0018
 */
public final class RssData {

    static Logger logger = LoggerFactory.getLogger(RssData.class);

    public static final String SERIALIZE_CONTENTMODELS_MAP_FILE = "feedContent.dat";

    /**
     * feed名和内容列表，数据需要序列化保存
     */
    public static Map<String, List<ContentModel>> nameAndContentModelsMap = new HashMap<>(128);

    /**
     * feed id和订阅模型（分类、feed源），从json中读取
     */
    public static Map<String, SubscibeModel> idAndSubscibeModelMap = new HashMap<>(128);

    /**
     * feed名和url，实时产生，不序列化
     */
    public static Map<String, String> nameAndUrl = new HashMap<>(128);

    public static void getUrlMapFromSubscribeMap(Map<String, String> nameAndUrl,
        Map<String, SubscibeModel> idAndSubscibeModelMap) {
        nameAndUrl.clear();
        for (SubscibeModel subscibeModel : idAndSubscibeModelMap.values()) {
            if (subscibeModel.getType() == 1) {
                nameAndUrl.put(subscibeModel.getName(), subscibeModel.getUrl());
            }
        }
    }

    public static void getSubscribeModelMapFromJson(Map<String, SubscibeModel> idAndSubscibeModelMap) {
        byte[] jsonByte;
        Map<String, SubscibeModel> tmpMap = new HashMap<>();
        try {
            jsonByte = Files.readAllBytes(Paths.get("feedData.json"));
            tmpMap = JSON.parseObject(new String(jsonByte, StandardCharsets.UTF_8),
                new TypeReference<TreeMap<String, SubscibeModel>>() {
                });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        idAndSubscibeModelMap.putAll(tmpMap);
    }

    public static void serializeNameAndContentModelsMap() {
        try {
            FileOutputStream fileOut = new FileOutputStream(SERIALIZE_CONTENTMODELS_MAP_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(nameAndContentModelsMap);
            out.close();
            fileOut.close();
            logger.warn("Serialized nameAndContentModelsMap OK!");
        } catch (IOException i) {
            logger.error("Serialized nameAndContentModelsMap IOException:", i);
        }
    }

    public static void deserializeNameAndContentModelsMap() {
        try (FileInputStream fileIn = new FileInputStream(SERIALIZE_CONTENTMODELS_MAP_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);) {
            Map<String, List<ContentModel>> tmp = (Map<String, List<ContentModel>>) in.readObject();
            if (tmp != null) {
                nameAndContentModelsMap.putAll(tmp);
                logger.error("Deserialized nameAndContentModelsMap OK!");
            }
        } catch (FileNotFoundException f) {
            logger.error("Deserialized nameAndContentModelsMap FileNotFoundException:", f);
        } catch (IOException i) {
            logger.error("Deserialized nameAndContentModelsMap IOException:", i);
        } catch (ClassNotFoundException c) {
            logger.error("Deserialized nameAndContentModelsMap ClassNotFoundException:", c);
        }
    }

    // static {
    //     nameAndUrl.put("福利吧", "https://fulibus.net/feed");
    //     nameAndUrl.put("阮一峰", "http://www.ruanyifeng.com/blog/atom.xml");
    //     nameAndUrl.put("ghpym", "https://www.ghpym.com/feed");
    //     nameAndUrl.put("异想萌域", "http://poi.ooo/index.xml");
    //     nameAndUrl.put("landiannews", "https://www.landiannews.com/feed");
    //     nameAndUrl.put("Sandeepin博客园", "http://feed.cnblogs.com/blog/u/72021/rss/");

    // 构造feed
    // SubscibeModel subscibeModelType1 = new SubscibeModel("我的博客", "", 0, "root");
    // SubscibeModel subscibeModelType2 = new SubscibeModel("科技IT", "", 0, "root");
    // SubscibeModel subscibeModelType3 = new SubscibeModel("其它收藏", "", 0, "root");
    //
    // SubscibeModel subscibeModel1 = new SubscibeModel("Sandeepin博客园", "http://feed.cnblogs
    // .com/blog/u/72021/rss/", 1, subscibeModelType1.getId());
    // SubscibeModel subscibeModel2 = new SubscibeModel("异想萌域", "http://poi.ooo/index.xml", 1, subscibeModelType1
    // .getId());
    //
    // SubscibeModel subscibeModel3 = new SubscibeModel("landiannews", "https://www.landiannews.com/feed", 1,
    // subscibeModelType2.getId());
    // SubscibeModel subscibeModel4 = new SubscibeModel("ghpym", "https://www.ghpym.com/feed", 1,
    // subscibeModelType2.getId());
    // SubscibeModel subscibeModel5 = new SubscibeModel("阮一峰", "http://www.ruanyifeng.com/blog/atom.xml", 1,
    // subscibeModelType2.getId());
    //
    // SubscibeModel subscibeModel6 = new SubscibeModel("福利吧", "https://fulibus.net/feed", 1, subscibeModelType3
    // .getId());
    //
    // idAndSubscibeModelMap.putIfAbsent(subscibeModelType1.getId(), subscibeModelType1);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModelType2.getId(), subscibeModelType2);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModelType3.getId(), subscibeModelType3);
    //
    // idAndSubscibeModelMap.putIfAbsent(subscibeModel1.getId(), subscibeModel1);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModel2.getId(), subscibeModel2);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModel3.getId(), subscibeModel3);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModel4.getId(), subscibeModel4);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModel5.getId(), subscibeModel5);
    // idAndSubscibeModelMap.putIfAbsent(subscibeModel6.getId(), subscibeModel6);

    // 存为json
    // String jsonStr = JSONObject.toJSONString(idAndSubscibeModelMap);
    // List<String> jsonList = new ArrayList<>();
    // jsonList.add(jsonStr);
    // try {
    //     Files.write(Paths.get("feedData.json"), jsonList);
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }
    // }

}
