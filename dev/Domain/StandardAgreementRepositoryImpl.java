package Domain;

import dataAccess.JdbcStandardAgreementDao;
import dataAccess.JdbcSupplierDao;
import dataAccess.StandardAgreementDAO;
import dto.AgreementDto;
import dto.ProductDto;
import dto.QuantityAgreementDto;
import dto.SupplierDto;
import util.Database;
import util.DatabaseManager;


import java.sql.SQLException;
import java.util.*;

public class StandardAgreementRepositoryImpl implements StandardAgreementRepository{
    private static StandardAgreementRepositoryImpl instance;

    private StandardAgreementDAO standardDao;
    private Map<Integer, Agreement> standardAgreementsList;

    private StandardAgreementRepositoryImpl() {
        this.standardDao = new JdbcStandardAgreementDao();
        this.standardAgreementsList = new HashMap<>();
    }

    public static StandardAgreementRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new StandardAgreementRepositoryImpl();
        }
        return instance;
    }
    public AgreementDto saveStandardAgreement (int supplierNumber, String date) throws SQLException{
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            AgreementDto saved = this.standardDao.saveStandard(new AgreementDto(-1, supplierNumber, new LinkedList<>(), date)); //-1 -fictive value
            Agreement agree=new Agreement(saved.IDNumber(),supplierNumber,date);
            SupplierRepository sRepo=SupplierRepositoryImpl.getInstance();
            sRepo.addAgreementToSup(supplierNumber,agree.getIDNumber());
            this.standardAgreementsList.put(agree.getIDNumber(), agree);
            DatabaseManager.getConnection().commit();
            return saved;
        }
        catch (SQLException e) {
            if ( DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        }
        finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
    }
    public void addProStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            ProductRepository p = ProductRepositoryImpl.getInstance();
            Optional<ProductDto> pro = p.getProd(numPro);
            if (this.standardAgreementsList.containsKey(id)) {
                this.standardAgreementsList.get(id).addProductAgreement(ProductMapper.toObject(pro.get()), price, catalogNumber, amountToDiscount, discount);
            }
            this.standardDao.addProById(id, new QuantityAgreementDto(pro.get(), price, catalogNumber, amountToDiscount, discount));
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            if (DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        } finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
    }
    public void deleteProductStandardAgree(int numAgree,int productNumber) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).removeProductAgreement(productNumber);
        }
        this.standardDao.removeProById( numAgree, productNumber);
    }

    public void setDateStandardAgreement(int numAgreement,String date) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgreement)) {
            this.standardAgreementsList.get(numAgreement).setDate(date);
        }
        this.standardDao.updateDateById( numAgreement, date);
    }
    public void deleteStandardAgreement(int numSup,int numAgree) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.remove(numAgree);
        }
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
        SupplierRepository s = SupplierRepositoryImpl.getInstance();
        s.removeAgreementToSup(numSup, numAgree);
        this.standardDao.removeStandard(numAgree);
            DatabaseManager.getConnection().commit();
        }
        catch (SQLException e) {
            if ( DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        }
        finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
    }
    public Optional<AgreementDto> getStandardAgreement(int numA) throws SQLException{
        if(this.standardAgreementsList.containsKey(numA)){
            return Optional.of(AgreementMapper.transfer(this.standardAgreementsList.get(numA)));
        }
        Optional<AgreementDto> optionalAgree = this.standardDao.findStandardAgreeById(numA);

        if (optionalAgree.isPresent()) {
            AgreementDto agree = optionalAgree.get();
            standardAgreementsList.put(numA, AgreementMapper.toObject(agree));
        }
        return optionalAgree;
    }
    public void setNumCatalogStandard(int numAgree,int productNumber,int catalogNumber) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setCatalog(productNumber,catalogNumber);
        }
        this.standardDao.updateCatalogById( numAgree, productNumber,catalogNumber);
    }
    public void setPriceStandardAgreement(int numAgree,int productNumber ,double price) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setPrice(productNumber,price);
        }
        this.standardDao.updatePriceById( numAgree, productNumber,price);
    }

    public void setAmountToDiscountStandardAgreement(int productNumber,int numAgree,int amountToDiscount) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setAmountToDiscount(productNumber,amountToDiscount);
        }
        this.standardDao.updateAmountToDiscountById( numAgree, productNumber,amountToDiscount);
    }
    public void setDiscountStandardAgreement(int productNumber,int numAgree,int discount) throws SQLException{
        if(this.standardAgreementsList.containsKey(numAgree)) {
            this.standardAgreementsList.get(numAgree).setDiscount(productNumber,discount);
        }
        this.standardDao.updateDiscountById( numAgree, productNumber,discount);
    }
    public QuantityAgreementDto proFromAgreeByIndex(int numAgree, int numP) throws SQLException{
        int i=1;
        Optional<AgreementDto> agree=this.standardDao.findStandardAgreeById(numAgree);
        if(agree.isPresent()) {
            List<QuantityAgreementDto> products = agree.get().productsList();
            for (QuantityAgreementDto qa : products) {
                if (i == numP) {
                    return qa;
                }
                i++;
            }
        }
        return null;
    }
    public QuantityAgreementDto searchPro(int numAgree,int numP) throws SQLException{
        Agreement a=AgreementMapper.toObject(getGeneralAgreement(numAgree).get());
        return QuantityAgreementMapper.transfer(a.searchPro(numP));
    }
    public  Optional<AgreementDto> getGeneralAgreement(int numA) throws SQLException {
        if (this.standardAgreementsList.containsKey(numA)) {
            return Optional.of(AgreementMapper.transfer(this.standardAgreementsList.get(numA)));
        }
       return this.standardDao.getAgreement(numA);

    }
    public List<AgreementDto> allAgreementsByStandardSup(int numS ) throws SQLException{
        List<AgreementDto> optionalAgrees = this.standardDao.findAllStandardAgreeBySupId(numS);
        for (AgreementDto agree:optionalAgrees) {
                standardAgreementsList.put(agree.IDNumber(), AgreementMapper.toObject(agree));

        }
        return optionalAgrees;
    }
    public  List<AgreementDto> getAllStandardAgreeWithPro(int numP) throws SQLException{
        List<AgreementDto> optionalAgrees = this.standardDao.findAllStandardAgreeByProId(numP);
        for (AgreementDto agree:optionalAgrees) {
            standardAgreementsList.put(agree.IDNumber(), AgreementMapper.toObject(agree));
        }
        return optionalAgrees;
    }

}
