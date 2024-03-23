package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.writeContents;
import static gitlet.Utils.writeObject;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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
    private String id;
    private Map<String, String> fileNameToBlobID;
    private Date currentTime;
    private List<String> parentsID;

    public Commit(String message, Map<String, String> fileNameToBlobID, Date currentTime, List<String> parentsID) {
        this.message = message;
        this.fileNameToBlobID = fileNameToBlobID;
        this.currentTime = currentTime;
        this.parentsID = parentsID;
        this.id = createID();
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    private String createID() {
        return Utils.sha1(this.message, this.fileNameToBlobID.toString(), dateToTimeStamp(this.currentTime), this.parentsID.toString());
    }

    public String getID() {
        return this.id;
    }

    public void SaveCommit(File file) {
        File f = new File(file, this.id);
        writeObject(f, this);
    }

    public Map<String, String> getMap() {
        return this.fileNameToBlobID;
    }

    public boolean containsFile(String fileName) {
        return this.fileNameToBlobID.containsKey(fileName);
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFileNameToBlobID() {
        return fileNameToBlobID;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public List<String> getParentsID() {
        return parentsID;
    }

    public void printMessage() {
        System.out.println("===");
        System.out.println("commit " + this.id);
        System.out.println(dateToTimeStamp(this.currentTime));
        System.out.println(this.message);
        System.out.println();
    }
}
