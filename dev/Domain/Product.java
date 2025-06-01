package Domain;

import dto.ProductDto;

//a class to represent a product
public class Product {
    private String productName; //name of product
    private int productNumber;  //product ID
    private unit unitOfMeasure; //the unit to measure a product
    private String manufacturer;

    //product constructor
    public Product(String ProductName, int productNumber,unit unitOfMeasure,String manufacturer) {
        this.productName=ProductName;
        this.productNumber=productNumber;
        this.unitOfMeasure=unitOfMeasure;
        this.manufacturer=manufacturer;

    }

    public Product(ProductDto pro){
        this.productName=pro.productName();
        this.productNumber=pro.productNumber();
        this.unitOfMeasure=StringToEnumUnit(pro.unitOfMeasure());
        this.manufacturer=pro.manufacturer();
    }
    public ProductDto transfer(){
        return new ProductDto(this.productName,this.productNumber,(this.unitOfMeasure).name(),this.manufacturer);
    }


    //productName setter
    public void setProductName(String productName) {
        this.productName = productName;
    }

    //unitOfMeasure setter
    public void setUnitOfMeasure(unit unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
    //manufacturer setter
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    //print method for product
    //returns String
    public String printProduct(){
        return "Product name:"+this.productName+"\nProduct number:"+this.productNumber;
    }
    //productName getter
    public String getProductName() {
        return productName;
    }
    //productNumber getter
    public int getProductNumber() {
        return productNumber;
    }
    //unitOfMeasure getter
    public unit getUnitOfMeasure() {
        return unitOfMeasure;
    }
    //manufacturer getter
    public String getManufacturer() {
        return manufacturer;
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
