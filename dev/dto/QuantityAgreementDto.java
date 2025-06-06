package dto;

public record QuantityAgreementDto(
        ProductDto pro,
        double price,
        int catalogNumber,
        int amountToDiscount,
        int discount
) {
}
