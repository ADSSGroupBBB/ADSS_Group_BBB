package Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class Stock {
    private static Stock instance; // the single instance
    private Map<String, PairInt > productStock; // map of product ID to quantity

    // private constructor to prevent external instantiation
    private Stock() {
        ProductController pc = ProductController.getInstance();
        productStock = new HashMap<>();
        for (Product product : pc.getAllProducts().values()) {
            Random rn = new Random();
            int min = rn.nextInt(5) + 1;
            int curr = rn.nextInt(6)+5;
            PairInt pi = new PairInt(min, curr);
            productStock.put(product.getProductName(), pi );
        }
    }

    // public method to access the instance, with initialization on first call
    public static Stock getInstance() {
        if (instance == null) {
            instance = new Stock();
        }
        return instance;
    }

    // Getter
    public Map<String, PairInt> getProductStock() {
        return productStock;
    }

    public void sell(Map<Integer,Integer> soldPro){
        ProductRepositoryImpl pri = new ProductRepositoryImpl();
        String name="" ;
        for (Map.Entry<String, PairInt> i : getProductStock().entrySet()) {
            for (Map.Entry<Integer, Integer> j : soldPro.entrySet()) {
                if (pri.getProd(j.getKey()).isPresent()) {
                    name = pri.getProd(j.getKey()).get().productName();
                }
                if (Objects.equals(i.getKey(), name)) {
                    int fir = i.getValue().first;
                    int sec = i.getValue().second;
                    PairInt cur = new PairInt(fir - soldPro.get(j.getKey()), sec);    //update the stock of sold item
                    i.setValue(cur);
                }
            }
        }
        for (Map.Entry<String, PairInt> entry : getProductStock().entrySet()) {
            if (entry.getValue().first < entry.getValue().second) {  //check if the amount of product is smaller than the minimum required
                automaticOrder.outOfStock.add(entry.getKey()); //if so add the id number of the product to the list
            }
        }
    }





}
