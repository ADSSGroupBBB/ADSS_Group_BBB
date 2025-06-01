package Domain;


import dto.ProductDto;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.Optional;

public interface ProductRepository {
    Optional<ProductDto> getProd(int num) throws SQLException;
    ProductDto addPro(String productName,int productNumber,String unitOfMeasure,String manufacturer) throws SQLException;
    void updateName(int productNumber,String productName) throws SQLException;
    void updateUnitOfMeasure(int productNumber,String unitOfMeasure) throws SQLException;
    void updateManufacturer(int productNumber,String manufacturer) throws SQLException;
    }
