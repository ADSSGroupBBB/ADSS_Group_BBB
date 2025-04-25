package Domain;

import java.util.HashMap;
import java.util.Map;

public class ProductController {
    private static Map<Integer,Product> allProducts;
    private static int num=0;
    public ProductController(){
        if (num==0){
            allProducts=new HashMap<>();
            num++;
        }
    }
    public boolean checkPro(int productNumber){
        return allProducts.containsKey(productNumber);
    }
    public Product getPro(int num){
        return allProducts.get(num);
    }
    public void addNewProduct(String productName,int productNumber,String unitOfMeasure,String manufacturer){
        unit u= StringToEnumUnit(unitOfMeasure);
        Product pro=new Product(productName,productNumber,u,manufacturer);
        allProducts.put(productNumber,pro);
    }

    public void setNamePro(int productNumber,String productName){
        allProducts.get(productNumber).setProductName(productName);
    }
    public void setunitOfMeasurePro(int productNumber,String unitOfMeasure){
        unit u=StringToEnumUnit(unitOfMeasure);
        if (u!=null) {
            allProducts.get(productNumber).setUnitOfMeasure(u);
        }
    }
    public void setManufacturerPro(int productNumber,String manufacturer){
        allProducts.get(productNumber).setManufacturer(manufacturer);
    }

    public unit StringToEnumUnit(String unitOfMeasure){
        if (unitOfMeasure.equals("kg")) {
            return unit.kg;
        }else if (unitOfMeasure.equals("g")) {
            return unit.g;
        }else if (unitOfMeasure.equals("ml")) {
            return unit.ml;
        }else if (unitOfMeasure.equals("liter")) {
            return unit.liter;
        }
        else {
            return null;
        }
    }
}
