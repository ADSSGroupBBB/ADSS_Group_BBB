package src.DataAccess.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import src.util.Database;
import src.DTO.TruckDTO;
import src.DataAccess.Interface.TruckDAO;

public class TruckDAOImpl implements TruckDAO {

    @Override
    public Optional<TruckDTO> findById(String id) throws SQLException {
        String sql = "SELECT truck_id, type, truck_weight, max_weight, curr_weight, on_drive FROM trucks WHERE truck_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TruckDTO truck = new TruckDTO(
                            rs.getString("truck_id"),
                            rs.getInt("type"),
                            rs.getInt("truck_weight"),
                            rs.getInt("max_weight"),
                            rs.getInt("curr_weight"),
                            rs.getInt("on_drive") == 1
                    );
                    return Optional.of(truck);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<TruckDTO> findAll() throws SQLException {
        String sql = "SELECT truck_id, type, truck_weight, max_weight, curr_weight, on_drive FROM trucks ORDER BY truck_id";
        List<TruckDTO> trucks = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                trucks.add(new TruckDTO(
                        rs.getString("truck_id"),
                        rs.getInt("type"),
                        rs.getInt("truck_weight"),
                        rs.getInt("max_weight"),
                        rs.getInt("curr_weight"),
                        rs.getInt("on_drive") == 1
                ));
            }
        }
        return trucks;
    }

    @Override
    public TruckDTO save(TruckDTO truck) throws SQLException {
        if (findById(truck.truck_id()).isEmpty()) {
            String sql = "INSERT INTO trucks (truck_id, type, truck_weight, curr_weight, max_weight, on_drive) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, truck.truck_id());
                ps.setInt(2, truck.type());
                ps.setInt(3, truck.truck_weight());
                ps.setInt(4, truck.currWeight());
                ps.setInt(5, truck.max_weight());
                ps.setInt(6, truck.onDrive() ? 1 : 0);
                ps.executeUpdate();
                return truck;
            }
        } else {
            String sql = "UPDATE trucks SET type = ?, truck_weight = ?, curr_weight = ?, max_weight = ?, on_drive = ? WHERE truck_id = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, truck.type());
                ps.setInt(2, truck.truck_weight());
                ps.setInt(3, truck.currWeight());
                ps.setInt(4, truck.max_weight());
                ps.setInt(5, truck.onDrive() ? 1 : 0);
                ps.setString(6, truck.truck_id());
                ps.executeUpdate();
                return truck;
            }
        }
    }

    public boolean deleteById(String truckId) throws SQLException {
        String sql = "DELETE FROM trucks WHERE truck_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, truckId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}