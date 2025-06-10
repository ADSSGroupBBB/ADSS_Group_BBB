package Domain;

import dto.QuantityAgreementDto;

public class QuantityAgreementMapper {

    public static QuantityAgreementDto transfer(QuantityAgreement qa) {
        return new QuantityAgreementDto(
                ProductMapper.transfer(qa.getProd()),
                qa.getPrice(),
                qa.getCatalogNumber(),
                qa.getAmountToDiscount(),
                qa.getDiscount()
        );
    }

    public static QuantityAgreement toObject(QuantityAgreementDto dto, int numAgreement) {
        return new QuantityAgreement(
                numAgreement,
                ProductMapper.toObject(dto.pro()),
                dto.price(),
                dto.catalogNumber(),
                dto.amountToDiscount(),
                dto.discount()
        );
    }
}
