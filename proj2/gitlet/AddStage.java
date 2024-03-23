package gitlet;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import gitlet.Repository;

public class AddStage {
    private static Map<String, String> fileNameToID = new HashMap<>();

    public static void clear() {
        fileNameToID = new HashMap<>();
        Utils.writeContents(Repository.ADDSTAGE_FILE, "");
    }

    public static void AddFile(Blob blob) {
        fileNameToID.put(blob.getFileName(), blob.getID());
        Utils.writeObject(Repository.ADDSTAGE_FILE, blob);
    }

    public static Map<String, String> modify(Map<String, String> tempMap) {
        for (String str: fileNameToID.keySet()) {
          tempMap.put(str, fileNameToID.get(str));
        }
        return tempMap;
    }

    public static boolean isEmpty() {
        return fileNameToID.isEmpty();
    }

    public static boolean containsFile(String fileName) {
        return fileNameToID.containsKey(fileName);
    }

    public static void deletFile(String fileName) {
        fileNameToID.remove(fileName);
    }

    public static void getFile() {

    }

    public static void readBlobs(Blob[] blobs) {
        for (Blob blob: blobs) {
            fileNameToID.put(blob.getFileName(), blob.getID());
        }
    }

    public static void removeFile(String fileName) {
        fileNameToID.remove(fileName);
    }
}
