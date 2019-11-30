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
            if(.before(timeNow) || cal.equals(timeNow)){
                if(ticket.getAmountOfTransaction() <= 0.00){
                    String reason = "Invalid amount.";
                    cdRec = new TransactionReceipt(ticket,false,reason);
                    accInfo.addTransaction(cdRec);
                    return  cdRec;
                }else{
                    acctBalance = accInfo.getAccountBalance();
                    double newBalance = acctBalance + ticket.getAmountOfTransaction();
                    newDate.add(Calendar.MONTH,ticket.getTermOfCD());
                    cdRec = new TransactionReceipt(ticket,true,acctBalance,newBalance,newDate);
                    accInfo.setAccountBalance(newBalance);
                    obj.checkTypeDeposit(acctType,ticket.getAmountOfTransaction());
                    accInfo.addTransaction(cdRec);
                    return cdRec;
                }
            }else{
                String reason = "Term has not ended.";
                cdRec = new TransactionReceipt(ticket,false,reason);
                accInfo.addTransaction(cdRec);
                return cdRec;
            }
        }else{
            String reason = "Account is closed.";
            cdRec = new TransactionReceipt(ticket,false,reason);
            accInfo.addTransaction(cdRec);
            return cdRec;
        }
    }

    public Calendar getMaturityDate(){
        return maturityDate;
    }
}
