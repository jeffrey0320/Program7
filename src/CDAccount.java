import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CDAccount extends Account {
    private Calendar maturityDate;

    public CDAccount(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus, Calendar maturityDate) {
        super(acctNumber, acctType, acctBalance, personInfo, acctStatus);
        this.maturityDate = maturityDate;
    }


    @Override
    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj, int index) {
        TransactionReceipt newRec;
        Account accInfo = obj.getAccts(index);

        if(accInfo.getAccountType().equals("CD")){//Account is type CD
            if(accInfo.getAccountStatus()){ // account open
                if(ticketInfo.getAmountOfTransaction() <= 0.0){
                    if(ticketInfo.getAmountOfTransaction() <= 0.00){
                        String reason = "Invalid amount.";
                        newRec = new TransactionReceipt(ticketInfo,false,reason);
                        return  newRec;
                }else{
                        double balance = accInfo.getAccountBalance();
                        double newBalance = balance + ticketInfo.getAmountOfTransaction();
                        newRec = new TransactionReceipt(ticketInfo,true,balance,newBalance);
                        accInfo.setAccountBalance(newBalance);
                        obj.checkTypeDeposit(accType,ticketInfo.getAmountOfTransaction());
                        return newRec;
                }
            }else{
                // account closed
            }
        }else{
            //not CD account
        }
        return ;
    }

}
