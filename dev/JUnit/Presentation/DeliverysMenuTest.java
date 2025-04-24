package Presentation;
import Domain.Location;
import Domain.Shipment_item;
import Domain.Shipping_Zone;
import Domain.UserController;
import Service.UserApplication;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DeliverysMenuTest {

    private UserApplication userApplication;
    private UserController controller;

    @Before
    public void setUp() {
        // Initialize the UserApplication and UserController objects
        userApplication = new UserApplication();
        controller = new UserController();

        // Initialize the test data from UserController
        UserController.initBaseData();  // Initialize base data using the method from UserController
    }

    @Test
    public void testViewDocumentation() {
        // Create some mock Shipment_item objects
        Shipment_item item1 = new Shipment_item(10, "Item1");
        Shipment_item item2 = new Shipment_item(20, "Item2");
        Shipment_item item3 = new Shipment_item(15, "Item3");
        Shipment_item item4 = new Shipment_item(25, "Item4");

        // Create mock Shipping_Zone objects with rank and zone names
        Shipping_Zone zone1 = new Shipping_Zone(1, "Zone1");
        Shipping_Zone zone2 = new Shipping_Zone(2, "Zone2");

        // Create some mock Location objects for destinations with Shipping_Zone and Shipment_item
        Location location1 = new Location("Contact1", "123456789", "Address1", zone1);
        Location location2 = new Location("Contact2", "987654321", "Address2", zone2);

        // Assign items required for each location
        location1.setItems_required(Arrays.asList(item1, item2));
        location2.setItems_required(Arrays.asList(item3, item4));

        // Create the items list for the documents
        List<Shipment_item> items1 = Arrays.asList(item1, item2);
        List<Shipment_item> items2 = Arrays.asList(item3, item4);

        // Add documents with the required Location and Shipment_item data
        String docId1 = userApplication.addDocument(
                items1, "2023-04-23", "T001", "09:00", "D001", "Warehouse Address", Arrays.asList(location1, location2), "Test document 1"
        );
        String docId2 = userApplication.addDocument(
                items2, "2023-04-24", "T002", "10:00", "D002", "Customer Address", Arrays.asList(location2), "Test document 2"
        );

        // Simulate the functionality of the viewDocumentation() method
        String documentationIds = userApplication.printDocIDS();  // This should return the IDs of added documents

        // Check if the document IDs are returned correctly
        assertTrue(documentationIds.contains(docId1));
        assertTrue(documentationIds.contains(docId2));

        // Now simulate viewing the document details
        String documentDetails1 = userApplication.printDocument(docId1);
        String documentDetails2 = userApplication.printDocument(docId2);

        // Verify the document content includes the expected information
        assertTrue(documentDetails1.contains("Test document 1"));
        assertTrue(documentDetails2.contains("Test document 2"));

        // Check that the Locations and items are properly included
        assertTrue(documentDetails1.contains("Address1"));
        assertTrue(documentDetails1.contains("Item1"));
        assertTrue(documentDetails2.contains("Address2"));
        assertTrue(documentDetails2.contains("Item3"));

        // Ensure the Shipping_Zone information is included
        assertTrue(documentDetails1.contains("Zone1"));
        assertTrue(documentDetails2.contains("Zone2"));
    }
}

