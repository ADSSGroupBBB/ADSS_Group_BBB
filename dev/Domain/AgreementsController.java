package Domain;


import java.util.HashMap;
import java.util.Map;
//a class for the controller of agreement (manager)
public class AgreementsController {
    private static AgreementsController instance;
    private Map<Integer, Agreement> allAgreements;

    private AgreementsController() {
        allAgreements = new HashMap<>();
    }

    public static AgreementsController getInstance() {
        if (instance == null) {
            instance = new AgreementsController();
        }
        return instance;
    }

    //a method to add a new agreement to a supplier
    //parameters:int supplierNumber,String date
    //returns an int
    public int addNewAgreement(int supplierNumber,String date){
        Agreement agree=new Agreement(supplierNumber,date);
        SupplierController s=SupplierController.getInstance();
        s.addAgreement(supplierNumber,agree);
        allAgreements.put(agree.getIDNumber(),agree);
        return agree.getIDNumber();
    }
    //add product to agreemennt
    //parameters:int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount
    public void addProToAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount){
        ProductController p= ProductController.getInstance();
        Product pro=p.getPro(numPro);
        allAgreements.get(id).addProductAgreement(pro,price,catalogNumber,amountToDiscount,discount);
    }
    // remove product from agreement
    //parameters: int numAgree,int productNumber
    public void deleteProductFromAgree(int numAgree,int productNumber){
        allAgreements.get(numAgree).removeProductAgreement(productNumber);
    }
    //set date for agreement
    //parameters: int numAgreement,String date
    public void setDateAgree(int numAgreement,String date){
        allAgreements.get(numAgreement).setDate(date);
    }
    //delete agreement
    //parameters: int numSup,int numAgree
    public void deleteAgree(int numSup,int numAgree){
        SupplierController s=SupplierController.getInstance();
        s.deleteAgreement(numSup,allAgreements.get(numAgree));
        allAgreements.remove(numAgree);
    }
    //checks if a certain product exists in agreement
    //parameters:int numP,int numA
    //returns aa boolean value. true is such product exists and false otherwise
    public boolean existProAgre(int numP,int numA){
        return allAgreements.get(numA).searchProduct(numP);
    }
    //checks if a certain agreement exists
    //parameters:int sup,int numA
    //returns a boolean value. true if the agreement exists and false otherwise
    public boolean existAgreement(int sup,int numA) {
        if (allAgreements.containsKey(numA)) {
            if(allAgreements.get(numA).getSupplierNumber()==sup){
                return true;
            }
        }
        return false;
    }
    //find product from agreements using indexes
    //parameters:int numAgree,int numP
    //returns QuantityAgreement
    public QuantityAgreement productFromAgreeByIndex(int numAgree,int numP){
        int i=1;
        for(QuantityAgreement qa:allAgreements.get(numAgree).getProductsList()){
            if(i==numP){
                return qa;
            }
            i++;
        }
        return null;
    }

    //finds the amount of products
    //parameters:int numAgreement
    //return int- the amount of products
    public int numProduct(int numAgreement){
        return allAgreements.get(numAgreement).getProductsList().size();
    }

    //print the products from a specific agreement
    //parameters: int num
    //returns a String
    public String printProduct(int num){
        return allAgreements.get(num).printListProducts();
    }
    //set catalog number
    //parameters: int productNumber,int numAgree,int catalogNumber
    public void setcatalogNum(int productNumber,int numAgree,int catalogNumber){
        allAgreements.get(numAgree).setCatalog(productNumber,catalogNumber);
    }
    //set price to product
    //parameters: int productNumber,int numAgree,double price
    public void setPriceAgree(int productNumber,int numAgree,double price){
        allAgreements.get(numAgree).setPrice(productNumber,price);
    }
    //set the amount for discount
    //parameters: int productNumber,int numAgree,int amountToDiscount
    public void setAmountToDiscountAgree(int productNumber,int numAgree,int amountToDiscount){
        allAgreements.get(numAgree).setAmountToDiscount(productNumber,amountToDiscount);
    }
    //set the discounted price
    //parameters: int productNumber,int numAgree,int discount
    public void setDiscountAgree(int productNumber,int numAgree,int discount){
        allAgreements.get(numAgree).setDiscount(productNumber,discount);
    }
    //checks if product exists given supplier
    //parameters:int numS,int numP
    //returns a boolean value
    public boolean existProBySup(int numS,int numP ) {
        for (Agreement agree: allAgreements.values()){
            if(agree.getSupplierNumber()==numS){
                if(agree.searchProduct(numP))
                    return true;
            }
        }
        return false;
    }
    }
