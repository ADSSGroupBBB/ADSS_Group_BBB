package Domain;

import dto.ProductDto;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//a class for the manager (the controller) of Product
public class ProductController {
    private static ProductController instance; // the single instance
    //private Map<Integer, Product> allProducts; // map of all products
    private ProductRepository proRepo;


    // private constructor to prevent outside instantiation
    private ProductController() {
        proRepo =new ProductRepositoryImpl();
    }

    // public method to access the single instance
    public static ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }
    //checks if a certain product exists
    //parameters:int productNumber
    //returns boolean value
    public boolean checkPro(int productNumber) throws SQLException {
        return this.proRepo.getProd(productNumber).isPresent();
    }

    public Optional<ProductDto> getPro(int num) throws SQLException{
        return this.proRepo.getProd(num);
    }
    public void addNewProduct(String productName, int productNumber, String unitOfMeasure, String manufacturer) throws SQLException{
         this.proRepo.addPro(productName,productNumber,unitOfMeasure,manufacturer);
    }
    public Map<Integer,Product> getAllProducts(){ return allProducts;}

    public void setNamePro(int productNumber,String productName) throws SQLException {
        this.proRepo.updateName(productNumber,productName);
    }
    public void setunitOfMeasurePro(int productNumber,String unitOfMeasure) throws SQLException{
        this.proRepo.updateUnitOfMeasure(productNumber,unitOfMeasure);
    }
    public void setManufacturerPro(int productNumber,String manufacturer) throws SQLException{
    this.proRepo.updateManufacturer(productNumber,manufacturer);
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
