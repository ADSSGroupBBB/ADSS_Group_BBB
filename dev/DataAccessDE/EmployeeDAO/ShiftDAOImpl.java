package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.ShiftAssignmentDAO;
import DataAccessDE.EmployeeInterface.ShiftDAO;
import util.Database_HR_DL;
import dto.ShiftDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShiftDAOImpl implements ShiftDAO {
    private final ShiftAssignmentDAO assignmentDAO;

    public ShiftDAOImpl() {
        this.assignmentDAO = new ShiftAssignmentDAOImpl();
    }

    @Override
    public Optional<ShiftDTO> findById(String id) throws SQLException {
        String sql = """
            SELECT id, date, shift_type, start_time, end_time, branch_address
            FROM shifts WHERE id = ?
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Get assignments for this shift
                    var assignments = assignmentDAO.getShiftAssignments(id);
                    String managerId = assignmentDAO.getShiftManager(id);
                    String managerName = getEmployeeName(managerId);

                    ShiftDTO shift = new ShiftDTO(
                            rs.getString("id"),
                            LocalDate.parse(rs.getString("date")),
                            rs.getString("shift_type"),
                            LocalTime.parse(rs.getString("start_time")),
                            LocalTime.parse(rs.getString("end_time")),
                            managerId,
                            managerName,
                            assignments,
                            rs.getString("branch_address")
                    );
                    return Optional.of(shift);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ShiftDTO> findAll() throws SQLException {
        List<ShiftDTO> shifts = new ArrayList<>();
        String sql = "SELECT id FROM shifts ORDER BY date, shift_type";

        try (Connection conn = Database_HR_DL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String shiftId = rs.getString("id");
                findById(shiftId).ifPresent(shifts::add);
            }
        }
        return shifts;
    }

    @Override
    public List<ShiftDTO> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<ShiftDTO> shifts = new ArrayList<>();
        String sql = "SELECT id FROM shifts WHERE date >= ? AND date <= ? ORDER BY date, shift_type";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, startDate.toString());
            ps.setString(2, endDate.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String shiftId = rs.getString("id");
                    findById(shiftId).ifPresent(shifts::add);
                }
            }
        }
        return shifts;
    }

    @Override
    public List<ShiftDTO> findByBranch(String branchAddress) throws SQLException {
        List<ShiftDTO> shifts = new ArrayList<>();
        String sql = "SELECT id FROM shifts WHERE branch_address = ? ORDER BY date, shift_type";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, branchAddress);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String shiftId = rs.getString("id");
                    findById(shiftId).ifPresent(shifts::add);
                }
            }
        }
        return shifts;
    }

    @Override
    public List<ShiftDTO> findFutureShifts() throws SQLException {
        LocalDate today = LocalDate.now();
        String sql = "SELECT id FROM shifts WHERE date >= ? ORDER BY date, shift_type";

        List<ShiftDTO> shifts = new ArrayList<>();
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, today.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String shiftId = rs.getString("id");
                    findById(shiftId).ifPresent(shifts::add);
                }
            }
        }
        return shifts;
    }

    @Override
    public List<ShiftDTO> findHistoricalShifts() throws SQLException {
        LocalDate today = LocalDate.now();
        String sql = "SELECT id FROM shifts WHERE date < ? ORDER BY date DESC, shift_type";

        List<ShiftDTO> shifts = new ArrayList<>();
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, today.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String shiftId = rs.getString("id");
                    findById(shiftId).ifPresent(shifts::add);
                }
            }
        }
        return shifts;
    }

    @Override
    public List<ShiftDTO> findEmployeeShifts(String employeeId) throws SQLException {
        List<ShiftDTO> shifts = new ArrayList<>();
        String sql = """
            SELECT DISTINCT s.id 
            FROM shifts s
            JOIN shift_assignments sa ON s.id = sa.shift_id
            WHERE sa.employee_id = ?
            ORDER BY s.date DESC, s.shift_type
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String shiftId = rs.getString("id");
                    findById(shiftId).ifPresent(shifts::add);
                }
            }
        }
        return shifts;
    }

    @Override
    public ShiftDTO save(ShiftDTO shift) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO shifts (id, date, shift_type, start_time, end_time, branch_address)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shift.getId());
            ps.setString(2, shift.getDate().toString());
            ps.setString(3, shift.getShiftType());
            ps.setString(4, shift.getStartTime().toString());
            ps.setString(5, shift.getEndTime().toString());
            ps.setString(6, shift.getBranchAddress());

            ps.executeUpdate();
        }

        return shift;
    }

    @Override
    public boolean deleteById(String id) throws SQLException {
        // Check if shift has assignments
        var assignments = assignmentDAO.getShiftAssignments(id);
        if (!assignments.isEmpty()) {
            return false; // Cannot delete shift with assignments
        }

        String sql = "DELETE FROM shifts WHERE id = ?";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    private String getEmployeeName(String employeeId) throws SQLException {
        if (employeeId == null) return null;

        String sql = "SELECT first_name, last_name FROM employees WHERE id = ?";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("first_name") + " " + rs.getString("last_name");
                }
            }
        }
        return null;
    }
}