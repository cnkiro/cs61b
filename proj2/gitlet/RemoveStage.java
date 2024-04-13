package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class RemoveStage {
    private static Map<String, String> fileNameToBlobID = new HashMap<>();

    public final static File REMOVESTAGE = Utils.join(Repository.GITLET_DIR, "removestage");

    public static void initializeRemovestageFile() {
        try {
            REMOVESTAGE.createNewFile();
        } catch (Exception e) {}
    }

    public static void add(Blob blob) {
        fileNameToBlobID.put(blob.getFileName(), blob.getID());
        addBlobToStageFile(blob);
    }

    public static void modify(Map<String, String> tempMap) {
        for (String str: fileNameToBlobID.keySet()) {
            if (tempMap.containsKey(str)) {
                tempMap.remove(str, tempMap.get(str));
            }
        }
    }

    private static void addBlobToStageFile(Blob blob) {
        String blobsID = Utils.readContentsAsString(REMOVESTAGE) + " " + blob.getID();
        Utils.writeContents(REMOVESTAGE, blobsID);
    }

    public static boolean isEmpty() {
        return fileNameToBlobID.keySet().isEmpty();
    }

    public static Map<String, String> getFileNameToBlobID() {
        return fileNameToBlobID;
    }

//    public static void readFromFile() {
//        String s = Utils.readContentsAsString(REMOVESTAGE);
//        String[] temp = s.split(" ");
//        for (int i = 1; i < temp.length; i++) {
//            Blob blob = readBlobFromFile(temp[i]);
//            fileNameToBlobID.put(blob.getFileName(), blob.getID());
//        }
//    }
//
//    private static Blob readBlobFromFile(String fileName) {
//        Blob blob = null;
//        try {
//            File file = new File(Repository.OBJECTS_DIR, fileName);
//            if (file.exists()) {
//                System.out.println("ok");
//                try (FileInputStream fileInputStream = new FileInputStream(file);
//                     ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//                    blob = (Blob) objectInputStream.readObject();
//                }
//            } else {
//            }
//        } catch (IOException | ClassNotFoundException e) {
//        }
//        return blob;
//    }

    public static void readFromFile() {
        String s = Utils.readContentsAsString(REMOVESTAGE);
        if (s.length() == 0) {
            return;
        }
        String[] temp = s.split(" ");
        for (int i = 1; i < temp.length; i++) {
            File f = new File(Repository.OBJECTS_DIR, temp[i]);
            Blob blob = Utils.readObject(f, Blob.class);
            fileNameToBlobID.put(blob.getFileName(), blob.getID());
        }
    }

    public static void clear() {
        fileNameToBlobID.clear();
        Utils.writeContents(REMOVESTAGE, "");
    }

    public static void printStatus() {
        RemoveStage.readFromFile();
        System.out.println("=== Removed Files ===");
        for (String s: fileNameToBlobID.keySet()) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println();
    }
}
