package Domain;

import dto.ProductDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;


public class StockController {
    private static StockController instance; // the single instance
    private Map<Integer, ProductStock > allProductStock; // map of product ID to quantity
    private Map<Integer,ProductStock> missProducts;

    // private constructor to prevent external instantiation
    private StockController() throws SQLException{
        ProductController pc = ProductController.getInstance();
        allProductStock = new HashMap<>();
        this.missProducts=new HashMap<>();
        for (ProductDto product : pc.getAllProducts()) {
            Random rn = new Random();
            int min = rn.nextInt(5) + 10;
            int curr = rn.nextInt(6)+15;
            ProductStock pi = new ProductStock(product.productNumber(),curr,min);
            allProductStock.put(product.productNumber(), pi );
        }
    }
    public void addProToStock(int productNumber){
        Random rn = new Random();
        int min = rn.nextInt(5) + 1;
        int curr = rn.nextInt(6)+5;
        ProductStock pi = new ProductStock(productNumber,min, curr);
        allProductStock.put(productNumber, pi );
    }

    // public method to access the instance, with initialization on first call
    public static StockController getInstance() throws SQLException{
        if (instance == null) {
            instance = new StockController();
        }
        return instance;
    }
    public int getCurrentAmount(int numProduct){
        return this.allProductStock.get(numProduct).getCurrentAmount();
    }
    public  int getMinimumAmount(int numProduct){
        return this.allProductStock.get(numProduct).getMinimumCount();
    }


    // Getter
    public Map<Integer, ProductStock> getProductStock() {
        return allProductStock;
    }

    public void sell(Map<Integer,Integer> soldPro){
        ProductRepositoryImpl pri =  ProductRepositoryImpl.getInstance();
        String name="" ;
        for (Map.Entry<Integer, Integer> pro : soldPro.entrySet()) {
            int productId = pro.getKey();
            int quantitySold = pro.getValue();
            if (getProductStock().containsKey(productId)) {
                getProductStock().get(productId).lessCurrentAmount(quantitySold);
                if(getProductStock().get(productId).getCurrentAmount()<getProductStock().get(productId).getMinimumCount()){
                    missProducts.put(productId,getProductStock().get(productId));
                }
        }
        }
    }

    public Map<Integer, ProductStock> getMissProducts() {
        LinkedList<Integer> pro_remove=new LinkedList<>();
        for (ProductStock pro:this.missProducts.values()) {
            if (pro.isBeOrdered()) {
                pro_remove.add(pro.getNumProduct());
            }
        }
        for (Integer numP:pro_remove) {
            this.missProducts.remove(numP);
        }
        return missProducts;
    }

    public void updateStock(int orderId)  throws SQLException {
        OrderController oc = OrderController.getInstance();
        Order o = oc.orderBYnum(orderId);
        for (ItemOrder it : o.getItems()) {
            int numP = it.getNumberItem();
            getProductStock().get(numP).addCurrentAmount(it.getAmountOrder()); //Product status varies depending on the interior
            }
        }
    public void cancelStock(int orderId)  throws SQLException {
        OrderController oc = OrderController.getInstance();
        Order o = oc.orderBYnum(orderId);
        for (ItemOrder it : o.getItems()) {
            int numP = it.getNumberItem();
            if (getCurrentAmount(numP) + it.getAmountOrder() >= getMinimumAmount(numP)) {
                getProductStock().get(numP).setBeOrdered(false);
            }
            if (getCurrentAmount(numP) < getMinimumAmount(numP)) {
                this.missProducts.put(numP,this.allProductStock.get(numP));
            }
        }
    }
    public void updateStatusOrderPro(int productId, int amount) {
        if (getCurrentAmount(productId) + amount >= getMinimumAmount(productId)) {
            getProductStock().get(productId).setBeOrdered(true);
            }
    }





}
