package Domain;


import java.util.HashMap;
import java.util.Map;

public class AgreementsController {
    private static Map<Integer,Agreement> allAgreements;
    private static int num=0;
    public AgreementsController(){
        if (num==0){
            allAgreements=new HashMap<>();
            num++;
        }
    }

    public int addNewAgreement(int supplierNumber,String date){
        Agreement agree=new Agreement(supplierNumber,date);
        SupplierController s=new SupplierController();
        s.addAgreement(supplierNumber,agree);
        allAgreements.put(agree.getIDNumber(),agree);
        return agree.getIDNumber();
    }
    public void addProToAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount){
        ProductController p= new ProductController();
        Product pro=p.getPro(numPro);
        allAgreements.get(id).addProductAgreement(pro,price,catalogNumber,amountToDiscount,discount);
    }
    public void deleteProductFromAgree(int numAgree,int productNumber){
        allAgreements.get(numAgree).removeProductAgreement(productNumber);
    }
    public void setDateAgree(int numAgreement,String date){
        allAgreements.get(numAgreement).setDate(date);
    }
    public void deleteAgree(int numSup,int numAgree){
        SupplierController s=new SupplierController();
        s.deleteAgreement(numSup,allAgreements.get(numAgree));
        allAgreements.remove(numAgree);
    }
    public boolean existProAgre(int numP,int numA){
        return allAgreements.get(numA).searchProduct(numP);
    }
    public boolean existAgreement(int sup,int numA) {
        if (allAgreements.containsKey(numA)) {
            if(allAgreements.get(numA).getSupplierNumber()==sup){
                return true;
            }
        }
        return false;
    }
    public QuantityAgreement productFromAgree(int numAgree,int numP){
        for(QuantityAgreement qa:allAgreements.get(numAgree).getProductsList()){
            if(qa.getNumberProAgreement()==numP){
                return qa;
            }
        }
        return null;
    }

    public int numProduct(int numAgreement){
        return allAgreements.get(numAgreement).getProductsList().size();
    }

    public String printProduct(int num){
        return allAgreements.get(num).printListProducts();
    }
    public void setcatalogNum(int productNumber,int numAgree,int catalogNumber){
        allAgreements.get(numAgree).setCatalog(productNumber,catalogNumber);
    }
    public void setPriceAgree(int productNumber,int numAgree,double price){
        allAgreements.get(numAgree).setPrice(productNumber,price);
    }
    public void setAmountToDiscountAgree(int productNumber,int numAgree,int amountToDiscount){
        allAgreements.get(numAgree).setAmountToDiscount(productNumber,amountToDiscount);
    }
    public void setDiscountAgree(int productNumber,int numAgree,int discount){
        allAgreements.get(numAgree).setDiscount(productNumber,discount);
    }

}
