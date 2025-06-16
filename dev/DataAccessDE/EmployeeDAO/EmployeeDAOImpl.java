package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.EmployeeDAO;
import DataAccessDE.EmployeeInterface.QualificationDAO;
import util.Database_HR_DL;
import dto.EmployeeDTO;
import Domain_employee.Employee.UserRole;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl implements EmployeeDAO {
    private final QualificationDAO qualificationDAO;

    public EmployeeDAOImpl() {
        this.qualificationDAO = new QualificationDAOImpl();
    }

    @Override
    public Optional<EmployeeDTO> findById(String id) throws SQLException {
        String sql = """
            SELECT id, first_name, last_name, bank_account, start_date, salary, 
                   role, sick_days, vacation_days, pension_fund_name, branch_address
            FROM employees WHERE id = ?
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Get employee qualifications
                    List<String> qualifications = qualificationDAO.getEmployeeQualifications(id);

                    EmployeeDTO employee = new EmployeeDTO(
                            rs.getString("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("bank_account"),
                            LocalDate.parse(rs.getString("start_date")),
                            rs.getDouble("salary"),
                            qualifications,
                            UserRole.valueOf(rs.getString("role")),
                            rs.getInt("sick_days"),
                            rs.getInt("vacation_days"),
                            rs.getString("pension_fund_name"),
                            rs.getString("branch_address")
                    );
                    return Optional.of(employee);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<EmployeeDTO> findAll() throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = """
            SELECT id, first_name, last_name, bank_account, start_date, salary, 
                   role, sick_days, vacation_days, pension_fund_name, branch_address
            FROM employees ORDER BY id
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String empId = rs.getString("id");
                List<String> qualifications = qualificationDAO.getEmployeeQualifications(empId);

                EmployeeDTO employee = new EmployeeDTO(
                        empId,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("bank_account"),
                        LocalDate.parse(rs.getString("start_date")),
                        rs.getDouble("salary"),
                        qualifications,
                        UserRole.valueOf(rs.getString("role")),
                        rs.getInt("sick_days"),
                        rs.getInt("vacation_days"),
                        rs.getString("pension_fund_name"),
                        rs.getString("branch_address")
                );
                employees.add(employee);
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeDTO> findByBranch(String branchAddress) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = """
            SELECT id, first_name, last_name, bank_account, start_date, salary, 
                   role, sick_days, vacation_days, pension_fund_name, branch_address
            FROM employees WHERE branch_address = ? ORDER BY id
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, branchAddress);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String empId = rs.getString("id");
                    List<String> qualifications = qualificationDAO.getEmployeeQualifications(empId);

                    EmployeeDTO employee = new EmployeeDTO(
                            empId,
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("bank_account"),
                            LocalDate.parse(rs.getString("start_date")),
                            rs.getDouble("salary"),
                            qualifications,
                            UserRole.valueOf(rs.getString("role")),
                            rs.getInt("sick_days"),
                            rs.getInt("vacation_days"),
                            rs.getString("pension_fund_name"),
                            rs.getString("branch_address")
                    );
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeDTO> findByRole(String role) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = """
            SELECT id, first_name, last_name, bank_account, start_date, salary, 
                   role, sick_days, vacation_days, pension_fund_name, branch_address
            FROM employees WHERE role = ? ORDER BY id
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String empId = rs.getString("id");
                    List<String> qualifications = qualificationDAO.getEmployeeQualifications(empId);

                    EmployeeDTO employee = new EmployeeDTO(
                            empId,
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("bank_account"),
                            LocalDate.parse(rs.getString("start_date")),
                            rs.getDouble("salary"),
                            qualifications,
                            UserRole.valueOf(rs.getString("role")),
                            rs.getInt("sick_days"),
                            rs.getInt("vacation_days"),
                            rs.getString("pension_fund_name"),
                            rs.getString("branch_address")
                    );
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employee) throws SQLException {
        String sql = """
            INSERT INTO employees (id, first_name, last_name, bank_account, start_date,
                                 salary, role, password, sick_days, vacation_days,
                                 pension_fund_name, branch_address)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getId());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getBankAccount());
            ps.setString(5, employee.getStartDate().toString());
            ps.setDouble(6, employee.getSalary());
            ps.setString(7, employee.getRole().toString());
            ps.setString(8, ""); // Default empty password for regular employees
            ps.setInt(9, employee.getSickDays());
            ps.setInt(10, employee.getVacationDays());
            ps.setString(11, employee.getPensionFundName());
            ps.setString(12, employee.getBranchAddress());

            ps.executeUpdate();
        }

        return employee;
    }

    @Override
    public boolean updateEmployee(EmployeeDTO employee) throws SQLException {
        String sql = """
            UPDATE employees SET
                first_name = ?, last_name = ?, bank_account = ?, salary = ?,
                role = ?, sick_days = ?, vacation_days = ?, pension_fund_name = ?,
                branch_address = ?
            WHERE id = ?
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getBankAccount());
            ps.setDouble(4, employee.getSalary());
            ps.setString(5, employee.getRole().toString());
            ps.setInt(6, employee.getSickDays());
            ps.setInt(7, employee.getVacationDays());
            ps.setString(8, employee.getPensionFundName());
            ps.setString(9, employee.getBranchAddress());
            ps.setString(10, employee.getId());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public boolean deleteById(String id) throws SQLException {
        // First check if employee is assigned to future shifts
        String checkShifts = """
            SELECT COUNT(*) FROM shift_assignments sa
            JOIN shifts s ON sa.shift_id = s.id
            WHERE sa.employee_id = ? AND s.date > date('now')
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkShifts)) {

            checkPs.setString(1, id);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Cannot delete employee assigned to future shifts
                }
            }
        }

        // Delete employee (qualifications and availability will be deleted by CASCADE)
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}