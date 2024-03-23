package gitlet;

import java.util.HashMap;
import java.util.Map;

public class RemoveStage {
    private static Map<String, String> fileNameToID = new HashMap<>();

    public static void addFile(Blob blob) {
        fileNameToID.put(blob.getFileName(), blob.getID());
    }
}
