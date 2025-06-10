package Domain;

import dto.ItemOrderDto;
import dto.QuantityAgreementDto;

public class ItemOrderMapper {

    public static ItemOrderDto transfer(ItemOrder it) {
        QuantityAgreementDto qaDto = QuantityAgreementMapper.transfer(it.getItem());
        return new ItemOrderDto(
                qaDto,
                it.getAmountOrder(),
                it.getFinalPrice(),
                it.getInitialPrice(),
                it.getNumOrder()
        );
    }

    public static ItemOrder toObject(ItemOrderDto it,int numAgree) {
        QuantityAgreement qa = QuantityAgreementMapper.toObject(it.itemDto(),numAgree);
        return new ItemOrder(
                qa,
                it.amountOrder(),
                it.numOrder()
        );
    }
}
