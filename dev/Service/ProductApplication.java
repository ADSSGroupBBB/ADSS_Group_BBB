package Service;

import Domain.ProductController;

public class ProductApplication {
    ProductController pc=ProductController.getInstance();
    public boolean existProduct(int productNumber){
        return pc.checkPro(productNumber);
    }
    public void addPro(String productName, int productNumber,String unitOfMeasure,String manufacturer){
        pc.addNewProduct(productName,productNumber,unitOfMeasure,manufacturer);
    }
    public void setName(int productNumber,String productName){
        pc.setNamePro(productNumber,productName);
    }
    public void setunitOfMeasure(int productNumber,String unitOfMeasure){
        pc.setunitOfMeasurePro(productNumber,unitOfMeasure);
    }
    public void setManufacturer(int productNumber,String manufacturer){
        pc.setManufacturerPro(productNumber,manufacturer);
    }


}
