package dto;

public record QuantityAgreementDto(
        int prodId,
        double price,
        int catalogNumber,
        int amountToDiscount,
        int discount
) {
}
