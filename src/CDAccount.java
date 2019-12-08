import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CDAccount extends Account {
    private Calendar maturityDate;

    public CDAccount(String Date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date oDate = sdf.parse(Date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(oDate);
        this.maturityDate = cal;
    }

    public CDAccount(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus) {
        super(acctNumber, acctType, acctBalance, personInfo, acctStatus);
    }

    public CDAccount(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus, Calendar maturityDate) {
        super(acctNumber, acctType, acctBalance, personInfo, acctStatus);
        this.maturityDate = maturityDate;
    }
    @Override
    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj) {
        TransactionReceipt cdRec;
        Calendar timeNow = Calendar.getInstance();
        Calendar newDate = Calendar.getInstance();

        String acctType = this.getAccountType();

        if(this.getAccountStatus()){
            if(this.getMaturityDate().before(timeNow) || this.getMaturityDate().equals(timeNow)){
                if(ticketInfo.getAmountOfTransaction() <= 0.00){
                    String reason = "Invalid amount.";
                    cdRec = new TransactionReceipt(ticketInfo,false,reason);
                    this.addTransaction(cdRec);
                    return  cdRec;
                }else{
                    double acctBalance = this.getAccountBalance();
                    double newBalance = acctBalance + ticketInfo.getAmountOfTransaction();
                    newDate.add(Calendar.MONTH,ticketInfo.getTermOfCD());
                    cdRec = new TransactionReceipt(ticketInfo,true,acctBalance,newBalance,newDate);
                    this.setAccountBalance(newBalance);
                    obj.checkTypeDeposit(acctType,ticketInfo.getAmountOfTransaction());
                    this.addTransaction(cdRec);
                    return cdRec;
                }
            }else{
                String reason = "Term has not ended.";
                cdRec = new TransactionReceipt(ticketInfo,false,reason);
                this.addTransaction(cdRec);
                return cdRec;
            }
        }else{
            String reason = "Account is closed.";
            cdRec = new TransactionReceipt(ticketInfo,false,reason);
            this.addTransaction(cdRec);
            return cdRec;
        }
    }

    @Override
    public TransactionReceipt makeWithdrawal(TransactionTicket ticketInfo, Bank obj, int index) {
        TransactionReceipt cdRec;
        Calendar timeNow = Calendar.getInstance();
        Calendar newDate = Calendar.getInstance();
        Account accInfo = obj.getAccts(index);

        double balance = accInfo.getAccountBalance();

        if(accInfo.getAccountStatus()){
            if(this.maturityDate.before(timeNow) || this.maturityDate.equals(timeNow)){
                if(ticketInfo.getAmountOfTransaction() <= 0.00){
                    String reason = "Invalid amount.";
                    cdRec = new TransactionReceipt(ticketInfo,false,reason);
                    accInfo.addTransaction(cdRec);
                    return  cdRec;
                }else if(ticketInfo.getAmountOfTransaction() > balance)
                {
                    String reason = "Balance has insufficient funds.";
                    cdRec = new TransactionReceipt(ticketInfo,false,reason,balance);
                    accInfo.addTransaction(cdRec);
                    return cdRec;
                }else{
                    double newBalance = balance - ticketInfo.getAmountOfTransaction();
                    newDate.add(Calendar.MONTH,ticketInfo.getTermOfCD());
                    cdRec = new TransactionReceipt(ticketInfo,true,balance,newBalance,newDate);
                    accInfo.setAccountBalance(newBalance);
                    obj.checkTypeWithdraw(accInfo.getAccountType(),ticketInfo.getAmountOfTransaction());
                    accInfo.addTransaction(cdRec);
                    return cdRec;
                }
            }else{
                String reason = "Term has not ended.";
                cdRec = new TransactionReceipt(ticketInfo,false,reason);
                accInfo.addTransaction(cdRec);
                return cdRec;
            }
        }else{
            String reason = "Account is closed.";
            cdRec = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(cdRec);
            return cdRec;
        }
    }

    public Calendar getMaturityDate(){
        return maturityDate;
    }
}
