package Service;

import Domain.*;

import java.util.*;


public class OrderApplication {
    OrderController oc=OrderController.getInstance();
    Stock st = Stock.getInstance();
    static List<Integer> outOfStock = new ArrayList<>();

    public void automatic_order(Map<Integer,Integer> soldPro){
        Stock st = Stock.getInstance();
        for (Map.Entry<Integer, PairInt> i : st.getProductStock().entrySet()){
            for (Map.Entry<Integer, Integer> j : soldPro.entrySet()){
                if(Objects.equals(i.getKey(), j.getKey())){
                    int fir = i.getValue().first;
                    int sec = i.getValue().second;
                    PairInt cur = new PairInt(fir- soldPro.get(j.getKey()),sec);
                    i.setValue(cur);
                }
            }
        }
        for (Map.Entry<Integer, PairInt> entry : st.getProductStock().entrySet()) {
            if (entry.getValue().first < entry.getValue().second){
                outOfStock.add(entry.getKey());
            }
        }

    }

    public static List<Integer> getOutOfStock() {
        return outOfStock;
    }

    public void addOrder(int orderNumber, int numSupplier, String address, String date, String contactPhone, String statusOrder){
         oc.addNewOrder(orderNumber,numSupplier,address,date,contactPhone,statusOrder);
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
    public boolean addItem(int orderNumber, int numAgreement, int numP,int amount){
        return oc.addItemOrder(orderNumber, numAgreement, numP, amount);
    }
    public void deleteOrder(int orderNumber){
        oc.statusDelete(orderNumber);
    }
    public String printOrder(int orderNumber){
        return oc.StringOrder(orderNumber);
    }
}
