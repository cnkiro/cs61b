package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            // exit
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if (Repository.isInit()) {
                    Utils.message("A Gitlet version-control system already exists in the current directory");
                } else {
                    Repository.initialize();
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Repository.addFile(args[1]);
                break;
            case "commit":
                if (args.length == 1) {
                    Utils.message("Please enter a commit message.");
                }
                // TODO: handle the 'commit' command
                Repository.makeCommit(args[1]);
            case "rm":
                // TODO: handle the 'rm [filename]' command
                Repository.removeFile(args[1]);
            case "log":
                // TODO: handle the 'log' command
                Repository.printLog();
        }
    }
}
