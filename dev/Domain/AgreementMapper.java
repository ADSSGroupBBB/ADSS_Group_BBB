package Domain;

import dto.AgreementDto;
import dto.QuantityAgreementDto;

import java.util.LinkedList;

public class AgreementMapper {

    public static AgreementDto transfer(Agreement agreement) {
        LinkedList<QuantityAgreementDto> productDtos = new LinkedList<>();
        for (QuantityAgreement qa : agreement.getProductsList()) {
            productDtos.add(QuantityAgreementMapper.transfer(qa));
        }
        return new AgreementDto(
                agreement.getIDNumber(),
                agreement.getSupplierNumber(),
                productDtos,
                agreement.getDate()
        );
    }

    public static Agreement toObject(AgreementDto dto) {
        LinkedList<QuantityAgreement> products = new LinkedList<>();
        for (QuantityAgreementDto qaDto : dto.productsList()) {
            products.add(QuantityAgreementMapper.toObject(qaDto,dto.IDNumber()));
        }
        return new Agreement(
                dto.IDNumber(),
                dto.supplierNumber(),
                products,
                dto.date()
        );
    }
}

