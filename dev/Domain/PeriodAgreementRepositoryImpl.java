package Domain;

import dataAccess.JdbcPeriodAgreementDao;
import dataAccess.PeriodAgreementDao;
import dto.*;
import util.Database;
import util.DatabaseManager;

import java.sql.SQLException;
import java.util.*;

public class PeriodAgreementRepositoryImpl implements PeriodAgreementRepository{
    private static PeriodAgreementRepositoryImpl instance;

    private PeriodAgreementDao periodDao;
    private Map<Integer, PeriodAgreement> periodAgreementsList;

    private PeriodAgreementRepositoryImpl() {
        this.periodDao = new JdbcPeriodAgreementDao();
        this.periodAgreementsList = new HashMap<>();
    }

    public static PeriodAgreementRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new PeriodAgreementRepositoryImpl();
        }
        return instance;
    }
    public PeriodAgreementDto savePeriodAgreement (int supplierNumber, String date,String address,String contactPhone) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);

            PeriodAgreementDto dto = this.periodDao.savePeriod(new PeriodAgreementDto(-1,supplierNumber, new LinkedList<>(), date, address, contactPhone)); //-1 -fictive value
            SupplierRepository sRepo = SupplierRepositoryImpl.getInstance();
            PeriodAgreement agree = new PeriodAgreement(dto.IDNumber(),supplierNumber, date, address, contactPhone);
            sRepo.addAgreementToSup(supplierNumber, agree.getIDNumber());
            this.periodAgreementsList.put(agree.getIDNumber(), agree);
            DatabaseManager.getConnection().commit();
            return dto;
        } catch (SQLException e) {
            if (DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        }
    }

    @Override
    public void addProPeriodAgreement(int id, int numPro, double price, int catalogNumber, int amountToDiscount, int discount, int amountToOrder)  throws SQLException{
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
        ProductRepository p = ProductRepositoryImpl.getInstance();
        Optional<ProductDto> pro = p.getProd(numPro);
        if(this.periodAgreementsList.containsKey(id)) {
            this.periodAgreementsList.get(id).addProductAgreement(ProductMapper.toObject(pro.get()), price, catalogNumber, amountToDiscount, discount,amountToOrder);
        }
        this.periodDao.addProById( id, new PeriodAgreementItemDto(new QuantityAgreementDto(pro.get(),  price, catalogNumber, amountToDiscount, discount),amountToOrder));
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            if (DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        }
        finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
    }

    @Override
    public void deleteProductPeriodAgree(int numAgree, int productNumber) throws SQLException {
        if(this.periodAgreementsList.containsKey(numAgree)) {
            this.periodAgreementsList.get(numAgree).removeProductAgreement(productNumber);
        }
        this.periodDao.removeProById( numAgree, productNumber);
    }


    @Override
    public void deletePeriodAgreement(int numSup, int numAgree) throws SQLException{
        if(this.periodAgreementsList.containsKey(numAgree)) {
            this.periodAgreementsList.remove(numAgree);
        }
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            SupplierRepository s = SupplierRepositoryImpl.getInstance();
            s.removeAgreementToSup(numSup, numAgree);
            this.periodDao.removePeriod(numAgree);
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


    @Override
    public Optional<PeriodAgreementDto> getPeriodAgreement(int numA) throws SQLException {
        if(this.periodAgreementsList.containsKey(numA)){
            return Optional.of(PeriodAgreementMapper.transfer(this.periodAgreementsList.get(numA)));
        }
        Optional<PeriodAgreementDto> optionalAgree = this.periodDao.findPeriodAgreeById(numA);

        if (optionalAgree.isPresent()) {
            PeriodAgreementDto agree = optionalAgree.get();
            this.periodAgreementsList.put(numA,PeriodAgreementMapper.toObject(agree));
        }
        return optionalAgree;
    }

    @Override
    public void setAddressAgreePeriod(int numAgreement, String address)throws SQLException {
        if(this.periodAgreementsList.containsKey(numAgreement)) {
            this.periodAgreementsList.get(numAgreement).setAddress(address);
        }
        this.periodDao.updateAddressPeriodById( numAgreement,address);
    }

    @Override
    public void setContactPhoneAgreePeriod(int numAgreement, String ContactPhone) throws SQLException {
        if(this.periodAgreementsList.containsKey(numAgreement)) {
            this.periodAgreementsList.get(numAgreement).setContactPhone(ContactPhone);
        }
        this.periodDao.updateContactPhonePeriodById( numAgreement,ContactPhone);
    }

    @Override
    public void setAmountPeriodOrder(int productNumber, int numAgree, int amount) throws SQLException{
        if(this.periodAgreementsList.containsKey(numAgree)) {
            this.periodAgreementsList.get(numAgree).setAmount(productNumber,amount);
        }
        this.periodDao.updateAmountById( numAgree, productNumber,amount);
    }

    @Override
    public List<PeriodAgreementDto> allAgreementsByPeriodSup(int numS) throws SQLException{
        List<PeriodAgreementDto> optionalAgrees = this.periodDao.findAllPeriodAgreeBySupId(numS);
        for (PeriodAgreementDto agree:optionalAgrees) {
                this.periodAgreementsList.put(agree.IDNumber(), PeriodAgreementMapper.toObject(agree));
            }
        return optionalAgrees;
    }
    public List<PeriodAgreementDto> allPeriodAgreementsToOrder(String day,String date) throws SQLException{

        LinkedList<PeriodAgreementDto> agreeToOrders = this.periodDao.findPeriodAgreementsToOrder(day,date);
        for (PeriodAgreementDto agree:agreeToOrders) {
            this.periodAgreementsList.put(agree.IDNumber(), PeriodAgreementMapper.toObject(agree));
        }
        return agreeToOrders;
    }
}
