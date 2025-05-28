package dto;



import java.util.LinkedList;

public record SupplierDto(
        int supplierNumber, //the supplier ID unique for each supplier
        String supplierName,    //the name of a supplier
        String bankAccount,    //bank account number for transaction
        String payment,   //method of payment
        LinkedList<String>contactNames,    //a list contact of the supplier
        String telephone,   //the contact information of the supplier
        LinkedList<String> deliveryDays,  //the days delivery according to the agreement
        String deliverySending,  // repeating the order
        LinkedList<Integer> agreementsId
) {
}
