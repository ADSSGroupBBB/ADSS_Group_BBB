package DataAccess.DAO;

import DataAccess.Interface.DocumentDAO;
import DataAccess.Interface.LocationDAO;
import DTO.*;
import util.Database_HR_DL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentDAOImpl implements DocumentDAO {

    @Override
    public Optional<DocumentDTO> findById(String documentId) throws SQLException {
        String sql = "SELECT doc_id, date, truck_id, dep_hour, driver_id, dep_from, event_message FROM documents WHERE doc_id = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, documentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    List<LocationDTO> destinations = findDestinationsByDocumentId(documentId);
                    List<ShipmentItemDTO> items = findItemsByDocumentId(documentId);

                    DocumentDTO doc = new DocumentDTO(
                            rs.getString("doc_id"),
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
        String sql = "SELECT doc_id FROM documents";
        try (Statement st = Database_HR_DL.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String docId = rs.getString("doc_id");
                findById(docId).ifPresent(documents::add);
            }
        }
        return documents;
    }

    @Override
    public DocumentDTO save(DocumentDTO doc) throws SQLException {
        String sql = "INSERT INTO documents (doc_id, date, truck_id, dep_hour, driver_id, dep_from, event_message) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, doc.docID());
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

    @Override
    public int getNextId() throws SQLException{
        String sql = "SELECT MAX(id) AS max_id FROM documents";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return rs.wasNull() ? 1 : maxId+1; // null if table is empty
            } else {
                return 1;
            }
        }
    }

    @Override
    public int getId(String document_id) throws SQLException{
        String sql = "SELECT id FROM documents WHERE doc_id = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)){
             ps.setString(1, document_id);
             ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return rs.wasNull() ? -1 : id; // null if table is empty
            } else {
                return -1;
            }
        }
    }

    private List<ShipmentItemDTO> findItemsByDocumentId(String docId) throws SQLException {
        List<ShipmentItemDTO> items = new ArrayList<>();
        String sql = "SELECT name, weight, amount FROM doc_items WHERE doc_id = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, docId);
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

    private List<LocationDTO> findDestinationsByDocumentId(String docId) throws SQLException {
        List<LocationDTO> destinations = new ArrayList<>();
        String sql = "SELECT address FROM doc_locations WHERE doc_id = ?";

        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, docId);
            LocationDAO lDao = new LocationDAOImpl();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Optional<LocationDTO> loc = lDao.findByAddress(rs.getString("address"));
                    loc.ifPresent(destinations::add);
                }
            }
        }

        return destinations;
    }

    private void saveItems(String docId, List<ShipmentItemDTO> items) throws SQLException {
        String sql = "INSERT INTO doc_items (doc_id, name, weight, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            for (ShipmentItemDTO item : items) {
                ps.setString(1, docId);
                ps.setString(2, item.name());
                ps.setInt(3, item.weight());
                ps.setInt(4, item.amount());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveDestinations(String docId, List<LocationDTO> destinations) throws SQLException {
        String sql = "INSERT INTO doc_locations (doc_id, address) VALUES (?, ?)";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            for (LocationDTO loc : destinations) {
                ps.setString(1, docId);
                ps.setString(2, loc.address());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}