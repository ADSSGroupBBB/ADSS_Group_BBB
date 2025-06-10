package Domain;

import dto.ItemOrderDto;
import dto.OrderDto;

import java.util.LinkedList;

public class OrderMapper {

    public static OrderDto transfer(Order o) {
        LinkedList<ItemOrderDto> itemsDto = new LinkedList<>();
        for (ItemOrder item : o.getItems()) {
            itemsDto.add(ItemOrderMapper.transfer(item));
        }

        return new OrderDto(
                o.getOrderNumber(),
                o.getNumAgreement(),
                o.getSupplierName(),
                o.getSupplierNumber(),
                o.getAddress(),
                o.getDate(),
                o.getContactPhone(),
                itemsDto,
                o.getStatusOrder().name()
        );
    }

    public static Order toObject(OrderDto dto) {
        Order order = new Order(
                dto.numAgreement(),
                dto.supplierName(),
                dto.supplierNumber(),
                dto.address(),
                dto.date(),
                dto.contactPhone(),
                stringToEnumStatus(dto.statusOrder())
        );

        for (ItemOrderDto itemDto : dto.items()) {
            ItemOrder item = ItemOrderMapper.toObject(itemDto, dto.numAgreement());
            order.getItems().add(item);
        }

        return order;
    }
    public static Status stringToEnumStatus(String str) {
        Status st;
        if (str.equals("shipped")) {
            st = Status.shipped;
        } else if (str.equals("deleted")) {
            st = Status.deleted;
        } else {
            st = Status.arrived;
        }
        return st;
    }
}


