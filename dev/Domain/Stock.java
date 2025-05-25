package Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Stock {
    private static Stock instance; // the single instance
    private Map<Integer, PairInt > productStock; // map of product ID to quantity

    // private constructor to prevent external instantiation
    private Stock() {
        ProductController pc = ProductController.getInstance();
        productStock = new HashMap<>();
        for (Integer productId : pc.getAllProducts().keySet()) {
            Random rn = new Random();
            int min = rn.nextInt(5) + 1;
            int curr = rn.nextInt(6)+5;
            PairInt pi = new PairInt(min, curr);
            productStock.put(productId, pi );
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
    public Map<Integer, PairInt> getProductStock() {
        return productStock;
    }





}
