package dto;

public record PeriodOrderItemDto (
        QuantityAgreementDto productAgreement,
         int amountToOrder
){
}
