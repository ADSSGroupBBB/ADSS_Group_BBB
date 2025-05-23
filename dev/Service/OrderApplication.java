package Service;

import Domain.AgreementsController;
import Domain.OrderController;
import Domain.ProductController;
import Domain.Stock;
import java.util.*;


public class OrderApplication {
    OrderController oc=OrderController.getInstance();
    ProductController pc = ProductController.getInstance();
    Stock st = Stock.getInstance(pc);

    public void automatic_order(Stock st , ProductController pc){
        List<Integer> outOfStock = new ArrayList<>();
        if (st.getProductStock().containsValue(0)){
            for (Map.Entry<Integer, Integer> entry : st.getProductStock().entrySet()) {
                if (entry.getValue() == 0) {
                    outOfStock.add(entry.getKey());
                }
            }
        }

    }

    public void addOrder(int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder){
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
