package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEAD_FILE = join(GITLET_DIR, "head");
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    public static final File MASTER_HEAD = join(HEADS_DIR, "master");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    // initialize gitlet repo if '.gitlet' dir is not created
    public static void initializeRep() {
        // step1: check the 'gitlet' dir
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
            Utils.message("A Gitlet version-control system already exists in the current direcotry.");
        } else {
            InitializeDir();
        }
    }

    private static void InitializeDir() {
        GITLET_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        AddStage.initializeAddstageFile();
        RemoveStage.initializeRemovestageFile();
        try {
            HEAD_FILE.createNewFile();
            MASTER_HEAD.createNewFile();
        } catch (Exception e) {

        }
        createNewCommit("initial commit", new HashMap<String, String>(), new Date(0), new LinkedList<>());
    }

    private static void createNewCommit(String message, Map<String, String> fileNameToBlobID, Date currentTime, List<String> parentsID) {
        Commit commit = new Commit(message, fileNameToBlobID, currentTime, parentsID);
        commit.save();
    }

    public static void addFile(String fileName) {
        File f = join(CWD, fileName);
        if (!f.exists()) {
            Utils.message("File does not exist.");
        }
        Blob blob = new Blob(fileName, readContents(f));
        AddStage.add(blob);
    }

    public static void makeCommit(String message) {
        // 首先把head中指向的commit读取出来
        String preCommitID = readContentsAsString(HEAD_FILE);
        Commit preCommit = readObject(join(OBJECTS_DIR, preCommitID), Commit.class);
        // 利用AddStage和RemoveStage中的blobs对map进行修改
        Map<String, String> tempMap = preCommit.getFileNameToBlobID();
        AddStage.readFromFile();
        RemoveStage.readFromFile();
        if (AddStage.isEmpty() && RemoveStage.isEmpty()) {
            System.out.println(new GitletException("No changes added to the commit."));
        } else if (!AddStage.isEmpty() && RemoveStage.isEmpty()) {
            AddStage.modify(tempMap);
        } else if (AddStage.isEmpty() && !RemoveStage.isEmpty()) {
            RemoveStage.modify(tempMap);
        } else {
            AddStage.modify(tempMap);
            RemoveStage.modify(tempMap);
        }
        List<String> list = new ArrayList<>();
        list.add(preCommit.getID());
        // 创建新的commit 调用createCommit方法
        createNewCommit(message, tempMap, new Date(), list);
        AddStage.clear();
        RemoveStage.clear();
    }

    public static void remove(String fileName) {
        File file = join(CWD, fileName);
        Blob blob = Repository.findBlob(fileName);
        String commitID = readContentsAsString(HEAD_FILE);
        Commit headCommit = Repository.findCommit(commitID);
        AddStage.readFromFile();
        RemoveStage.readFromFile();
        if (AddStage.contain(blob)) {
            AddStage.remove(blob);
        } else if (headCommit.contain(blob) && file.exists()) {
            file.delete();
            RemoveStage.add(blob);
        } else if (headCommit.contain(blob) && !file.exists()) {
            RemoveStage.add(blob);
        } else if (!headCommit.contain(blob) && !AddStage.contain(blob)) {
            System.out.println(new GitletException("No reason to remove the file."));
        }
    }

    private static Commit findCommit(String commitID) {
        File file = join(OBJECTS_DIR, commitID);
        return readObject(file, Commit.class);
    }

    private static Blob findBlob(String fileName) {
        List<String> fileNames = plainFilenamesIn(OBJECTS_DIR);
        for (String filename : fileNames) {
            try {
                File f = join(OBJECTS_DIR, filename);
                Blob blob = readObject(f, Blob.class);
                if (fileName.equals(blob.getFileName())) {
                    return blob;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static void printLog() {
        String headCommitID = readContentsAsString(HEAD_FILE);
        Commit headCommit = findCommit(headCommitID);
        Commit Commit_p = headCommit;
        while (!Commit_p.getParentsID().isEmpty()) {
            Commit_p.print();
            Commit_p = findCommit(Commit_p.getParentsID().get(0));
        }
        Commit_p.print();
    }

    public static void printglobal_log() {
        List<String> list = plainFilenamesIn(OBJECTS_DIR);
        for (String s : list) {
            try {
                File f = join(OBJECTS_DIR, s);
                Commit commit = readObject(f, Commit.class);
                commit.print();
            } catch (Exception e) {
            }
        }
    }

    public static void findCommitByMessage(String message) {
        List<String> list = plainFilenamesIn(OBJECTS_DIR);
        for (String s : list) {
            try {
                File f = join(OBJECTS_DIR, s);
                Commit commit = readObject(f, Commit.class);
                if (message.equals(commit.getMessage())) {
                    System.out.println(commit.getID());
                }
            } catch (Exception e) {
            }
        }
    }
    public static void status() {
        List<String> branches = plainFilenamesIn(HEADS_DIR);
        System.out.println("=== Branches ===");
        for (String branch: branches) {
            if ("master".equals(branch)) {
                System.out.println("*master");
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
        System.out.println();
        AddStage.printStatus();
        RemoveStage.printStatus();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
        System.out.println();
    }

    // handle the 'checkout -- [filename]'
    public static void checkoutFile(String fileName) {
        checkoutFile(fileName, readContentsAsString(HEAD_FILE));
    }

    // handle the 'checkout [commitID] -- [filename]'
    public static void checkoutFileBycommitID(String fileName, String commitID) {
        checkoutFile(fileName, commitID);
    }

    // handle the 'checkout [branchname]'
    public static void checkoutBranch(String branchName) {
        File f = join(HEADS_DIR, branchName);
        if (!f.exists()) {
            throw new GitletException("No such branch exists.");
        }
        String commitID = readContentsAsString(f);
        Commit branchHead = readObject(join(OBJECTS_DIR, commitID), Commit.class);
        Commit currentHead = findCommit(readContentsAsString(HEAD_FILE));
        if (commitID.equals(readContentsAsString(HEAD_FILE))) {
            throw new GitletException("No need to checkout the current branch.");
        }
        // 用branchHead作为循环体
        for (String fileName: currentHead.getFileNameToBlobID().keySet()) {
            if (branchHead.getFileNameToBlobID().containsKey(fileName)) {
                if (!branchHead.getFileNameToBlobID().get(fileName).equals(currentHead.getFileNameToBlobID().get(fileName))) {
                    File file = join(OBJECTS_DIR, branchHead.getBlobIDByFileName(fileName));
                    Blob blob = readObject(file, Blob.class);
                    writeContents(join(CWD, fileName), blob.getContent());
                } else if (!branchHead.containsFile(fileName)) {
                    restrictedDelete(join(CWD, fileName));
                }
            }
        }
        for (String fileName: branchHead.getFileNameToBlobID().keySet()) {
            if (!currentHead.containsFile(fileName)) {
                File file = join(CWD, fileName);
                if (file.exists()) {
                    throw new GitletException("There is an untracked file in the wat; delete it, or add and commit it first.");
                } else {
                    try {
                        file.createNewFile();
                        Blob blob = readObject(join(OBJECTS_DIR, branchHead.getBlobIDByFileName(fileName)), Blob.class);
                        writeContents(file, blob.getContent());
                    } catch (IOException e) {
                    }
                }
            }
        }
        writeContents(HEAD_FILE, commitID);
        AddStage.clear();
        RemoveStage.clear();
    }

    private static void checkoutFile(String fileName, String commitID) {
        Commit commit = findCommit(commitID);
        if (!commit.containsFile(fileName) && commitID == readContentsAsString(HEAD_FILE)) {
            throw new GitletException("Files does not exist in that commit.");
        } else {
            File f = join(CWD, fileName);
            String blobID = commit.getBlobIDByFileName(fileName);
            Blob blob = findBlobByID(blobID);
            if (f.exists()) {
                writeContents(f, blob.getContent());
            } else {
                try {
                    f.createNewFile();
                    writeContents(f, blob.getContent());
                } catch (IOException e) {
                }
            }
        }
    }

    private static Blob findBlobByID(String blobID) {
        File f = join(OBJECTS_DIR, blobID);
        return readObject(f, Blob.class);
    }

    public static void removeBranch(String branchName) {
        // step1: 检查给定的branchname是否存在
        File f = join(HEADS_DIR, branchName);
        if (!f.exists()) {
            throw new GitletException("A branch with that name does not exist.");
        } else {
            Commit branchHeadCommit = readObject(join(OBJECTS_DIR, readContentsAsString(f)), Commit.class);
            Commit HEADCommit = readObject(join(OBJECTS_DIR, readContentsAsString(HEAD_FILE)), Commit.class);
            Commit temp = branchHeadCommit;
            while (!temp.getParentsID().isEmpty()) {
                if (temp.getID().equals(HEADCommit.getID())) {
                    throw new GitletException("Cannot remove the current branch.");
                }
            }
            restrictedDelete(f);
        }
    }

    public static void createBranch(String branchName) {
        File f = join(HEADS_DIR, branchName);
        try {
            f.createNewFile();
            writeContents(f, readContentsAsString(HEAD_FILE));
        } catch (IOException e) {
        }
    }

    public static void reset(String commitID) {
        File f = join(OBJECTS_DIR, commitID);
        if (!f.exists()) {
            throw new GitletException("No commit with that id exists.");
        }
        Commit currentCommit = readObject(HEAD_FILE, Commit.class);
        Commit targetCommit = readObject(f, Commit.class);
        writeContents(HEAD_FILE, commitID);
        for (String branchName: plainFilenamesIn(HEADS_DIR)) {
            if (commitID.equals(readContentsAsString(join(HEADS_DIR, branchName)))) {
                checkoutBranch(branchName);
                return;
            }
        }
        for (String fileName: currentCommit.getFileNameToBlobID().keySet()) {
            if (targetCommit.containsFile(fileName)) {
                if (!targetCommit.getBlobIDByFileName(fileName).equals(currentCommit.getBlobIDByFileName(fileName))) {
                    File file = join(CWD, fileName);
                    writeContents(f, findBlobByID(targetCommit.getBlobIDByFileName(fileName)));
                }
            } else {
                File file = join(CWD, fileName);
                restrictedDelete(file);
            }
        }
        for (String fileName: targetCommit.getFileNameToBlobID().keySet()) {
            if (!currentCommit.containsFile(fileName)) {
                File file = join(CWD, fileName);
                try {
                    file.createNewFile();
                    Blob blob = findBlobByID(targetCommit.getBlobIDByFileName(fileName));
                    writeContents(file, blob.getContent());
                } catch (IOException e) {
                }
            }
        }
        AddStage.clear();
        RemoveStage.clear();
    }

    public static void merge(String branchName) {
        AddStage.readFromFile();
        RemoveStage.readFromFile();
        if (!AddStage.isEmpty() || !RemoveStage.isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        }
        File f = join(HEADS_DIR, branchName);
        if (!f.exists()) {
            throw new GitletException("A branch with that name does not exist.");
        }
        String mergedCommitId = readContentsAsString(f);
        Commit mergedCommit = findCommit(readContentsAsString(f));
        assert mergedCommit != null;
        if (mergedCommitId.equals(readContentsAsString(HEAD_FILE))) {
            throw new GitletException("Cannot merge a branch with itself.");
        }
        String currentCommitId = readContentsAsString(HEAD_FILE);
        assert currentCommitId != null;
        Commit currentCommit = findCommit(currentCommitId);
        assert currentCommit != null;
        List<String> cwdFileNames = Utils.plainFilenamesIn(CWD);
        assert cwdFileNames != null;
        List<String> untrackedFiles = getUntrackedFiles(currentCommit, cwdFileNames);
        if (!untrackedFiles.isEmpty()) {
            for (String untrackedFileName : untrackedFiles) {
                if (mergedCommit.getBlobIDByFileName(untrackedFileName) != null) {
                    throw new GitletException("There is an untracked file in the way; delete it, or add and commit " + "it first.");
                }
            }
        }
        if (!getUntrackedFiles(currentCommit, cwdFileNames).isEmpty()) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first" + ".");
        }
        String splitPointCommitId = getSplitPointCommitId(currentCommitId, mergedCommitId);
        // If the split point is the same commit as the given branch, then we do nothing;
        if (splitPointCommitId.equals(mergedCommitId)) {
            throw new GitletException("Given branch is an ancestor of the current branch.");
        }
        //  If the split point is the current branch, then the effect is to check out the given
        //  branch
        if (splitPointCommitId.equals(currentCommitId)) {
            checkoutBranch(branchName);
            throw new GitletException("Current branch fast-forwarded.");
        }
        Commit splitPointCommit = findCommit(splitPointCommitId);
        assert splitPointCommit != null;
        boolean conflict = processMerge(splitPointCommit, currentCommit, mergedCommit);
        List<String> parentsId = new ArrayList<>();
        parentsId.add(currentCommitId);
        parentsId.add(mergedCommitId);
        createNewCommit(("Merged " + branchName + " into " + getBranchName(currentCommitId)), mergedCommit.getFileNameToBlobID(), new Date(), parentsId);
        if (conflict) {
            throw new GitletException("Encountered a merge conflict.");
        }

    }

    private static String getBranchName(String commitId) {
        for (String fileName: plainFilenamesIn(HEADS_DIR)) {
            if (commitId.equals(readContentsAsString(join(HEADS_DIR, fileName)))) {
                return fileName;
            }
        }
        return null;
    }

    private static String getSplitPointCommitId(String currentCommitId, String id) {
        Set<String> commitSet = new HashSet<>();
        Queue<String> bfsQueue = new ArrayDeque<>();
        bfsQueue.add(currentCommitId);
        while (!bfsQueue.isEmpty()) {
            String commitId = bfsQueue.remove();
            Commit commit = findCommit(commitId);
            commitSet.add(commitId);
            if (!commit.getParentsID().isEmpty()) {
                if (commit.getParentsID().size() == 1) {
                    bfsQueue.add(commit.getParentsID().get(0));
                } else if (commit.getParentsID().size() == 2) {
                    bfsQueue.add(commit.getParentsID().get(0));
                    bfsQueue.add(commit.getParentsID().get(1));
                }
            }
        }

        bfsQueue.add(id);
        while (!bfsQueue.isEmpty()) {
            String commitId = bfsQueue.remove();
            Commit commit = findCommit(commitId);
            if (commitSet.contains(commitId)) {
                return commitId;
            }
            if (!commit.getParentsID().isEmpty()) {
                if (commit.getParentsID().size() == 1) {
                    bfsQueue.add(commit.getParentsID().get(0));
                } else if (commit.getParentsID().size() == 2) {
                    bfsQueue.add(commit.getParentsID().get(0));
                    bfsQueue.add(commit.getParentsID().get(1));
                }
            }
        }
        return null;
    }

    private static List<String> getUntrackedFiles(Commit commit, List<String> fileNames) {
        List<String> result = new ArrayList<>();
        AddStage.readFromFile();
        for (String fileName: fileNames) {
            if (commit.containsFile(fileName) && AddStage.getFileNameToBlobID().containsKey(fileName)) {
                result.add(fileName);
            }
        }
        Collections.sort(result);
        return result;
    }

    private static boolean processMerge(Commit splitPointCommit,
                                        Commit currentCommit, Commit mergedCommit) {
        boolean conflict = false;
        HashMap<String, String> splitBlobs = (HashMap<String, String>) splitPointCommit.getFileNameToBlobID();
        HashMap<String, String> currentBlobs = (HashMap<String, String>) currentCommit.getFileNameToBlobID();
        HashMap<String, String> mergedBlobs = (HashMap<String, String>) mergedCommit.getFileNameToBlobID();
        for (String fileName : mergedBlobs.keySet()) {
            // modified in the given branch since the split point
            String mergedBlobId = mergedBlobs.get(fileName);
            String splitBlobId = splitBlobs.get(fileName);
            String currentBlobId = currentBlobs.get(fileName);
            // case1: modify in mergedBranch but not in HEAD -> other
            if (splitBlobId != null && !mergedBlobId.equals(splitBlobId)) {
                if (splitBlobId.equals(currentBlobId)) {
                    checkoutFile(fileName, mergedCommit.getID());
                    AddStage.add(findBlob(mergedCommit.getBlobIDByFileName(fileName)));
                    continue;
                }
            }
            // case3: modify in HEAD and mergedBranch in the same way -> keep same

            // case5: not in split nor HEAD but in other -> other 
            if (splitBlobId == null && currentBlobId == null) {
                checkoutFile(fileName, mergedCommit.getID());
                AddStage.add(findBlob(mergedCommit.getBlobIDByFileName(fileName)));
                continue;
            }
            // case7: unmodified in mergedBranch but not present in HEAD -> keep same

            // case8: or the contents of one are changed and the other file is deleted,
            if (splitBlobId != null && !mergedBlobId.equals(splitBlobId) && currentBlobId == null) {
                conflict = true;
                processConflict(fileName, currentBlobId, mergedBlobId);
            }
        }

        for (String fileName : currentBlobs.keySet()) {
            String currentBlobId = currentBlobs.get(fileName);
            String splitBlobId = splitBlobs.get(fileName);
            String mergedBlobId = mergedBlobs.get(fileName);
            // case2: keep same
            // case4: keep same
            // case6: Any files present at the split point, unmodified in the current branch, and
            // absent in the given branch should be removed (and untracked).
            if (currentBlobId.equals(splitBlobId)) {
                if (mergedBlobId == null) {
                    Utils.join(CWD, fileName).delete();
                    RemoveStage.add(findBlobByID(currentBlobId));
                    continue;
                }
            }
            // case8: the contents of both are changed and different from other
            if (splitBlobId != null && mergedBlobId != null) {
                if (!currentBlobId.equals(splitBlobId) && !mergedBlobId.equals(splitBlobId)) {
                    if (!currentBlobId.equals(mergedBlobId)) {
                        conflict = true;
                        processConflict(fileName, currentBlobId, mergedBlobId);
                    }
                }
            }
            // case8: or the contents of one are changed and the other file is deleted,
            if (splitBlobId != null && !currentBlobId.equals(splitBlobId) && mergedBlobId == null) {
                conflict = true;
                processConflict(fileName, currentBlobId, mergedBlobId);
            }
            // case8: or the file was absent at the split point and has different contents in the
            // given and current branches.
            if (splitBlobId == null && currentBlobId != null && mergedBlobId != null) {
                if (!currentBlobId.equals(mergedBlobId)) {
                    conflict = true;
                    processConflict(fileName, currentBlobId, mergedBlobId);
                }
            }
        }
        return conflict;
    }

    private static void processConflict(String fileName, String currentBlobId, String mergedBlobId) {
        String newContents = conflictFileContents(currentBlobId, mergedBlobId);
        Blob newBlob = new Blob(fileName, newContents.getBytes(StandardCharsets.UTF_8));
        newBlob.save();
        File file = Utils.join(CWD, fileName);
        Utils.writeContents(file, newContents);
        AddStage.add(newBlob);
    }

    private static String conflictFileContents(String currentBlobId, String mergedBlobId) {
        String currentContents;
        String mergedContents;
        if (currentBlobId == null) {
            currentContents = "";
        } else {
            currentContents = Utils.readContentsAsString(Utils.join(OBJECTS_DIR, currentBlobId));
        }
        if (mergedBlobId == null) {
            mergedContents = "";
        } else {
            mergedContents = Utils.readContentsAsString(Utils.join(OBJECTS_DIR, mergedBlobId));
        }
        return "<<<<<<< HEAD\n" + currentContents + "=======\n" + mergedContents + ">>>>>>>\n";
    }
}