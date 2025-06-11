package Domain;

import java.util.LinkedList;

public class PeriodAgreement extends Agreement {
    private String address; //the address for shipment
    private String contactPhone;

    public PeriodAgreement(int IDNumber, int supplierNumber, String date, String address, String contactPhone) {
        super(IDNumber,supplierNumber, date);
        this.IDNumber = IDNumber;
        this.address = address;
        this.contactPhone = contactPhone;
    }


    public void addProductAgreement(Product p, double price, int catalogNumber, int amountToDiscount, int discount, int amountToOrder) {
        this.productsList.add(new PeriodicOrderItem(this.IDNumber, p, price, catalogNumber, amountToDiscount, discount, amountToOrder));
    }
    public void setAmount(int proN,int amountToOrder){
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                ((PeriodicOrderItem)(productsList.get(i))).setAmountToOrder(amountToOrder);
                break;
            }
        }
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}
