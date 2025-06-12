package Domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class SupplierTest {
    private Product product;
    private Supplier supplier;
    private Agreement agreement1;
    private Agreement agreement2;
    private QuantityAgreement quantityAgreement;

    @BeforeEach
    void setUp() {
        LinkedList<String> contactNames = new LinkedList<>();
        contactNames.add("John Doe");

        LinkedList<Days> deliveryDays = new LinkedList<>();
        deliveryDays.add(Days.Monday);

        LinkedList<Agreement> agreements = new LinkedList<>();
        product = new Product("pro",1,unit.g,"man");
        quantityAgreement = new QuantityAgreement( 1, product, 100.0, 1, 10, 5);
        agreement1 = new Agreement(1,"01-01-2025");
        agreement2=new Agreement(1,"02-02-2025");
        agreement1.addProductAgreement(product,43,3,22,10);
        agreements.add(agreement1);
        agreements.add(agreement2);
        supplier = new Supplier(1,"sup","bank",paymentTerms.Cash,contactNames,"0505050505"
        ,deliveryDays,Delivery.invitation,agreements,"t","e");

    }

    @Test
    void testExistProduct_ProductExists() {
        assertTrue(supplier.existProduct(1));
    }

    @Test
    void testExistProduct_ProductDoesNotExist() {
        assertFalse(supplier.existProduct(999));
    }

    @Test
    void testAddAndRemoveAgreement() {
        Agreement agreement3 = new Agreement(3,"02-03-2025");
        supplier.addAgreements(agreement3);

        assertTrue(supplier.numAgree().contains(Integer.toString(agreement3.getIDNumber())));

        supplier.removeAgreements(agreement3);
        assertFalse(supplier.numAgree().contains(Integer.toString(agreement3.getIDNumber())));
    }
    @Test
    void testNumAgree() {
        String agreements = supplier.numAgree();
        assertTrue(agreements.contains(Integer.toString(agreement1.getIDNumber())));
    }

}