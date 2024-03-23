package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import gitlet.Repository;

public class Blob implements Serializable {
    private String fileName;
    private String id;
    private byte[] contents;


    public Blob(String fileName, byte[] contents) {
        this.fileName = fileName;
        this.contents = contents;
        this.id = Utils.sha1(fileName, contents.toString());
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getID() {
        return  this.id;
    }

    public void saveFile() {
        File f = new File(Repository.OBJECT_DIR, this.id);
        Utils.writeObject(f, this);
    }
}
