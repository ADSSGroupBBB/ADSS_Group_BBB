package dataAccess;

import dto.ProductDto;
import dto.SupplierDto;
import util.Database;
import util.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

public class JdbcProductDao implements ProductDao{
    @Override
    public Optional<ProductDto> findProById(int num) throws SQLException {
        String sql= "SELECT * FROM products WHERE productNumber = ?";
        try (PreparedStatement psPro= DatabaseManager.getConnection().prepareStatement(sql)){
            psPro.setInt(1,num);
            try (ResultSet pro=psPro.executeQuery()){
                if(pro.next()){
                    String productName=pro.getString("productName");
                    int productNumber=pro.getInt("productNumber");
                    String unitOfMeasure=pro.getString("unitOfMeasure");
                    String manufacturer=pro.getString("manufacturer");
                    return Optional.of(new ProductDto(productName,productNumber,unitOfMeasure,manufacturer)) ;
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public ProductDto savePro(ProductDto pro) throws SQLException {
        String sql = """
                INSERT INTO products(productName, productNumber, unitOfMeasure, manufacturer) VALUES (?,?,?,?)
                """;
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, pro.productName());
            ps.setInt(2, pro.productNumber());
            ps.setString(3, pro.unitOfMeasure());
            ps.setString(4, pro.manufacturer());
            ps.executeUpdate();
            return pro;
        }
    }

    @Override
    public void updateNameProById(int productNumber, String productName) throws SQLException{
        String sql = "UPDATE products SET productName = ? WHERE productNumber = ?";
        try (PreparedStatement ps = (DatabaseManager.getConnection().prepareStatement(sql))) {
            ps.setString(1, productName);
            ps.setInt(2, productNumber);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateUnitOfMeasureById(int productNumber, String unitOfMeasure) throws SQLException {
        String sql = "UPDATE products SET unitOfMeasure = ? WHERE productNumber = ?";
        try (PreparedStatement ps = (DatabaseManager.getConnection().prepareStatement(sql))) {
            ps.setString(1, unitOfMeasure);
            ps.setInt(2, productNumber);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateManufacturerById(int productNumber, String manufacturer) throws SQLException {
        String sql = "UPDATE products SET manufacturer = ? WHERE productNumber = ?";
        try (PreparedStatement ps = (DatabaseManager.getConnection().prepareStatement(sql))) {
            ps.setString(1, manufacturer);
            ps.setInt(2, productNumber);
            ps.executeUpdate();
        }
    }
    public LinkedList<ProductDto> findAll() throws SQLException{
        LinkedList<ProductDto> products = new LinkedList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)){
             try   (ResultSet rs = ps.executeQuery()){
                 while (rs.next()) {
                     String productName = rs.getString("productName");
                     int productNumber = rs.getInt("productNumber");
                     String unitOfMeasure = rs.getString("unitOfMeasure");
                     String manufacturer = rs.getString("manufacturer");

                     ProductDto product = new ProductDto(productName, productNumber, unitOfMeasure, manufacturer);
                     products.add(product);
                 }
             }
        }
        return products;
    }
}
