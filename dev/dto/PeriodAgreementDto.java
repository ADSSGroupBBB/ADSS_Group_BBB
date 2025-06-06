package dto;

import java.util.LinkedList;

public record PeriodAgreementDto(
        int IDNumber,  //agreement ID
        int supplierNumber, //the supplier this agreement belongs to
        LinkedList<PeriodAgreementItemDto>productsList, //the list of products for this agreement
        String date,
        String address, //the address for shipment
        String contactPhone
) {
}
