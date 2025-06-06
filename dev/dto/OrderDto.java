package dto;


import java.util.LinkedList;

public record OrderDto(
         int orderNumber,//order ID
         int numAgreement,
         String supplierName,    //the name of the supplier the order is from
         int supplierNumber, //the number of the supplier the order is from
         String address, //the address for shipment
         String date,   //the date the order was made
         String contactPhone,    //contact info (phone number)
         LinkedList<ItemOrderDto> items,    //the items in the order
         String statusOrder //the status of the order
) {
}
