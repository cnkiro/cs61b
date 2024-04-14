package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddStage {

    private static Map<String, String> fileNameToBlobID = new HashMap<>();
    public final static File ADDSTAGE = Utils.join(Repository.GITLET_DIR, "addstage");

    public static void initializeAddstageFile() {
        try {
            ADDSTAGE.createNewFile();
        } catch (Exception e) {}
    }

    public static void add(Blob blob) {
        fileNameToBlobID.put(blob.getFileName(), blob.getID());
        saveBlob(blob);
        addBlobToStageFile(blob);
    }

    private static void addBlobToStageFile(Blob blob) {
        String blobsID = Utils.readContentsAsString(ADDSTAGE) + " " + blob.getID();
        Utils.writeContents(ADDSTAGE, blobsID);
    }

    private static void saveBlob(Blob blob) {
       blob.save();
    }

    public static void modify(Map<String, String> tempMap) {
        for (String str: fileNameToBlobID.keySet()) {
            if (tempMap.containsKey(str)) {
                tempMap.put(str, fileNameToBlobID.get(str));
            } else {
                tempMap.put(str, fileNameToBlobID.get(str));
            }
        }
    }

    public static boolean isEmpty() {
        return fileNameToBlobID.isEmpty();
    }

//    public static void readFromFile() {
//        String s = Utils.readContentsAsString(ADDSTAGE);
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
        String s = Utils.readContentsAsString(ADDSTAGE);
        if (s.length() == 0) {
            return;
        }
        String[] temp = s.split(" ");
        for (int i = 1; i < temp.length; i++) {
            File f = Utils.join(Repository.OBJECTS_DIR, temp[i]);
            Blob blob = Utils.readObject(f, Blob.class);
            fileNameToBlobID.put(blob.getFileName(), blob.getID());
        }
    }


    public static void clear() {
        fileNameToBlobID.clear();
        Utils.writeContents(ADDSTAGE, "");
    }

    public static boolean contain(Blob blob) {
        return fileNameToBlobID.containsValue(blob.getID());
    }

    public static void remove(Blob blob) {
        fileNameToBlobID.remove(blob.getFileName());
        removeFromFile(blob.getID());
    }

    private static void removeFromFile(String id) {
        String s = Utils.readContentsAsString(ADDSTAGE);
        String[] IDs = s.split(" ");
        StringBuffer stringBuffer = new StringBuffer();
        for (String blobID: IDs) {
            if (id.equals(blobID)) {
                continue;
            } else {
                stringBuffer.append(blobID);
            }
        }
        Utils.writeContents(ADDSTAGE, stringBuffer.toString());
    }

    public static void printStatus() {
        AddStage.readFromFile();
        System.out.println("=== Staged Files ===");
        for (String s: fileNameToBlobID.keySet()) {
           System.out.println(s);
       }
        System.out.println();
        System.out.println();
    }

    public static Map<String, String> getFileNameToBlobID() {
        return fileNameToBlobID;
    }
}
