package DomainDelivery;
import Service.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitTests {}
/**
    @Before
    public void setUp() {
        LocationApplication la = new LocationApplication();
        DeliveriesController dc = new DeliveriesController();
        DeliveriesController.initBaseData(); // Initialize the test data
    }


    @Test
    public void testCompleteDeliveryProcess() {
        // This test simulates a complete delivery process through the UserApplication layer

        // 1. Add a new zone
        String zoneName = "DeliveryZone";
        String result = userApplication.addShippingZone(5, zoneName);
        assertTrue(result.contains("New shipping zone created"));

        // 2. Add locations
        result = userApplication.insertLocation("Warehouse", "555-0000", "Warehouse Address", zoneName);
        assertTrue(result.contains("New location added"));

        result = userApplication.insertLocation("Customer", "555-1111", "Customer Address", zoneName);
        assertTrue(result.contains("New location added"));

        // 3. Create a route
        List<Location> route = new ArrayList<>();
        result = userApplication.addDestination("Warehouse Address", route);
        assertEquals("Location added successfully.", result);

        result = userApplication.addDestination("Customer Address", route);
        assertEquals("Location added successfully.", result);

        // 4. Add items to the route
        boolean itemExists = userApplication.setItem("Milk");
        assertTrue(itemExists);

        result = userApplication.setRequiredItemInRoute(route, "Milk", 3);
        assertTrue(result.contains("Added 3 of Milk"));

        // 5. Get a truck and check weight
        try {
            int weight = userApplication.weightRouteItems("T2", route);
            assertTrue(weight > 0);
        } catch (WeightEx e) {
            fail("Weight exception should not be thrown with this configuration");
        }

        // 6. Sort the route by zones
        userApplication.sortRouteAccordingToZones(route);

        // 7. Get total items
        List<Shipment_item> items = userApplication.getTotalItems(route);
        assertFalse(items.isEmpty());

        // 8. Create delivery document
        String docId = userApplication.addDocument(
                items, "2023-04-23", "T2", "09:00",
                "D1", "Warehouse Address", route, "Test integration delivery"
        );
        assertNotNull(docId);

        // 9. Get document details
        String docInfo = userApplication.printDocument(docId);
        assertTrue(docInfo.contains(docId));

        // 10. End the delivery
        result = userApplication.endDelivery(docId);
        assertTrue(result.contains("Delivery ended successfully"));

        // Clean up
        userApplication.deleteLocation("Warehouse Address");
        userApplication.deleteLocation("Customer Address");
        userApplication.deleteZone(zoneName);
    }

    @Test
    public void testTruckDriverCompatibility() {
        // Test truck and driver compatibility through the UserApplication

        // 1. Add a new driver with specific licenses
        String driverId = "TestDriver";
        String result = userApplication.insertDriver(driverId, "Test Driver", Arrays.asList(111, 222));
        assertTrue(result.contains("New driver added"));

        // 2. Add trucks with different types
        String truck1Id = "TestTruck1";
        result = userApplication.insertTruck(truck1Id, 111, 1000, 5000);
        assertEquals("Truck added: " + truck1Id, result);

        String truck2Id = "TestTruck2";
        result = userApplication.insertTruck(truck2Id, 333, 1000, 5000);
        assertEquals("Truck added: " + truck2Id, result);

        // 3. Test compatibility
        result = userApplication.isAvailableDriver(driverId, truck2Id);
        assertEquals("Driver doesn't have correct license for this truck type", result);

        // Clean up
        userApplication.deleteDriver(driverId);
        userApplication.deleteTruck(truck1Id);
        userApplication.deleteTruck(truck2Id);
    }

    @Test
    public void testStorageAlertFlow() {
        // Test storage alert handling

        // 1. Add necessary data
        String zoneName = "StorageZone";
        userApplication.addShippingZone(5, zoneName);

        userApplication.insertLocation("Warehouse", "555-0000", "Headquarters", zoneName);
        userApplication.insertLocation("Store", "555-1111", "Store Address", zoneName);

        // 2. Create route for storage alert
        List<Location> route = new ArrayList<>();
        userApplication.addDestination("Headquarters", route);
        userApplication.addDestination("Store Address", route);

        // 3. Add required items
        boolean itemExists = userApplication.setItem("Milk");
        assertTrue(itemExists);

        String result = userApplication.setRequiredItemInRoute(route, "Milk", 10);
        assertTrue(result.contains("Added 10 of Milk"));

        // 4. Check weight and create document
        try {
            int weight = userApplication.weightRouteItems("T2", route);
            assertTrue(weight > 0);

            List<Shipment_item> items = userApplication.getTotalItems(route);
            String docId = userApplication.addDocument(
                    items, "2023-04-23", "T2", "09:00",
                    "D1", "Headquarters", route, "Storage alert delivery"
            );

            assertNotNull(docId);
            userApplication.endDelivery(docId);
        } catch (WeightEx e) {
            fail("Weight exception should not be thrown with this configuration");
        }

        // Clean up
        userApplication.deleteLocation("Store Address");
        userApplication.deleteZone(zoneName);
    }

    @Test
    public void testWeightOverflowHandling() {
        // Test handling of weight overflow

        // 1. Add a small capacity truck
        String truckId = "SmallTruck";
        userApplication.insertTruck(truckId, 111, 100, 150);

        // 2. Create route with heavy items
        String zoneName = "TestZone";
        userApplication.addShippingZone(5, zoneName);

        userApplication.insertLocation("Origin", "555-0000", "Origin Address", zoneName);
        userApplication.insertLocation("Dest", "555-1111", "Dest Address", zoneName);

        List<Location> route = new ArrayList<>();
        userApplication.addDestination("Origin Address", route);
        userApplication.addDestination("Dest Address", route);

        // 3. Add heavy items to the route
        userApplication.setRequiredItemInRoute(route, "Meat", 10); // Meat is 15 units each

        // 4. Test weight calculation - should trigger exception
        try {
            userApplication.weightRouteItems(truckId, route);
            fail("Expected WeightEx to be thrown");
        } catch (WeightEx e) {
            // Expected exception - now handle it

            // 5. Check if another truck is available
            String newTruckId = userApplication.changeTruck(route, truckId, "D1");
            if (newTruckId != null) {
                // Should be able to use a bigger truck
                assertNotEquals(truckId, newTruckId);
            } else {
                // No bigger truck available, so items must be removed
                userApplication.removeItems(route, truckId, e.weight);

                // Try again - should work now
                try {
                    int newWeight = userApplication.weightRouteItems(truckId, route);
                    assertTrue(newWeight <= 150);
                } catch (WeightEx ex) {
                    fail("Weight should be under limit after removing items");
                }
            }
        }

        // Clean up
        userApplication.deleteLocation("Origin Address");
        userApplication.deleteLocation("Dest Address");
        userApplication.deleteZone(zoneName);
        userApplication.deleteTruck(truckId);
    }

    @Test
    public void testDriverAvailabilityTracking() {
        // Test tracking of driver availability during deliveries

        // 1. Create driver and truck
        String driverId = "AvailDriver";
        userApplication.insertDriver(driverId, "Available Driver", List.of(111));

        String truckId = "AvailTruck";
        userApplication.insertTruck(truckId, 111, 1000, 5000);

        // 2. Verify driver is initially available
        String result = userApplication.isAvailableDriver(driverId, truckId);
        assertEquals("Driver is available.", result);

        // 3. Create a delivery
        String zoneName = "AvailZone";
        userApplication.addShippingZone(5, zoneName);
        userApplication.insertLocation("Origin", "555-0000", "Origin Address", zoneName);

        List<Location> route = new ArrayList<>();
        userApplication.addDestination("Origin Address", route);

        List<Shipment_item> items = new ArrayList<>();
        Shipment_item item = new Shipment_item(10, "TestItem");
        items.add(item);

        String docId = userApplication.addDocument(
                items, "2023-04-23", truckId, "09:00",
                driverId, "Origin Address", route, "Availability test"
        );

        // 4. Verify driver is now unavailable
        result = userApplication.isAvailableDriver(driverId, truckId);
        assertNotEquals("Driver is available.", result);

        // 5. End delivery and check availability again
        userApplication.endDelivery(docId);
        result = userApplication.isAvailableDriver(driverId, truckId);
        assertEquals("Driver is available.", result);

        // Clean up
        userApplication.deleteDriver(driverId);
        userApplication.deleteTruck(truckId);
        userApplication.deleteLocation("Origin Address");
        userApplication.deleteZone(zoneName);
    }

    @Test
    public void testMultipleDestinationDelivery() {
        // Test a delivery with multiple destinations

        // Create a route with multiple destinations
        List<Location> route = new ArrayList<>();

        // Add origin
        Location origin = new Location("HQ", "555-0000", "Headquarters", new Shipping_Zone(1, "Downtown"));
        route.add(origin);

        // Add multiple destinations
        controller.addDestination("Derech Metzada 17", route);
        controller.addDestination("Bialik 19", route);
        controller.addDestination("BenGurion 1", route);

        // Verify route size
        assertEquals(4, route.size());

        // Add different items to route
        controller.setRequiredItemInRoute(route, "Milk", 2);
        controller.setRequiredItemInRoute(route, "Bread", 3);

        // Get total items and verify
        List<Shipment_item> totalItems = controller.getTotalItems(route);
        assertEquals(10, totalItems.size()); // 2 items * 3 destinations

        // Sort route by zones
        controller.sortRouteAccordingToZones(route);

        // Create document for this multi-destination delivery
        String docId = controller.addDocument(
                totalItems, "2023-04-23", "T2", "09:00",
                "D1", "Headquarters", route, "Multi-destination test"
        );

        // Verify document was created
        assertNotNull(docId);
        String docInfo = controller.printDocument(docId);
        assertTrue(docInfo.contains("Multi-destination test"));

        // Verify all destinations are in the document
        assertTrue(docInfo.contains("Derech Metzada 17"));
        assertTrue(docInfo.contains("Bialik 19"));
        assertTrue(docInfo.contains("BenGurion 1"));
    }

    @Test
    public void testTruckWeightCapacityLimits() {
        // Test truck weight capacity functionality

        // Insert a truck with specific weight limits
        String truckId = "WeightTest";
        controller.insertTruck(truckId, 111, 500, 1000);

        // Create a route
        List<Location> route = new ArrayList<>();
        Location origin = new Location("Origin", "111-111", "Origin Address", new Shipping_Zone(1, "Downtown"));
        route.add(origin);

        Location dest = new Location("Dest", "222-222", "Dest Address", new Shipping_Zone(2, "Suburbs"));
        List<Shipment_item> items = new ArrayList<>();

        // Add items with weight
        Shipment_item lightItem = new Shipment_item(50, "LightItem");
        lightItem.setAmount(5); // Total weight: 250
        items.add(lightItem);

        dest.setItems_required(items);
        route.add(dest);

        // Test weight calculation - should be within limits
        try {
            int weight = controller.weightRouteItems(truckId, route);
            assertEquals(750, weight); // 500 (truck) + 250 (items)

            // Now add heavier items to exceed limit
            Shipment_item heavyItem = new Shipment_item(300, "HeavyItem");
            heavyItem.setAmount(2); // Additional 600 weight
            items.add(heavyItem);

            // Should now throw exception
            controller.weightRouteItems(truckId, route);
            fail("Should have thrown WeightEx");
        } catch (WeightEx e) {
            // Expected exception for the second calculation
            assertEquals(1350, e.weight); // 500 (truck) + 250 (light items) + 600 (heavy items)
        }
    }

    @Test
    public void testDriverLicenseManagement() {
        // Test driver license management functionality

        // Create a driver with initial licenses
        String driverId = "LicenseTest";
        List<Integer> initialLicenses = new ArrayList<>(Arrays.asList(111, 222));
        controller.insertDriver(driverId, "License Test Driver", initialLicenses);

        // Add a new license
        String result = controller.addLicense(driverId, 333);
        assertEquals("License added successfully.", result);

        // Try to add the same license again (should fail)
        result = controller.addLicense(driverId, 333);
        assertEquals("License already exist.", result);

        // Delete a license
        result = controller.deleteLicense(driverId, 333);
        assertEquals("License removed successfully.", result);

        // Try to delete a non-existent license
        result = controller.deleteLicense(driverId, 444);
        assertEquals("License not found.", result);

        // Test compatibility with trucks
        controller.insertTruck("Truck111", 111, 1000, 2000);
        controller.insertTruck("Truck333", 333, 1000, 2000);

        // Should not be compatible with license 333 (we deleted it)
        result = controller.isAvailableDriver(driverId, "Truck333");
        assertEquals("Driver doesn't have correct license for this truck type", result);
    }

    @Test
    public void testDocumentLifecycleAndHistory() {
        // Test document creation, retrieval and history tracking

        // Create multiple documents
        List<Shipment_item> items = new ArrayList<>();
        Shipment_item item = new Shipment_item(10, "TestItem");
        items.add(item);

        List<Location> destinations = new ArrayList<>();
        Location origin = new Location("Origin", "111-111", "Origin Address", new Shipping_Zone(1, "Downtown"));
        destinations.add(origin);

        // Create first document
        String doc1Id = controller.addDocument(
                items, "2023-04-23", "T1", "09:00",
                "D1", "Origin Address", destinations, "First delivery"
        );

        // Create second document
        String doc2Id = controller.addDocument(
                items, "2023-04-24", "T2", "10:00",
                "D2", "Origin Address", destinations, "Second delivery"
        );

        // Verify both documents were created with different IDs
        assertNotNull(doc1Id);
        assertNotNull(doc2Id);
        assertNotEquals(doc1Id, doc2Id);

        // Retrieve document IDs list
        String docList = controller.printDocIDS();
        assertTrue(docList.contains(doc1Id));
        assertTrue(docList.contains(doc2Id));

        // Retrieve and verify document content
        String doc1Info = controller.printDocument(doc1Id);
        assertTrue(doc1Info.contains("First delivery"));

        String doc2Info = controller.printDocument(doc2Id);
        assertTrue(doc2Info.contains("Second delivery"));

        // Complete first delivery
        String endResult = controller.endDelivery(doc1Id);
        assertTrue(endResult.contains("Delivery ended successfully"));

        // Verify document status was updated
        doc1Info = controller.printDocument(doc1Id);
        assertTrue(doc1Info.contains("Delivery finished"));
    }


}*/