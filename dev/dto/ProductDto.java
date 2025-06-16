package dto;


public record ProductDto(
         String productName, //name of product
         int productNumber,  //product ID
         String unitOfMeasure, //the unit to measure a product
         String manufacturer
) {
}
