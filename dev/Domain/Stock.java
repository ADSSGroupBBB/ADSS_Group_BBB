package Domain;

import java.util.HashMap;
import java.util.Map;

public class Stock {
    private static Stock instance; // the single instance
    private Map<Integer, Integer> productStock; // map of product ID to quantity

    // private constructor to prevent external instantiation
    private Stock(ProductController pc) {
        productStock = new HashMap<>();
        for (Integer productId : pc.getAllProducts().keySet()) {
            productStock.put(productId, 5);
        }
    }

    // public method to access the instance, with initialization on first call
    public static Stock getInstance(ProductController pc) {
        if (instance == null) {
            instance = new Stock(pc);
        }
        return instance;
    }

    // Getter
    public Map<Integer, Integer> getProductStock() {
        return productStock;
    }
}
