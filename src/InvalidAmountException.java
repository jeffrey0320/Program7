public class InvalidAmountException extends Exception {

    public InvalidAmountException() {
        super("Error: Amount is Invalid");
    }

    public InvalidAmountException(double amount) {
        super("Error: Amount " + amount + " is Invalid\n");
    }

}
