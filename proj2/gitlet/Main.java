package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.initializeRep();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Repository.addFile(args[1]);
                break;
            case "commit":
                // TODO: handle the 'commit [message]' command
                if (args.length == 1) {
                    System.out.println(new GitletException("Please enter a commit message").getMessage());
                }
                Repository.makeCommit(args[1]);
                break;
            case "rm":
                // TODO: handle the 'rm [filename]' command
                Repository.remove(args[1]);
                break;
            case "log":
                // TODO: handle the 'log' command
                Repository.printLog();
                break;
            case "global-log":
                // TODO: handle the 'global-log' command
                Repository.printglobal_log();
                break;
            case "find":
                // TODO: handle the 'find [commitmessage]' command
                Repository.findCommitByMessage(args[1]);
                break;
            case "status":
                // TODO: handle the 'status' command
                Repository.status();
                break;
            case "checkout":
                // TODO: handle the 'checkout [filename]' command
                if (args.length == 3) {
                    Repository.checkoutFile(args[2]);
                } else if (args.length == 4) {
                    Repository.checkoutFileBycommitID(args[3], args[1]);
                } else if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                }
                break;
            case "branch":
                // TODO: handle the 'branch [branchname]' command
                Repository.createBranch(args[1]);
                break;
            case "rm-branch":
                // TODO: handle the 'rm-branch [branchname]' command
                Repository.removeBranch(args[1]);
                break;
            case "reset":
                // TODO: handle the 'reset [commitID]' command
                Repository.reset(args[1]);
            case "merge":
                // TODO: handle the 'merge [branchname]' command
                Repository.merge(args[1]);
        }
    }
}