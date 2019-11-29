import java.util.Calendar;

public class CheckingAccount extends Account {

    public CheckingAccount() {
        super();
    }

    public CheckingAccount(double amount){
        super(amount);
    }

    public TransactionReceipt clearCheck(Check checkInfo, TransactionTicket info, Bank acc, int index){
        TransactionReceipt clearedCheck;
        Account bal = acc.getAccts(index);

        Calendar timeNow = Calendar.getInstance();
        Calendar beforeSixMonths = Calendar.getInstance();
        beforeSixMonths.add(Calendar.MONTH, -6);
        Calendar check = checkInfo.getDateOfCheck();
        check.add(Calendar.MONTH,6);

        if(bal.getAccountStatus()){
            if(timeNow.before(check)) {

                double drawAmount = checkInfo.getCheckAmount();
                bal = acc.getAccts(index);
                double balance =  bal.getAccountBalance();

                if(drawAmount <= 0.0)
                {
                    String reason = "Trying to withdraw invalid amount.";
                    clearedCheck = new TransactionReceipt(info,false,reason,balance);
                    return clearedCheck;
                }
                else if(drawAmount > balance)
                {
                    String reason = "Balance has insufficient funds. You have been charged a $2.50 service fee. ";
                    final double fee = 2.50;
                    double newBal = balance - fee;
                    clearedCheck = new TransactionReceipt(info,false,reason,balance,newBal);
                    bal = new Account(newBal);
                    //bal.setAccountBalance(newBal);
                    acc.checkTypeWithdraw(bal.getAccountType(),fee);
                    return clearedCheck;
                }
                else
                {
                    double newBal = balance - drawAmount;
                    clearedCheck = new TransactionReceipt(info,true,balance,newBal);
                    bal = new Account(newBal);
                    acc.checkTypeWithdraw(bal.getAccountType(),drawAmount);
                    return clearedCheck;
                }
            }
            else
            {
                String reason = "The date on the check is more than 6 months ago.";
                clearedCheck = new TransactionReceipt(info,false,reason);
                return clearedCheck;
            }
        }else{
            String reason = "Account is closed.";
            clearedCheck = new TransactionReceipt(info,false,reason);
            return clearedCheck;
        }
    }
}
