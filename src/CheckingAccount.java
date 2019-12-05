import java.sql.SQLOutput;
import java.util.Calendar;

public class CheckingAccount extends Account {

    public CheckingAccount() {
        super();
    }

    public CheckingAccount(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus) {
        super(acctNumber, acctType, acctBalance, personInfo, acctStatus);
    }

    @Override
    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj, int index) {
        TransactionReceipt newRec;
        Account accInfo = obj.getAccts(index);
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

    public TransactionReceipt clearCheck(Check checkInfo, TransactionTicket info, Bank acc, int index) {
        TransactionReceipt clearedCheck;
        Account bal = acc.getAccts(index);

        Calendar timeNow = Calendar.getInstance();
        Calendar beforeSixMonths = Calendar.getInstance();
        beforeSixMonths.add(Calendar.MONTH, -6);
        Calendar check = checkInfo.getDateOfCheck();

        check.add(Calendar.MONTH, 6);
        System.out.println(check.getTime());
        timeNow.clear(Calendar.HOUR_OF_DAY);
        timeNow.clear(Calendar.HOUR);
        timeNow.clear(Calendar.AM_PM);
        timeNow.clear(Calendar.MINUTE);
        timeNow.clear(Calendar.SECOND);
        timeNow.clear(Calendar.MILLISECOND);
        System.out.println("Time now " + timeNow.getTime());

        if (bal.getAccountStatus()) {
            if (timeNow.before(check) || timeNow.equals(check)) {

                double drawAmount = checkInfo.getCheckAmount();
                bal = acc.getAccts(index);
                double balance = bal.getAccountBalance();

                if (drawAmount <= 0.0) {
                    String reason = "Trying to withdraw invalid amount.";
                    clearedCheck = new TransactionReceipt(info, false, reason, balance);
                    bal.addTransaction(clearedCheck);
                    return clearedCheck;
                } else if (drawAmount > balance) {
                    String reason = "Balance has insufficient funds. You have been charged a $2.50 service fee. ";
                    final double fee = 2.50;
                    double newBal = balance - fee;
                    clearedCheck = new TransactionReceipt(info, false, reason, balance, newBal);
                    bal.setAccountBalance(newBal);
                    acc.checkTypeWithdraw(bal.getAccountType(), fee);
                    bal.addTransaction(clearedCheck);
                    return clearedCheck;
                } else {
                    double newBal = balance - drawAmount;
                    clearedCheck = new TransactionReceipt(info, true, balance, newBal);
                    bal.setAccountBalance(newBal);
                    acc.checkTypeWithdraw(bal.getAccountType(), drawAmount);
                    bal.addTransaction(clearedCheck);
                    return clearedCheck;
                }
            } else {
                String reason = "The date on the check is more than 6 months ago.";
                clearedCheck = new TransactionReceipt(info, false, reason);
                return clearedCheck;
            }
        } else {
            String reason = "Account is closed.";
            clearedCheck = new TransactionReceipt(info, false, reason);
            return clearedCheck;
        }
    }

}
