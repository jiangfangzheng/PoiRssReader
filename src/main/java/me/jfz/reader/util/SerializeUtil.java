package me.jfz.reader.util;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述
 *
 * @author Sandeepin
 * @since 2020/3/30 0030
 */
public class SerializeUtil {

    public static void serialize(String fileName, Object object) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(object);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Object deserialize(String fileName) {
        Object object = new Object();
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            object = in.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return object;
    }

    public static void serializeFast(String fileName, Object object) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             FSTObjectOutput out = new FSTObjectOutput(fileOut)) {
            out.writeObject(object);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Object deserializeFast(String fileName) {
        Object object = new Object();
        try (FileInputStream fileIn = new FileInputStream(fileName);
             FSTObjectInput in = new FSTObjectInput(fileIn)) {
            object = in.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return object;
    }

}
