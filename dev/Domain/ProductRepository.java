package Domain;


import dto.ProductDto;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.Optional;

public interface ProductRepository {
    Optional<ProductDto> getProd(int num);
    ProductDto addPro(String productName,int productNumber,String unitOfMeasure,String manufacturer);
    void updateName(int productNumber,String productName);
    void updateUnitOfMeasure(int productNumber,String unitOfMeasure);
     void updateManufacturer(int productNumber,String manufacturer);

    }
