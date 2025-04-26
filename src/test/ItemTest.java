package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import inventory.Item;
import inventory.Discount;


public class ItemTest {

    @Test
    public void testIsLowStockReturnsTrue() {
        Item item = new Item(1, "Milk", 3, 10, "A1", 3.0, 5.0, new Date(), null, null);
        Assertions.assertTrue(item.isLowStock());
    }

    @Test
    public void testIsLowStockReturnsFalse() {
        Item item = new Item(2, "Juice", 12, 10, "B1", 3.5, 6.0, new Date(), null, null);
        Assertions.assertFalse(item.isLowStock());
    }

    @Test
    public void testDiscountedPriceWithActiveDiscount() {
        Discount discount = new Discount(20.0,
                new Date(System.currentTimeMillis() - 1000),
                new Date(System.currentTimeMillis() + 100000));
        Item item = new Item(3, "Apple Juice", 5, 2, "C1", 2.0, 10.0, new Date(), null, discount);
        Assertions.assertEquals(8.0, item.getDiscountedPrice(), 0.01);
    }

    @Test
    public void testDiscountedPriceWithoutDiscount() {
        Item item = new Item(4, "Soda", 10, 5, "C2", 1.5, 6.0, new Date(), null, null);
        Assertions.assertEquals(6.0, item.getDiscountedPrice(), 0.01);
    }
}
