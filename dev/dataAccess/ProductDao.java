package dataAccess;

import dto.ProductDto;

import java.sql.SQLException;
import java.util.Optional;

public interface ProductDao {
    Optional<ProductDto> findProById(int num) throws SQLException;
     ProductDto savePro(ProductDto pro) throws SQLException ;
     void updateNameProById(int productNumber, String productName) throws SQLException ;
     void updateUnitOfMeasureById(int productNumber, String unitOfMeasure) throws SQLException ;
     void updateManufacturerById(int productNumber, String manufacturer) throws SQLException ;
    }
