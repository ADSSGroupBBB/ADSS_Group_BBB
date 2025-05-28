package dto;

import Domain.QuantityAgreement;

public record ItemOrderDto(
        QuantityAgreementDto itemDto,
         int amountOrder,
         double finalPrice,
         double initialPrice,
         int numOrder
) {
}
