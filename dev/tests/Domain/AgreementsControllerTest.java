package Domain;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgreementsControllerTest {
    private AgreementsController controller;

    @BeforeEach
    public void setUp() {
        controller = new AgreementsController();
    }

    @Test
    public void testAddNewAgreement() {
        int supplierNumber = 1;
        String date = "2025-04-26";
        int agreementId = controller.addNewAgreement(supplierNumber, date);

        assertTrue(controller.existAgreement(supplierNumber, agreementId));
    }
    @Test
    public void testAddAndRemoveProduct() {
        int supplierNumber = 2;
        String date = "2025-04-26";
        int agreementId = controller.addNewAgreement(supplierNumber, date);

        controller.addProToAgreement(agreementId, 101, 50.0, 1001, 10, 5);
        assertTrue(controller.existProAgre(101, agreementId));

        controller.deleteProductFromAgree(agreementId, 101);
        assertFalse(controller.existProAgre(101, agreementId));
    }
    @Test
    public void testDeleteAgreement() {
        int supplierNumber = 3;
        String date = "2025-04-26";
        int agreementId = controller.addNewAgreement(supplierNumber, date);

        assertTrue(controller.existAgreement(supplierNumber, agreementId));

        controller.deleteAgree(supplierNumber, agreementId);

        assertFalse(controller.existAgreement(supplierNumber, agreementId));
    }

}