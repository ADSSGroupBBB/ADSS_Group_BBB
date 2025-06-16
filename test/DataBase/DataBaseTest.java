package DataBase;

import Domain.*;
import dto.ProductDto;
import dto.SupplierDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Optional;

public class DataBaseTest {
    private OrderController orderControl;
    private StockController stockControl;
    private SupplierController supplierControl;
    private AgreementsController agreementsControl;
    private ProductController productControl;

    @BeforeAll
    public static void setupAll() {
        try {
            util.DatabaseManager.connect("default_data.db");
            util.Database.createTablesIfNotExist(util.DatabaseManager.getConnection());
            util.DataLoad.loadDefaultData(util.DatabaseManager.getConnection());
        }
        catch (Exception e) {
            System.out.println("Boot failed");
            e.printStackTrace();

        }

    }

    @BeforeEach
    public void setup() {
        try {
            orderControl = OrderController.getInstance();
            stockControl = StockController.getInstance();
            supplierControl = SupplierController.getInstance();
            agreementsControl = AgreementsController.getInstance();
            productControl=ProductController.getInstance();
        }
        catch (Exception e) {
            System.out.println("Boot failed");
            e.printStackTrace();

        }
    }

    @Test
    public void testAddProToAgree() throws SQLException {
        productControl.addNewProduct("banana",15,"kg","manu");
        agreementsControl.addProToStandardAgreement(1,15,10,13,10,10);
        assertEquals("manu",agreementsControl.searchPro(1,15).getProd().getManufacturer());
    }

    @Test
    public void testUpdateSupplier() throws SQLException {
        supplierControl.setAddressSup(1,"Beer-sheva");
        Optional<SupplierDto> s= supplierControl.getSupplier(1);
        assertEquals("Beer-sheva",s.get().address());
    }

    @Test
    public void testUpdatePro() throws SQLException {
        productControl.setManufacturerPro(1,"good");
        Optional<ProductDto> s= productControl.getPro(1);
        assertEquals("good",s.get().manufacturer());
    }

    @Test
    public void testProOnAgree() throws SQLException {
        assertFalse(agreementsControl.existProByRegularSup(1,2));
        assertTrue(agreementsControl.existProByRegularSup(2,2));
        assertTrue(agreementsControl.existProByPeriodSup(1,6));
    }
    @Test
    public void testIsConstance() throws SQLException {
        assertTrue(supplierControl.isConstantSup(1));
        assertTrue(supplierControl.checkdaysSup(1,"Sunday"));
    }






}
