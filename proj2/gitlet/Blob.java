package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private String fileName;
    private String ID;
    private byte[] content;

    public Blob() {

    }

    public Blob(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
        this.ID = Utils.sha1(fileName, content);
    }

    public String getFileName() {
        return fileName;
    }

    public String getID() {
        return ID;
    }

    public byte[] getContent() {
        return content;
    }

    public void save() {
        File f = Utils.join(Repository.OBJECTS_DIR, this.getID());
        try {
            f.createNewFile();
            Utils.writeObject(f, this);
        } catch (Exception e) {
        }
    }
}
