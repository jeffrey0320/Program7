public class SavingsAccount extends Account {

    public SavingsAccount() {
        super();
    }

    public SavingsAccount(int acctNumber, String acctType, double acctBalance, Depositor personInfo, boolean acctStatus) {
        super(acctNumber, acctType, acctBalance, personInfo, acctStatus);
    }

    @Override
    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj, int index) {
        return null;
    }


}
