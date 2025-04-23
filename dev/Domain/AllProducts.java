package Domain;


import java.util.HashMap;
import java.util.Map;


public class AllProducts {
    private static Map<Integer, Product> existProducts;

    public AllProducts() {
        existProducts = new HashMap<>();
    }

    public static boolean checkExist(String productName, int productNumber, unit unitOfMeasure, String manufacturer) {
        if (existProducts.containsKey(productNumber)) {
            return true;
        }
        return false;
    }

    public static Product gerProduct(int productNumber) {
        if (existProducts.containsKey(productNumber)) {
            return existProducts.get(productNumber);
        }
        return null;
    }

    public static void addProduct(int productNumber,Product p) {
        if (existProducts.containsKey(productNumber)) {
            existProducts.put(productNumber, p);
        }
    }
}