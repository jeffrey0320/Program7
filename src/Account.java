import java.util.ArrayList;

public abstract class Account {
    private Depositor personInfo;
    private int acctNumber;
    private String acctType;
    private boolean acctStatus;
    private double acctBalance;
    private ArrayList<TransactionReceipt> arrayOfReceipts;

    //no-args constructor
    public Account() {
        personInfo = new Depositor();
        this.acctNumber = 0;
        this.acctBalance = 0;
        arrayOfReceipts = new ArrayList<>();
    }

    public Account(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus){
        this.acctNumber = acctNumber;
        this.acctType = acctType;
        this.acctBalance = acctBalance;
        this.personInfo = personInfo;
        this.acctStatus = acctStatus;
        arrayOfReceipts = new ArrayList<>();
    }

    public Account(String s, String token, String s1, String s2, int acctNum,boolean status) {
        personInfo = new Depositor(s1,s,token);
        acctType = s2;
        acctNumber = acctNum;
        acctStatus = status;
    }
    // copy constructor
    public Account(Account copy){
        this.personInfo = copy.personInfo;
        this.acctNumber = copy.acctNumber;
        this.acctType = copy.acctType;
        this.acctStatus = copy.acctStatus;
        this.acctBalance = copy.acctBalance;
        this.arrayOfReceipts = copy.arrayOfReceipts;
    }

    public TransactionReceipt getBalance(TransactionTicket ticketInfo){
        TransactionReceipt newRec;
        
            if(this.getAccountStatus()){
                double balance = this.getAccountBalance();
                newRec = new TransactionReceipt(ticketInfo, true, balance);
                this.addTransaction(newRec);
                return  newRec;
            }else{
                String reason = "Account is closed.";
                newRec = new TransactionReceipt(ticketInfo,false,reason);
                this.addTransaction(newRec);
                return newRec;
            }
    }

    public abstract TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj) throws InvalidAmountException;

    public abstract TransactionReceipt makeWithdrawal(TransactionTicket ticketInfo, Bank obj, int index);

    public TransactionReceipt closeAccount(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt close;
        Account accInfo = obj.getAccts(index);

        if(accInfo.getAccountBalance()>0){
            String reason = "Account cant be close, Withdraw first.";
            close = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(close);
            return close;
        }else{
            if(obj.getAccts(index).acctStatus){
                accInfo = obj.getAccts(index);
                accInfo.setAccountStatus(false);
                close = new TransactionReceipt(ticketInfo,true);
                accInfo.addTransaction(close);
                return close;
            }else{
                String reason = "Account is closed already.";
                close = new TransactionReceipt(ticketInfo,false,reason);
                accInfo.addTransaction(close);
                return close;
            }
        }
    }

    public TransactionReceipt reopenAccount(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt reOpen;
        Account accInfo = obj.getAccts(index);

        if(obj.getAccts(index).acctStatus){
            String reason = "Account is active.";
            reOpen = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(reOpen);
            return reOpen;
        }else{
            accInfo.setAccountStatus(true);
            reOpen = new TransactionReceipt(ticketInfo,true);
            accInfo.addTransaction(reOpen);
            return reOpen;
        }
    }

    public ArrayList<TransactionReceipt> getTransactionHistory(TransactionTicket ticket, Bank obj, int index) {
        Account accInfo = obj.getAccts(index);
        ArrayList<TransactionReceipt> allReceipts = accInfo.arrayOfReceipts;
        return allReceipts;
    }

    private void setAccountStatus(boolean b) {
        acctStatus = b;
    }

    public Depositor getPersonInfo() {
        return new Depositor(personInfo);
    }

    public int getAccountNumber() {
        return acctNumber;
    }

    public String getAccountType() {
        return acctType;
    }

    public boolean getAccountStatus() {
        return acctStatus;
    }

    public double getAccountBalance() {
        return acctBalance;
    }

    public void addTransaction(TransactionReceipt receipt){
        arrayOfReceipts.add(receipt);
    }

    protected void setAccountBalance(double amount){
        this.acctBalance = amount;
    }

    private boolean equals(Account accInfo){
        if(acctNumber == accInfo.acctNumber)
            return true;
        else
            return false;
    }
    // Account toString override
    public String toString(){
        Name myName = personInfo.getPersonName();

        String str = String.format("%-12s%-12s%-9s%13s%19s%-3s$%9.2f",
                myName.getFirstName(),
                myName.getLastName(),
                personInfo.getSSN(),
                this.acctNumber,
                this.acctType, " ",
                this.acctBalance);
        return str;
    }

    public String toStringAccInfo(){
        Name myName = personInfo.getPersonName();

        String str =  "Name: " + myName.getFirstName() +" "+ myName.getLastName() +"\n"+
                "Social secruity number: " + personInfo.getSSN() +"\n"+
                "Account number: " + this.acctNumber +"\n"+
                "Account type: " + this.acctType +"\n"+
                String.format("Account Balance: $%.2f\n", this.acctBalance);
        return str;
    }
}
