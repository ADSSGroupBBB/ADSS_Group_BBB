package Domain;

public class PeriodicOrderItem extends QuantityAgreement{
    private int amountToOrder;
    public PeriodicOrderItem( int numAgreement,Product prod,double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder){
        super(numAgreement,prod,price,catalogNumber,amountToDiscount,discount);
        this.amountToOrder=amountToOrder;

    }

    public void setAmountToOrder(int amountToOrder) {
        this.amountToOrder = amountToOrder;
    }

}
