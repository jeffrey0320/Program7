import java.util.ArrayList;

public class Bank {
    private ArrayList<Account> arrayOfAccounts;
    // static variables
    private static double totalAmountInSavingsAccts = 0.0;
    private static double totalAmountInCheckingAccts = 0.0;
    private static double totalAmountInCDAccts = 0.0;
    private static double totalAmountInAllAccts = 0.0;

    public Bank(){
        arrayOfAccounts = new ArrayList<>();
    }

    public static void addAmountOfSavingsAccts(double amount){
        totalAmountInSavingsAccts += amount;
        addAmountOfAllAccts();
    }

    public static void addAmountOfCheckingAccts(double amount){
        totalAmountInCheckingAccts += amount;
        addAmountOfAllAccts();
    }

    public static void addAmountOfCDAccts(double amount){
        totalAmountInCDAccts += amount;
        addAmountOfAllAccts();
    }

    public static void addAmountOfAllAccts(){
        totalAmountInAllAccts = totalAmountInCDAccts + totalAmountInCheckingAccts + totalAmountInSavingsAccts;
    }

    public static void subtractAmountOfSavingsAccts(double amount){
        totalAmountInSavingsAccts -= amount;
        addAmountOfAllAccts();
    }

    public static void subtractAmountOfCheckingAccts(double amount){
        totalAmountInCheckingAccts -= amount;
        addAmountOfAllAccts();
    }

    public static void subtractAmountOfCDAccts(double amount){
        totalAmountInCDAccts -= amount;
        addAmountOfAllAccts();
    }

    public void openNewAccount(Account customerInfo){
        arrayOfAccounts.add(customerInfo);
    }

    public TransactionReceipt openNewAccount(TransactionTicket ticket, Bank obj, String[] customerInfo, int acctNum){
        TransactionReceipt openRec;
        Account allInfo;
        final int INITIAL_BAL = 0;

        boolean isEmpty = false;
        for(int i=0;i<customerInfo.length-1;i++){
            if(customerInfo[i].isEmpty()) {
                isEmpty = true;
                break;
            }
        }
        if(isEmpty){
            String reason = "Missing information";
            openRec = new TransactionReceipt(ticket,false,reason);
            return openRec;
        }else{
            Name newName = new Name(customerInfo[0],customerInfo[1]);
            Depositor newInfo = new Depositor(customerInfo[2],newName);
            if(customerInfo[3].equals("Checking")){
                allInfo = new CheckingAccount(acctNum,customerInfo[3],INITIAL_BAL,newInfo,true);
            }else if(customerInfo[3].equals("Savings")) {
                allInfo = new SavingsAccount(acctNum,customerInfo[3],INITIAL_BAL,newInfo,true);
            }else{
                allInfo = new CDAccount(acctNum,customerInfo[3],INITIAL_BAL,newInfo,true);
            }
            obj.arrayOfAccounts.add(arrayOfAccounts.size(),allInfo);
            openRec = new TransactionReceipt(ticket,true,0);
            return openRec;
        }
    }

    public TransactionReceipt deleteAccount(TransactionTicket ticket, Bank bankObj, int index){
        TransactionReceipt delAcct;
        String reason;

        if(bankObj.getAccts(index).getAccountBalance()<0){
            reason = "Account has a negative balance and cannot be deleted.";
            delAcct = new TransactionReceipt(ticket,false,reason);
            return delAcct;
        }else if(bankObj.getAccts(index).getAccountBalance()>0){
            reason = "Account has a balance, Withdraw your balance and try again.";
            delAcct = new TransactionReceipt(ticket,false,reason);
            return delAcct;
        }else {
            arrayOfAccounts.remove(index);
            delAcct = new TransactionReceipt(ticket,true);
            return delAcct;
        }
    }

    public int findAcct(int reqAccount) throws InvalidAccountException {
        for (int index = 0; index < arrayOfAccounts.size(); index++) {
            if (arrayOfAccounts.get(index).getAccountNumber() == reqAccount) {
                return index;
            } else {
                throw new InvalidAccountException(arrayOfAccounts.get(index).getAccountNumber());
            }
        }
        return -1;
    }

    public int acctUsingSSN(String reqAccount) {
        for (int index = 0; index < arrayOfAccounts.size(); index++)
            if (arrayOfAccounts.get(index).getPersonInfo().getSSN().equals(reqAccount))
                return index;
        return -1;
    }

    public Account getAccts(int index){
        return arrayOfAccounts.get(index);
    }

    public int getNumAccts(){
        return arrayOfAccounts.size();
    }

    public static double getTotalAmountInSavingsAccts() {
        return totalAmountInSavingsAccts;
    }

    public static double getTotalAmountInCheckingAccts() {
        return totalAmountInCheckingAccts;
    }

    public static double getTotalAmountInCDAccts() {
        return totalAmountInCDAccts;
    }

    public static double getTotalAmountInAllAccts() {
        return totalAmountInAllAccts;
    }

    public void checkTypeDeposit(String type, double amount){
        if(type.equals("Savings"))
            addAmountOfSavingsAccts(amount);
        else if(type.equals("Checking"))
            addAmountOfCheckingAccts(amount);
        else
            addAmountOfCDAccts(amount);
    }

    public void checkTypeWithdraw(String type, double amount){
        if(type.equals("Savings"))
            subtractAmountOfSavingsAccts(amount);
        else if(type.equals("Checking"))
            subtractAmountOfCheckingAccts(amount);
        else
            subtractAmountOfCDAccts(amount);
    }

}
