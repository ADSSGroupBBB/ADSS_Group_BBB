package Domain;

import dto.PeriodAgreementItemDto;
import dto.QuantityAgreementDto;

public class PeriodicOrderItemMapper {
    public static PeriodAgreementItemDto transfer(PeriodicOrderItem perIt) {
        return new PeriodAgreementItemDto(
                QuantityAgreementMapper.transfer(perIt),
                perIt.getAmountToOrder()
        );
    }

    public static PeriodicOrderItem toObject(PeriodAgreementItemDto dto, int numAgreement) {
        QuantityAgreementDto qaDto = dto.productAgreement();
        Product product = ProductMapper.toObject(qaDto.pro());
        return new PeriodicOrderItem(
                numAgreement,
                product,
                qaDto.price(),
                qaDto.catalogNumber(),
                qaDto.amountToDiscount(),
                qaDto.discount(),
                dto.amountToOrder()
        );
    }
}
