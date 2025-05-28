package Domain;

import dto.SupplierDto;

import java.util.LinkedList;
import java.util.Optional;

public class SupplierRepositoryImpl implements SupplierRepository{
    private SupplierDao supDao;
    private LinkedList<Supplier> supList;

    @Override
    public SupplierDto addSupplier(Supplier sup) {
        return this.supDao.save(new SupplierDto(int supplierNumber,String supplierName, String bankAccount, String payment, LinkedList<String>contactNames, String telephone, LinkedList<String> deliveryDays, String deliverySending, LinkedList<Integer> agreementsId));
    }

    @Override
    public Optional<SupplierDto> getSupplier(int id) {
        return  this.supDao.findSupById(id);
    }

    @Override
    public void update(Supplier sup) {

    }

    @Override
    public void remove(int id) {
        this.supDao.remove(id;

    }
}
