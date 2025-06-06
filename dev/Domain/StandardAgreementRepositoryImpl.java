package Domain;

import dataAccess.JdbcStandardAgreementDao;
import dataAccess.JdbcSupplierDao;
import dataAccess.StandardAgreementDAO;
import dto.AgreementDto;
import dto.ProductDto;
import dto.QuantityAgreementDto;
import dto.SupplierDto;


import java.sql.SQLException;
import java.util.*;

public class StandardAgreementRepositoryImpl implements StandardAgreementRepository{
    private StandardAgreementDAO standardDao;
    private Map<Integer, Agreement> standardAgreementsList;
    public StandardAgreementRepositoryImpl(){
        this.standardDao=new JdbcStandardAgreementDao();
        this.standardAgreementsList=new HashMap<>();
    }

    public AgreementDto saveStandardAgreement (int supplierNumber, String date){
        Agreement agree=new Agreement(supplierNumber,date);
        SupplierRepository sRepo=SupplierRepository.getInstance();
        sRepo.addAgreementToSup(supplierNumber,agree.getIDNumber());
        this.standardAgreementsList.put(agree.getIDNumber(),agree);
        return this.standardDao.saveStandard(new AgreementDto(agree.IDNumber,agree.supplierNumber,new LinkedList<QuantityAgreementDto>(),date));
    }
    public void addProStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount) throws SQLException {
        if(this.standardAgreementsList.containsKey(id)) {
            ProductRepository p = ProductRepositoryImpl.getInstance();
            Optional<ProductDto> pro = p.getProd(numPro);
            this.standardAgreementsList.get(id).addProductAgreement(new Product(pro), price, catalogNumber, amountToDiscount, discount);
        }
        this.standardDao.addProById( id, new QuantityAgreementDto(numPro,  price, catalogNumber, amountToDiscount, discount));
    }
    public void deleteProductStandardAgree(int numAgree,int productNumber){
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).removeProductAgreement(productNumber);
        }
        this.standardDao.removeProById( numAgree, productNumber);
    }

    public void setDateStandardAgreement(int numAgreement,String date){
        if(this.standardAgreementsList.containsKey(numAgreement)) {
            this.standardAgreementsList.get(numAgreement).setDate(date);
        }
        this.standardDao.updateDateById( numAgreement, date);
    }
    public void deleteStandardAgreement(int numSup,int numAgree){
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.remove(numAgree);
        }
        SupplierRepository s = SupplierRepositoryImpl.getInstance();
        s.removeAgreementToSup(numSup, numAgree);
        this.standardDao.removeStandard(numAgree);
        }
    public Optional<AgreementDto> getStandardAgreement(int numA){
        if(this.standardAgreementsList.containsKey(numA)){
            return Optional.of(this.standardAgreementsList.get(numA).transfer());
        }
        Optional<AgreementDto> optionalAgree = this.standardDao.findStandardAgreeById(numA);

        if (optionalAgree.isPresent()) {
            AgreementDto agree = optionalAgree.get();
            standardAgreementsList.put(numA, new Agreement(agree));
        }
        return optionalAgree;
    }
    public void setNumCatalogStandard(int numAgree,int productNumber,int catalogNumber){
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setCatalog(productNumber,catalogNumber);
        }
        this.standardDao.updateCatalogById( numAgree, productNumber,catalogNumber);
    }
    public void setPriceStandardAgreement(int numAgree,int productNumber ,double price){
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setPrice(productNumber,price);
        }
        this.standardDao.updatePriceById( numAgree, productNumber,price);
    }

    public void setAmountToDiscountStandardAgreement(int productNumber,int numAgree,int amountToDiscount){
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setAmountToDiscount(productNumber,amountToDiscount);
        }
        this.standardDao.updateAmountToDiscountById( numAgree, productNumber,amountToDiscount);
    }
    public void setDiscountStandardAgreement(int productNumber,int numAgree,int discount){
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setDiscount(productNumber,discount);
        }
        this.standardDao.updateDiscountById( numAgree, productNumber,discount);
    }
    public List<Optional<AgreementDto>> allAgreementsByStandardSup(int numS ){
        List<Optional<AgreementDto>> optionalAgrees = this.standardDao.findAllStandardAgreeBySupId(numS);
        for (Optional<AgreementDto> agree:optionalAgrees) {
            if (agree.isPresent()) {
                AgreementDto a = agree.get();
                standardAgreementsList.put(a.IDNumber(), new Agreement(a));
            }
        }
        return optionalAgrees;
    }
    public boolean existProStandardAgreementByName(String nameP,int numA){

    }
    public QuantityAgreement productFromAgreementByName(int numAgree,String nameP);
    Map<Integer, Agreement> getAllStandardAgreements();

}
