package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import inventory.Discount;
import java.util.Date;

public class DiscountTest {

    @Test
    public void testIsActiveTrue() {
        Discount discount = new Discount(15.0,
                new Date(System.currentTimeMillis() - 10000),
                new Date(System.currentTimeMillis() + 10000));
        Assertions.assertTrue(discount.isActive());
    }

    @Test
    public void testIsActiveFalse() {
        Discount discount = new Discount(15.0,
                new Date(System.currentTimeMillis() - 20000),
                new Date(System.currentTimeMillis() - 10000));
        Assertions.assertFalse(discount.isActive());
    }
}
