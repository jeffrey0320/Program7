public class InvalidAccountException extends Exception {
    public InvalidAccountException() {
        super("Error: Account does not exist");
    }

    public InvalidAccountException(int acctnum) {
        super("Error: Account " + acctnum + " does not exist \n");
    }
}
