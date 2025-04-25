package Domain;

public class Product {
    private String productName;
    private int productNumber;
    private unit unitOfMeasure;
    private String manufacturer;

    public Product(String ProductName, int productNumber,unit unitOfMeasure,String manufacturer) {
        this.productName=ProductName;
        this.productNumber=productNumber;
        this.unitOfMeasure=unitOfMeasure;
        this.manufacturer=manufacturer;

    }


    public void setProductName(String productName) {
        this.productName = productName;
    }


    public void setUnitOfMeasure(unit unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String printProduct(){
        return "Product name:"+this.productName+"\nProduct number:"+this.productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public unit getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
