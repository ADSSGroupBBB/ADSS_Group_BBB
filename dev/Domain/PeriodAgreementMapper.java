package Domain;
import dto.PeriodAgreementDto;
import dto.PeriodAgreementItemDto;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class PeriodAgreementMapper {

    public static PeriodAgreementDto transfer(PeriodAgreement agree) {
        LinkedList<PeriodAgreementItemDto> dtoItems = new LinkedList<>();
        for (QuantityAgreement item : agree.getProductsList()) {
            PeriodAgreementItemDto dto = PeriodicOrderItemMapper.transfer((PeriodicOrderItem)item);
            dtoItems.add(dto);
        }


        return new PeriodAgreementDto(
                agree.getIDNumber(),
                agree.getSupplierNumber(),
                dtoItems,
                agree.getDate(),
                agree.getAddress(),
                agree.getContactPhone()
        );
    }

    public static PeriodAgreement toObject(PeriodAgreementDto agree) {
        PeriodAgreement agreement = new PeriodAgreement(
                agree.IDNumber(),
                agree.supplierNumber(),
                agree.date(),
                agree.address(),
                agree.contactPhone()
        );

        for (PeriodAgreementItemDto itemDto : agree.productsList()) {
            Product p = ProductMapper.toObject(itemDto.productAgreement().pro());
            double price = itemDto.productAgreement().price();
            int catalogNumber = itemDto.productAgreement().catalogNumber();
            int amountToDiscount = itemDto.productAgreement().amountToDiscount();
            int discount = itemDto.productAgreement().discount();
            int amountToOrder = itemDto.amountToOrder();

            agreement.addProductAgreement(p, price, catalogNumber, amountToDiscount, discount, amountToOrder);
        }

        return agreement;
    }

    }
