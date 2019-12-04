public class SavingsAccount extends Account {

    public SavingsAccount() {
        super();
    }

    public SavingsAccount(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus) {
        super(acctNumber, acctType, acctBalance, personInfo, acctStatus);
    }

    @Override
    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj, int index) {
        TransactionReceipt newRec;
        Account accInfo;
        accInfo = obj.getAccts(index);
        String accType = accInfo.getAccountType();

        if(accInfo.getAccountStatus()){
            if(ticketInfo.getAmountOfTransaction() <= 0.00){
                String reason = "Invalid amount.";
                newRec = new TransactionReceipt(ticketInfo,false,reason);
                accInfo.addTransaction(newRec);
                return  newRec;
            }else{
                double balance = accInfo.getAccountBalance();
                double newBalance = balance + ticketInfo.getAmountOfTransaction();
                newRec = new TransactionReceipt(ticketInfo,true,balance,newBalance);
                accInfo.setAccountBalance(newBalance);
                obj.checkTypeDeposit(accType,ticketInfo.getAmountOfTransaction());
                accInfo.addTransaction(newRec);
                return newRec;
            }
        }else{
            String reason = "Account is closed.";
            newRec = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(newRec);
            return newRec;
        }
    }

    @Override
    public TransactionReceipt makeWithdrawal(TransactionTicket ticketInfo, Bank obj, int index) {
        TransactionReceipt newRec;
        Account bal = obj.getAccts(index);
        double balance = bal.getAccountBalance();

        if(bal.getAccountStatus()){
            if(ticketInfo.getAmountOfTransaction() <= 0.0) {
                String reason = "Trying to withdraw invalid amount.";
                newRec = new TransactionReceipt(ticketInfo,false,reason,balance);
                bal.addTransaction(newRec);
                return newRec;
            }
            else if(ticketInfo.getAmountOfTransaction() > balance) {
                String reason = "Balance has insufficient funds.";
                newRec = new TransactionReceipt(ticketInfo,false,reason,balance);
                bal.addTransaction(newRec);
                return newRec;
            }
            else {
                double newBal = balance - ticketInfo.getAmountOfTransaction();
                newRec = new TransactionReceipt(ticketInfo,true,balance,newBal);
                bal.setAccountBalance(newBal);
                obj.checkTypeWithdraw(bal.getAccountType(),ticketInfo.getAmountOfTransaction());
                bal.addTransaction(newRec);
                return newRec;
            }
        }else{
            String reason = "Account is closed.";
            newRec = new TransactionReceipt(ticketInfo,false,reason);
            bal.addTransaction(newRec);
            return newRec;
        }
    }

}
