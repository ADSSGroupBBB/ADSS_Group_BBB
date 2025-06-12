package Domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class OrderTest {

    private Order order;
    private Product product;
    private QuantityAgreement quantityAgreement;

    @BeforeEach
    void setUp() {
        // Assuming Status is an enum or class you have
        order = new Order(1, "SupplierName", 123, "123 Main St", "2025-01-01", "123-4567890", Status.arrived);
        product = new Product("pro",21,unit.g,"man");
        // Mock QuantityAgreement
        quantityAgreement = new QuantityAgreement( 3, product, 101.0, 5, 10, 30);
    }

    @Test
    void testAddProductOrder_Duplicate() {
        order.addProductOrder(quantityAgreement, 5);
        boolean addedAgain = order.addProductOrder(quantityAgreement, 5);
        assertFalse(addedAgain, "Should not add duplicate item");
    }
    @Test
    void testPrintOrder() {
        order.addProductOrder(quantityAgreement, 5);
        String output = order.print_Order();

        assertTrue(output.contains("orderNumber:2"));
        assertTrue(output.contains("supplierName:SupplierName"));
        assertTrue(output.contains("Product"));
    }
    @Test
    void testSettersAndGetters() {
        order.setSupplierName("NewSupplier");
        assertEquals("NewSupplier", order.getSupplierName());

        order.setSupplierNumber(321);
        assertEquals(321, order.getSupplierNumber());

        order.setAddress("456 New St");
        assertEquals("456 New St", order.getAddress());

        order.setDate("2025-12-31");
        assertEquals("2025-12-31", order.getDate());

        order.setContactPhone("987-6543210");
        assertEquals("987-6543210", order.getContactPhone());

        order.setStatusOrder(Status.arrived);
        assertEquals(Status.arrived, order.getStatusOrder());
    }

}