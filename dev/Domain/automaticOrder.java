package Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class automaticOrder {
    static List<String> outOfStock = new ArrayList<>();

    public Map<Integer,QuantityAgreement> automatic_order() {
        StandardAgreementRepositoryImpl sari = new StandardAgreementRepositoryImpl();
        ProductRepositoryImpl pri = new ProductRepositoryImpl();
        Stock st = Stock.getInstance();


        Map<Integer,QuantityAgreement> toOrder= new HashMap<>();
        for (String p : outOfStock) {
            List<QuantityAgreement> agreeForPro = new ArrayList<>();
            for (Agreement agree : sari.getAllStandardAgreements().values()) {
                if (sari.existProStandardAgreementByName(p, agree.getIDNumber())) {
                    QuantityAgreement qa = sari.productFromAgreementByName(agree.getIDNumber(), p);
                    if (qa != null) {
                        agreeForPro.add(qa);
                    }
                }
            }
            QuantityAgreement bestOp = getMinimumEffectivePrice(agreeForPro, pri);
            toOrder.put(bestOp.getNumberProAgreement(),bestOp);
        }
        return toOrder;
    }

    public QuantityAgreement getMinimumEffectivePrice(List<QuantityAgreement> agreeForPro, ProductRepositoryImpl pri) {
        double minEffectivePrice = Double.MAX_VALUE;
        QuantityAgreement bestOp = null;
        for (QuantityAgreement quAg : agreeForPro) {
            double effectivePrice=0;
            int discount = quAg.getDiscountAgreement();
            int amountToDis = quAg.getAmountToDiscountAgreement();
            double price = quAg.getPriceAgreement();
            Stock st =Stock.getInstance();
            String pName = quAg.getNameAgreement();
            int currStock = st.getProductStock().get(pName).first;
            if (currStock>= amountToDis) {
                // Example calculation: discounted price per item
                effectivePrice = price - (discount / 100.0) * price;
            }
            // Update minimum
            if (effectivePrice < minEffectivePrice) {
                minEffectivePrice = effectivePrice;
                bestOp=quAg;
            }
        }

        return bestOp;
    }

    public boolean standardAutoOrder(int numAgree ,int numSupplier, String address, String date, String contactPhone, String statusOrder){
        OrderController oc = OrderController.getInstance();
        int orderNum = oc.addNewOrder( numAgree , numSupplier, address,  date,  contactPhone,statusOrder);
        Map<Integer,QuantityAgreement> toOrder = automatic_order();
        for (QuantityAgreement pro : toOrder.values()){
            int pID = pro.getNumberProAgreement();
            String pName = pro.getNameAgreement();
            Stock st = Stock.getInstance();
            int amount = st.getProductStock().get(pName).second;
            if (oc.addItemOrder(orderNum, pID, amount)) continue;
            else return false;
        }
        return true;
    }


    public static List<String> getOutOfStock() {
        return outOfStock;
    }
}
