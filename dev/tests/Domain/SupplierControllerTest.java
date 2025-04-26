package Domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
class SupplierControllerTest {
    private SupplierController controller;

    @BeforeEach
    public void setUp() {
        controller = new SupplierController();
    }
    @Test
    public void testAddNewSupplierAndCheckExists() {
        LinkedList<String> contacts = new LinkedList<>();
        contacts.add("John Doe");
        LinkedList<String> days = new LinkedList<>();
        days.add("Monday");
        days.add("Wednesday");

        controller.addNewSupplier(1, "Supplier A", "123-456", "Cash", contacts, "050-0000000", days, "constant");

        assertTrue(controller.checkSup(1));
        assertEquals("Supplier A", controller.getName(1));
    }
    @Test
    public void testCheckContactName() {
        LinkedList<String> contacts = new LinkedList<>();
        contacts.add("Alice");
        controller.addNewSupplier(2, "Supplier B", "789-101", "Credit", contacts, "050-1111111", new LinkedList<>(), "invitation");

        assertTrue(controller.checkContactName(2, "Alice"));
        assertFalse(controller.checkContactName(2, "Bob"));
    }

    @Test
    public void testAddAndDeleteContactNames() {
        LinkedList<String> contacts = new LinkedList<>();
        controller.addNewSupplier(3, "Supplier C", "456-789", "Cash", contacts, "050-2222222", new LinkedList<>(), "selfCollection");

        LinkedList<String> newContacts = new LinkedList<>();
        newContacts.add("Charlie");
        controller.addContactNamesSup(3, newContacts);

        assertTrue(controller.checkContactName(3, "Charlie"));

        controller.deleteContactNamesSup(3, "Charlie");

        assertFalse(controller.checkContactName(3, "Charlie"));
    }
    @Test
    public void testDeleteSupplier() {
        LinkedList<String> contacts = new LinkedList<>();
        controller.addNewSupplier(7, "Supplier G", "111-222", "Credit", contacts, "050-6666666", new LinkedList<>(), "selfCollection");

        assertTrue(controller.checkSup(7));

        controller.deleteSup(7);

        assertFalse(controller.checkSup(7));
    }
}