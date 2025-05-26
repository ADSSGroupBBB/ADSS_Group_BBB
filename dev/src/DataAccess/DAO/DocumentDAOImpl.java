package DataAccess.DAO;

import DTO.*;
import DataAccess.Interface.DocumentDAO;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentDAOImpl implements DocumentDAO {

    @Override
    public Optional<DocumentDTO> findById(int documentId) throws SQLException {
        String sql = "SELECT document_id, date, truck_id, dep_hour, driver_id, dep_from, event_message FROM documents WHERE document_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, documentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    List<ShipmentItemDTO> items = findItemsByDocumentId(documentId);
                    List<LocationDTO> destinations = findDestinationsByDocumentId(documentId);

                    DocumentDTO doc = new DocumentDTO(
                            rs.getInt("documentId"),
                            items,
                            rs.getString("date"),
                            rs.getString("truck_id"),
                            rs.getString("dep_hour"),
                            rs.getString("driver_id"),
                            rs.getString("dep_from"),
                            destinations,
                            rs.getString("event_message")
                    );
                    return Optional.of(doc);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<DocumentDTO> findAll() throws SQLException {
        List<DocumentDTO> documents = new ArrayList<>();
        String sql = "SELECT document_id FROM documents ORDER BY document_id";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int docId = rs.getInt("document_id");
                findById(docId).ifPresent(documents::add);
            }
        }
        return documents;
    }

    @Override
    public DocumentDTO save(DocumentDTO doc) throws SQLException {
        String sql = "INSERT INTO documents (document_id, date, truck_id, dep_hour, driver_id, dep_from, event_message) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, doc.docID());
            ps.setString(2, doc.date());
            ps.setString(3, doc.truck_id());
            ps.setString(4, doc.dep_hour());
            ps.setString(5, doc.driver_id());
            ps.setString(6, doc.dep_from());
            ps.setString(7, doc.eventMessage());
            ps.executeUpdate();
        }

        saveItems(doc.docID(), doc.items());
        saveDestinations(doc.docID(), doc.destinations());

        return doc;
    }

    private List<ShipmentItemDTO> findItemsByDocumentId(int docId) throws SQLException {
        List<ShipmentItemDTO> items = new ArrayList<>();
        String sql = "SELECT name, weight, amount FROM shipment_items WHERE document_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, docId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShipmentItemDTO item = new ShipmentItemDTO(
                            rs.getString("name"),
                            rs.getInt("weight"),
                            rs.getInt("amount")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    private List<LocationDTO> findDestinationsByDocumentId(int docId) throws SQLException {
        List<LocationDTO> destinations = new ArrayList<>();
        String sql = "SELECT address, contact_name, phone, zoneName, zoneRank FROM document_destinations WHERE document_id = ?";

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, docId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShippingZoneDTO zone = new ShippingZoneDTO(
                            rs.getString("zoneName"),
                            rs.getInt("zoneRank")
                    );

                    LocationDTO loc = new LocationDTO(
                            rs.getString("address"),
                            rs.getString("contact_name"),
                            rs.getString("phone"),
                            zone
                    );

                    destinations.add(loc);
                }
            }
        }

        return destinations;
    }

    private void saveItems(int docId, List<ShipmentItemDTO> items) throws SQLException {
        String sql = "INSERT INTO shipment_items (document_id, name, weight, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            for (ShipmentItemDTO item : items) {
                ps.setInt(1, docId);
                ps.setString(2, item.name());
                ps.setInt(3, item.weight());
                ps.setInt(4, item.amount());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveDestinations(int docId, List<LocationDTO> destinations) throws SQLException {
        String sql = "INSERT INTO document_destinations (document_id, address, contact_name, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            for (LocationDTO loc : destinations) {
                ps.setInt(1, docId);
                ps.setString(2, loc.address());
                ps.setString(3, loc.contact_name());
                ps.setString(4, loc.contact_num());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
