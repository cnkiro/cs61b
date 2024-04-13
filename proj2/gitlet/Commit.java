package gitlet;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import gitlet.Repository.*;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class

import static gitlet.Repository.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author cnkiro
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String ID;
    private Map<String, String> fileNameToBlobID;
    private Date currentTime;
    private List<String> parentsID;

    public Commit() {}

    public Commit(String message, Map<String, String> fileNameToBlobID, Date currentTime, List<String> parentsID) {
        this.message = message;
        this.fileNameToBlobID = fileNameToBlobID;
        this.currentTime = currentTime;
        this.parentsID = parentsID;
        this.ID = Utils.sha1(message, dateToTimeStamp(currentTime), fileNameToBlobID.toString(), parentsID.toString());
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Map<String, String> getFileNameToBlobID() {
        return fileNameToBlobID;
    }

    public void setFileNameToBlobID(Map<String, String> fileNameToBlobID) {
        this.fileNameToBlobID = fileNameToBlobID;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public List<String> getParentsID() {
        return parentsID;
    }

    public void setParentsID(List<String> parentsID) {
        this.parentsID = parentsID;
    }

    public void save() {
        try {
            File f = Utils.join(OBJECTS_DIR, this.ID);
            f.createNewFile();
            Utils.writeObject(f, this);
        } catch (Exception e) {
        }
        Utils.writeContents(HEAD_FILE, this.ID);
        Utils.writeContents(MASTER_HEAD, this.ID);
    }

    public boolean contain(Blob blob) {
        return this.fileNameToBlobID.containsValue(blob.getID());
    }

    public void print() {
        System.out.println("===");
        System.out.println("commit " + this.ID);
        System.out.println("Date: " + dateToTimeStamp(this.getCurrentTime()));
        System.out.println(this.message);
        System.out.println();
    }

    public void printfileName() {
        for (String file : fileNameToBlobID.keySet()) {
            System.out.println(file);
        }
    }

    public boolean containsFile(String fileName) {
        return fileNameToBlobID.containsKey(fileName);
    }

    public String getBlobIDByFileName(String fileName) {
        return fileNameToBlobID.get(fileName);
    }
}