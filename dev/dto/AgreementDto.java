package dto;


import java.util.LinkedList;

public record AgreementDto(
        int IDNumber,  //agreement ID
        int supplierNumber, //the supplier this agreement belongs to
        LinkedList<QuantityAgreementDto>productsList, //the list of products for this agreement
        String date
) {
}
