package Domain;

import dataAccess.JdbcPeriodAgreementDao;
import dataAccess.PeriodAgreementDao;
import dto.*;

import java.sql.SQLException;
import java.util.*;

public class PeriodAgreementRepositoryImpl implements PeriodAgreementRepository{
    private PeriodAgreementDao periodDao;
    private Map<Integer, PeriodAgreement> periodAgreementsList;
    public PeriodAgreementRepositoryImpl(){
        this.periodDao=new JdbcPeriodAgreementDao();
        this.periodAgreementsList=new HashMap<>();
    }

    public PeriodAgreementDto savePeriodAgreement (int supplierNumber, String date,String address,String contactPhone) throws SQLException {
        SupplierRepository sRepo=SupplierRepository.getInstance();
        PeriodAgreement agree=new PeriodAgreement(supplierNumber,date,address,contactPhone);
        sRepo.addAgreementToSup(supplierNumber,agree.getIDNumber());
        this.periodAgreementsList.put(agree.getIDNumber(),agree);
        return this.periodDao.savePeriod(new PeriodAgreementDto(agree.getIDNumber(),agree.getSupplierNumber(),new LinkedList<PeriodAgreementItemDto>(),date,address,contactPhone));
    }

    @Override
    public void addProPeriodAgreement(int id, int numPro, double price, int catalogNumber, int amountToDiscount, int discount, int amountToOrder)  throws SQLException{
        if(this.periodAgreementsList.containsKey(id)) {
            ProductRepository p = ProductRepositoryImpl.getInstance();
            Optional<ProductDto> pro = p.getProd(numPro);
            this.periodAgreementsList.get(id).addProductAgreement(new Product(pro), price, catalogNumber, amountToDiscount, discount,amountToOrder);
        }
        this.periodDao.addProById( id, new PeriodAgreementItemDto(new QuantityAgreementDto(numPro,  price, catalogNumber, amountToDiscount, discount),amountToOrder));
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
        SupplierRepository s = SupplierRepositoryImpl.getInstance();
        s.removeAgreementToSup(numSup, numAgree);
        this.periodDao.removePeriod(numAgree);
    }


    @Override
    public Optional<PeriodAgreementDto> getPeriodAgreement(int numA) throws SQLException {
        if(this.periodAgreementsList.containsKey(numA)){
            return Optional.of(this.periodAgreementsList.get(numA).transfer());
        }
        Optional<PeriodAgreementDto> optionalAgree = this.periodDao.findPeriodAgreeById(numA);

        if (optionalAgree.isPresent()) {
            PeriodAgreementDto agree = optionalAgree.get();
            this.periodAgreementsList.put(numA, new PeriodAgreement(agree));
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
    public List<Optional<PeriodAgreementDto>> allAgreementsByPeriodSup(int numS) throws SQLException{
        List<Optional<PeriodAgreementDto>> optionalAgrees = this.periodDao.findAllPeriodAgreeBySupId(numS);
        for (Optional<PeriodAgreementDto> agree:optionalAgrees) {
            if (agree.isPresent()) {
                PeriodAgreementDto a = agree.get();
                this.periodAgreementsList.put(a.IDNumber(), new PeriodAgreement(a));
            }
        }
        return optionalAgrees;
    }


}
