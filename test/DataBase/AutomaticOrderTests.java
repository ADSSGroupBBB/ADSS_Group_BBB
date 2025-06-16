package DataBase;
import java.lang.Exception;

import static org.junit.jupiter.api.Assertions.*;

import Domain.*;
import dto.AgreementDto;
import dto.PeriodAgreementDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomaticOrderTests {

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
    public void test_MissingProductWithValidAgreement_shouldCreateOrder() throws SQLException {
        int productId = 2;
        Map<Integer,Integer> pro_sell=new HashMap<Integer,Integer>();
        pro_sell.put(productId,15);
        stockControl.sell(pro_sell);
        int current=stockControl.getCurrentAmount(productId);
        int minimum=stockControl.getMinimumAmount(productId);
        assertFalse(stockControl.getBeOrder(2));
        String ord=orderControl.addMissOrder();
        assertTrue(ord.contains("1 orders created"));
        assertTrue(stockControl.getBeOrder(2));
        int numOrder=extractOrderNumber(ord);
        assertEquals(1,orderControl.orderBYnum(numOrder).getItems().size());
        orderControl.arriveOrder(numOrder);
        assertEquals(current+minimum,stockControl.getCurrentAmount(productId));
    }

    @Test
    public void test_MissingProductChooseBestAgreement() throws SQLException {
        int productId = 1;
        Map<Integer,Integer> pro_sell=new HashMap<Integer,Integer>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);
        pro_sell.put(1,15);
        stockControl.sell(pro_sell);
        int current=stockControl.getCurrentAmount(productId);
        int minimum=stockControl.getMinimumAmount(productId);
        assertFalse(stockControl.getBeOrder(1));
       AgreementDto a =agreementsControl.agreementMostEffectivePrice(1,minimum);
        assertEquals(6,a.IDNumber());
        String ord=orderControl.addMissOrder();
        assertTrue(ord.contains("1 orders created"));
        assertTrue(stockControl.getBeOrder(productId));
        int numOrder=extractOrderNumber(ord);
        assertEquals(1,orderControl.orderBYnum(numOrder).getItems().size());
        int numSup=extractSupplierNumber(ord);
        assertEquals(2,numSup);
        orderControl.arriveOrder(numOrder);
        assertEquals(current+minimum,stockControl.getCurrentAmount(productId));


    }

     @Test
     public void test_MissingProductWithNoAgreement_updateOrder()  throws SQLException{
         int productId = 3;
         Map<Integer,Integer> pro_sell=new HashMap<Integer,Integer>();
         pro_sell.put(productId,15);
         stockControl.sell(pro_sell);
         int current=stockControl.getCurrentAmount(productId);
         int minimum= stockControl.getMinimumAmount(productId);
         assertFalse(stockControl.getBeOrder(productId));
         String ord=orderControl.addMissOrder();
         assertTrue(ord.contains("1 orders created"));
         assertTrue(stockControl.getBeOrder(3));
         int numOrder=extractOrderNumber(ord);
         orderControl.arriveOrder(numOrder);
         assertEquals(current+minimum,stockControl.getCurrentAmount(productId));
         assertEquals(1,orderControl.orderBYnum(numOrder).getItems().size());
     }

     @Test
     public void test_PeriodicOrderEditOnDay()  throws SQLException{
        List<PeriodAgreementDto> p=agreementsControl.getAllPeriodToOrder();
        if(p.size()>0){
            assertFalse(agreementsControl.periodAgreeCanEdit(p.get(0).supplierNumber()));
        }
     }

     @Test
     public void test_MissProductWithoutAgree() throws SQLException{
         int productId = 6;
         Map<Integer,Integer> pro_sell=new HashMap<Integer,Integer>();
         pro_sell.put(productId,15);
         stockControl.sell(pro_sell);
         AgreementDto a=agreementsControl.agreementMostEffectivePrice(productId,20);
         assertNull(a);
     }





    // @Test
    // public void test_SameSupplierForMultipleProducts_SameOrder() { ... }

    // וכו'
    public static int extractOrderNumber(String printOrderString) {
        String[] lines = printOrderString.split("\n");
        for (String line : lines) {
            if (line.startsWith("orderNumber:")) {
                String numStr = line.substring("orderNumber:".length()).trim();
                return Integer.parseInt(numStr);
            }
        }
        return -1;
    }
    public int extractSupplierNumber(String orderString) {
        String[] lines = orderString.split("\n");
        for (String line : lines) {
            if (line.startsWith("supplierNumber:")) {
                String numStr = line.substring("supplierNumber:".length()).trim();
                return Integer.parseInt(numStr);
            }
        }
        return -1;
    }

}
