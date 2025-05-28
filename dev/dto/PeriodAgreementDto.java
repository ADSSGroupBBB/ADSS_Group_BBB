package dto;

import java.util.LinkedList;

public record PeriodAgreementDto(
        AgreementDto agreement,
        LinkedList<String> days,
        String address, //the address for shipment
        String contactPhone
) {
}
