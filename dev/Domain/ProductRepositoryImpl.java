package Domain;

import dataAccess.JdbcProductDao;
import dataAccess.JdbcSupplierDao;
import dataAccess.ProductDao;
import dataAccess.SupplierDao;
import dto.ProductDto;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository{
    private ProductDao proDao;
    private Map<Integer,Product> proList;
    public ProductRepositoryImpl(){
        this.proDao=new JdbcProductDao();
        this.proList=new HashMap<>();
    }

    @Override
    public Optional<ProductDto> getProd(int num) {
        Optional<ProductDto> optionalPro = this.proDao.findProById(num);
        if (optionalPro.isPresent()) {
            ProductDto pro = optionalPro.get();
            ?proList.put(num, pro);
        }
        return optionalPro;
    }

    @Override
    public ProductDto addPro(String productName, int productNumber, String unitOfMeasure, String manufacturer) {
        return this.proDao.savePro(new ProductDto(productName, productNumber,  unitOfMeasure,  manufacturer));
    }

    @Override
    public void updateName(int productNumber, String productName) {
        this.proDao.updateNameProById(productNumber,productName);
    }

    @Override
    public void updateUnitOfMeasure(int productNumber, String unitOfMeasure) {
        this.proDao.updateUnitOfMeasureById(productNumber,unitOfMeasure);
    }

    @Override
    public void updateManufacturer(int productNumber, String manufacturer) {
        this.proDao.updateManufacturerById(productNumber,manufacturer);
    }
}

