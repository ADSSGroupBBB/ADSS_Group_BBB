package Domain;

import dto.ProductDto;
import dto.SupplierDto;

public class ProductMapper {
    public static ProductDto transfer(Product pro) {
        return new ProductDto(pro.getProductName(),pro.getProductNumber(),(pro.getUnitOfMeasure()).name(),pro.getManufacturer());

    }
    public static Product toObject(ProductDto pro) {
        return new Product(pro.productName(),pro.productNumber(),StringToEnumUnit(pro.unitOfMeasure()),pro.manufacturer());

    }
    public static unit StringToEnumUnit(String unitOfMeasure){
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
