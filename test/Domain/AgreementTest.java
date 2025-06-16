package Domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgreementTest {

    private Agreement agreement;
    private Product product; // assuming you have a simple Product class

    @BeforeEach
    void setUp() {
        agreement = new Agreement(1,123, "01-01-2025");
        product = new Product( "ProductName",  3, unit.g, "manufacturer");
    }

    @Test
    void testAddProductAgreement() {
        agreement.addProductAgreement(product, 10.0, 101, 5, 10);
        assertEquals(1, agreement.getProductsList().size());
    }

    @Test
    void testSearchProduct_NotFound() {
        assertFalse(agreement.searchProduct(999));
    }
    @Test
    void testSetUnitOfMeasure() {
        agreement.addProductAgreement(product, 10.0, 101, 5, 10);
        agreement.setUnitOfMeasure(101, unit.kg); // assuming you have a unit enum
        assertEquals(unit.kg, agreement.getProductsList().getFirst().getUnitOfMeasureAgreement());
    }



}