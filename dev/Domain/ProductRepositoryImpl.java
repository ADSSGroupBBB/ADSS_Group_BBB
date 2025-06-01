package Domain;

import dataAccess.JdbcProductDao;
import dataAccess.JdbcSupplierDao;
import dataAccess.ProductDao;
import dataAccess.SupplierDao;
import dto.ProductDto;
import java.sql.SQLException;


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
    public Optional<ProductDto> getProd(int num) throws SQLException {
        if(proList.containsKey(num)){
            return Optional.of((proList.get(num)).transfer());
        }
        Optional<ProductDto> optionalPro = this.proDao.findProById(num);
        if (optionalPro.isPresent()) {
            ProductDto pro = optionalPro.get();
            proList.put(num, new Product(pro));
        }
        return optionalPro;
    }

    @Override
    public ProductDto addPro(String productName, int productNumber, String unitOfMeasure, String manufacturer) throws SQLException {
        proList.put(productNumber,new Product(productName,productNumber,StringToEnumUnit(unitOfMeasure),manufacturer));
        return this.proDao.savePro(new ProductDto(productName, productNumber,  unitOfMeasure,  manufacturer));
    }

    @Override
    public void updateName(int productNumber, String productName) throws SQLException {
        this.proDao.updateNameProById(productNumber,productName);
        if(proList.containsKey(productNumber)){
            proList.get(productNumber).setProductName(productName);
        }
    }

    @Override
    public void updateUnitOfMeasure(int productNumber, String unitOfMeasure) throws SQLException {
        this.proDao.updateUnitOfMeasureById(productNumber,unitOfMeasure);
        if(proList.containsKey(productNumber)){
            proList.get(productNumber).setUnitOfMeasure(StringToEnumUnit(unitOfMeasure));
        }
    }

    @Override
    public void updateManufacturer(int productNumber, String manufacturer) throws SQLException {
        this.proDao.updateManufacturerById(productNumber,manufacturer);
        if(proList.containsKey(productNumber)){
            proList.get(productNumber).setManufacturer(manufacturer);
        }
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

