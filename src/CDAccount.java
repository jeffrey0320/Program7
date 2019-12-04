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
    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj, int index) {
        TransactionReceipt cdRec;
        Calendar timeNow = Calendar.getInstance();
        Calendar newDate = Calendar.getInstance();
        Account accInfo = obj.getAccts(index);

        String acctType = accInfo.getAccountType();

        if(accInfo.getAccountStatus()){
            if(this.getMaturityDate().before(timeNow) || this.getMaturityDate().equals(timeNow)){
                if(ticketInfo.getAmountOfTransaction() <= 0.00){
                    String reason = "Invalid amount.";
                    cdRec = new TransactionReceipt(ticketInfo,false,reason);
                    //accInfo.addTransaction(cdRec);
                    return  cdRec;
                }else{
                    double acctBalance = accInfo.getAccountBalance();
                    double newBalance = acctBalance + ticketInfo.getAmountOfTransaction();
                    newDate.add(Calendar.MONTH,ticketInfo.getTermOfCD());
                    cdRec = new TransactionReceipt(ticketInfo,true,acctBalance,newBalance,newDate);
                    accInfo.setAccountBalance(newBalance);
                    obj.checkTypeDeposit(acctType,ticketInfo.getAmountOfTransaction());
                    //accInfo.addTransaction(cdRec);
                    return cdRec;
                }
            }else{
                String reason = "Term has not ended.";
                cdRec = new TransactionReceipt(ticketInfo,false,reason);
                //accInfo.addTransaction(cdRec);
                return cdRec;
            }
        }else{
            String reason = "Account is closed.";
            cdRec = new TransactionReceipt(ticketInfo,false,reason);
            //accInfo.addTransaction(cdRec);
            return cdRec;
        }
    }

    @Override
    public TransactionReceipt makeWithdrawal(TransactionTicket ticketInfo, Bank obj,
                                             int index) {
        return null;
    }

    public Calendar getMaturityDate(){
        return maturityDate;
    }
}
