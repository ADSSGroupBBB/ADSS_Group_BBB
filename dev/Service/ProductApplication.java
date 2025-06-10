package Service;

import Domain.ProductController;

import java.sql.SQLException;

public class ProductApplication {
    ProductController pc=ProductController.getInstance();
    public boolean existProduct(int productNumber) throws SQLException {
        return pc.checkPro(productNumber);
    }
    public void addPro(String productName, int productNumber,String unitOfMeasure,String manufacturer) throws SQLException{
        pc.addNewProduct(productName,productNumber,unitOfMeasure,manufacturer);
    }
    public void setName(int productNumber,String productName) throws SQLException{
        pc.setNamePro(productNumber,productName);
    }
    public void setunitOfMeasure(int productNumber,String unitOfMeasure) throws SQLException{
        pc.setunitOfMeasurePro(productNumber,unitOfMeasure);
    }
    public void setManufacturer(int productNumber,String manufacturer) throws SQLException{
        pc.setManufacturerPro(productNumber,manufacturer);
    }


}
