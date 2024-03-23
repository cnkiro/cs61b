package gitlet;

import java.io.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     *
     *  Structure of the .gitlet directory
     *   .gitlet
     *      |--objects
     *      |     |--commit and blob
     *      |--refs
     *      |    |--heads
     *      |         |--master
     *      |--HEAD
     *      |--addstage
     *      |--removestage
     */
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECT_DIR = join(GITLET_DIR, "objects");
    public static final File REF_DIR = join(GITLET_DIR, "refs");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    public static final File ADDSTAGE_FILE = join(GITLET_DIR, "addstage");
    public static final File REMOVESTAGE_FILE = join(GITLET_DIR, "removesatge");
    public static final File HEADS_DIR = join(REF_DIR, "heads");
    public static final File MASTER_FILE = join(HEADS_DIR, "master");

    public static boolean isInit() {
        return GITLET_DIR.exists();
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    // step1: create the structure step2: create the first initial commit
    public static void initialize() {
        GITLET_DIR.mkdir();
        OBJECT_DIR.mkdir();
        REF_DIR.mkdir();
        HEADS_DIR.mkdir();
        try {
            HEAD_FILE.createNewFile();
            ADDSTAGE_FILE.createNewFile();
            REMOVESTAGE_FILE.createNewFile();
            MASTER_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        createCommit("initial message", new HashMap<>(), new Date(0), new LinkedList<>());
    }

    public static void createCommit(String message, Map<String, String> fileNameToBlobID, Date currentTime, List<String> parentsID) {
        Commit commit = new Commit(message, fileNameToBlobID, currentTime, parentsID);
        commit.SaveCommit(OBJECT_DIR);
        writeContents(HEAD_FILE, commit.getID());
        writeContents(MASTER_FILE, commit.getID());
    }

    // step1: checkout whether this file is already created; step2: add blob to the AddStage
    public static void addFile(String arg) {
        File f = new File(CWD, arg);
        if (!f.exists()) {
            message("File doesn't exit.");
        } else {
            Blob blob = new Blob(arg, readContents(f));
            AddStage.AddFile(blob);
            blob.saveFile();
        }
    }

    // step1: copy parents' map step2: modify map according to the AddStage step3: create commit and save it
    public static void makeCommit(String string) {
        String parentID = readContentsAsString(HEAD_FILE);
        Commit parentCommit = readObject(join(OBJECT_DIR, parentID), Commit.class);
        Map<String, String> tempMap = parentCommit.getMap();
        List<String> parentsID = new LinkedList<>();
        parentsID.add(parentCommit.getID());
        AddStage.readBlobs(readBlobsFromFile(ADDSTAGE_FILE.getAbsolutePath()));
        if (!AddStage.isEmpty()) {
            createCommit(string, AddStage.modify(tempMap), new Date(), parentsID);
            AddStage.clear();
        } else {
            message("No changes added to the commit.");
        }
    }

    public static Blob[] readBlobsFromFile(String filename) {
        Blob[] blobs = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            int count = 0;
            while (true) {
                try {
                    Blob blob = (Blob) inputStream.readObject();
                    if (blobs == null) {
                        blobs = new Blob[1];
                    } else {
                        Blob[] temp = new Blob[blobs.length + 1];
                        System.arraycopy(blobs, 0, temp, 0, blobs.length);
                        blobs = temp;
                    }
                    blobs[count++] = blob;
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blobs;
    }

    // three different ways
    public static void removeFile(String arg) {
        // 1: filename [arg] in the addstage without any commit
        if(AddStage.containsFile(arg)) {
            AddStage.removeFile(arg);
        }
        // 2: HEAD commit contains the file [arg] and CWD has this file
        File f = join(CWD, arg);
        Commit headCommit = readObject(HEAD_FILE, Commit.class);
        if (headCommit.containsFile(arg) && f.exists()) {
            RemoveStage.addFile(new Blob(f.getName(), readContents(f)));
            f.delete();
        } else {
            RemoveStage.addFile(new Blob(f.getName(), readContents(f)));
        }
    }

    // step1: get the head commit step2: iterate the parents commit util reach the initial commit
    public static void printLog() {
        Commit commit = readObject(HEAD_FILE, Commit.class);
        while (commit.getParentsID() != null) {
            commit.printMessage();
            commit = findPreCommit(commit.getID());
        }
        commit.printMessage();
    }

    public static Commit findPreCommit(String commitID) {
        File file = new File(OBJECT_DIR, commitID);
        Commit commit = readObject(file, Commit.class);
        return commit;
    }
}
