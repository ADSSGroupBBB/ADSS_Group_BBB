package DataAccessDelivery.DAO;

public class DriverDAOImpl {
    /**
    @Override
    public Optional<DriverDTO> findById(int id) throws SQLException {
        String sql = "SELECT id, name, license_number FROM drivers WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DriverDTO driver = new DriverDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("license_number")
                    );
                    return Optional.of(driver);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<DriverDTO> findAll() throws SQLException {
        String sql = "SELECT id, name, license_number FROM drivers ORDER BY id";
        List<DriverDTO> drivers = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                drivers.add(new DriverDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("license_number")
                ));
            }
        }
        return drivers;
    }

    @Override
    public DriverDTO save(DriverDTO driver) throws SQLException {
        if (driver.id() == null) {
            String sql = "INSERT INTO drivers (name, license_number) VALUES (?, ?)";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, driver.name());
                ps.setString(2, driver.licenseNumber());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new DriverDTO(keys.getInt(1), driver.name(), driver.licenseNumber());
                    } else {
                        throw new SQLException("Creating driver failed, no ID obtained.");
                    }
                }
            }
        } else {
            String sql = "UPDATE drivers SET name = ?, license_number = ? WHERE id = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, driver.name());
                ps.setString(2, driver.licenseNumber());
                ps.setInt(3, driver.id());
                ps.executeUpdate();
                return driver;
            }
        }
    }
    */
}
