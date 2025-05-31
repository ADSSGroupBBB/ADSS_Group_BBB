package Service;

import Domain.*;
import java.util.*;



public class OrderApplication {
    OrderController oc=OrderController.getInstance();
    Stock st = Stock.getInstance();
    static List<Integer> outOfStock = new ArrayList<>();

    public void automatic_order(Map<Integer,Integer> soldPro) {
        Stock st = Stock.getInstance();
        for (Map.Entry<Integer, PairInt> i : st.getProductStock().entrySet()) {
            for (Map.Entry<Integer, Integer> j : soldPro.entrySet()) {
                if (Objects.equals(i.getKey(), j.getKey())) {
                    int fir = i.getValue().first;
                    int sec = i.getValue().second;
                    PairInt cur = new PairInt(fir - soldPro.get(j.getKey()), sec);    //update the stock of sold item
                    i.setValue(cur);
                }
            }
        }
        for (Map.Entry<Integer, PairInt> entry : st.getProductStock().entrySet()) {
            if (entry.getValue().first < entry.getValue().second) {  //check if the amount of product is smaller than the minimum required
                outOfStock.add(entry.getKey()); //if so add the id number of the product to the list
            }
        }
        StandardAgreementRepositoryImpl sari = new StandardAgreementRepositoryImpl();
        for (Integer p : outOfStock) {
            List<QuantityAgreement> agreeForPro = new ArrayList<>();
            for (Agreement agree : sari.getAllStandardAgreements().values()){
                    if (sari.existProStandardAgreementByName(agree.get))
                    QuantityAgreement qa = sari.productFromAgreementByIndex(agree.getIDNumber(), p);
                    if (qa != null){
                        agreeForPro.add(qa);
                    }
            }
            for (QuantityAgreement quAg : agreeForPro){
                int discount = quAg.getDiscountAgreement();
                int amountToDis = quAg.getAmountToDiscountAgreement();
                double price =quAg.getPriceAgreement();

            }
            }
    }

    public static List<Integer> getOutOfStock() {
        return outOfStock;
    }

    public int addOrder(int numAgree ,int numSupplier, String address, String date, String contactPhone, String statusOrder){
         return oc.addNewOrder(numAgree,numSupplier,address,date,contactPhone,statusOrder);
    }
    public boolean orderExist(int orderNumber){
        return oc.existOrder(orderNumber);
    }
    public String printByAgree(int numAgreement){
        return oc.printProByAgree(numAgreement);
    }
    public int numProAgreement(int numAgreement){
        return oc.numProByAgree(numAgreement);
    }
    public boolean addItem(int orderNumber, int numP,int amount){
        return oc.addItemOrder(orderNumber, numP, amount);
    }
    public void deleteOrder(int orderNumber){
        oc.statusDelete(orderNumber);
    }
    public String printOrder(int orderNumber){
        return oc.StringOrder(orderNumber);
    }
}
