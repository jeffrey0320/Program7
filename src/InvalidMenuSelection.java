public class InvalidMenuSelection extends Exception{
    public InvalidMenuSelection() {
        super("Error: Invalid menu selection");
    }

    public InvalidMenuSelection(char select) {
        super("Error: " + select + " is an invalid selection");
    }
}
