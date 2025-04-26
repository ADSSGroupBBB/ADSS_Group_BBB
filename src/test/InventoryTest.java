package test;
import inventory.Inventory;
import inventory.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class InventoryTest {

    @Test
    public void testAddItemAndFind() {
        Inventory inventory = new Inventory();
        Item item = new Item(1, "Milk", 5, 10, "A1", 3.0, 5.0, new Date(), null, null);
        inventory.addItem(item);
        Assertions.assertEquals(item, inventory.findItemById(1));
    }

    @Test
    public void testGetItemsBelowMinQuantity() {
        Inventory inventory = new Inventory();
        Item item = new Item(1, "Milk", 5, 10, "A1", 3.0, 5.0, new Date(), null, null);
        inventory.addItem(item);
        Assertions.assertEquals(1, inventory.getItemsBelowMinQuantity().size());
    }
}
