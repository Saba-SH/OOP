package com.example.part2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLProductDAO implements ProductDAO {

    @Override
    public List<Product> list() {
        List<Product> result = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/products", "", "");
            String query = "SELECT * FROM products;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next()) {
                Product product = new Product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
                result.add(product);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
//            try {rs.close();} catch (SQLException e) {throw new RuntimeException(e);}
//            try {stmt.close();} catch (SQLException e) {throw new RuntimeException(e);}
//            try {conn.close();} catch (SQLException e) {throw new RuntimeException(e);}
            return result;
        }
    }

    @Override
    public Product getProductById(String id) {
        Product result = null;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/products", "", "");
            String query = "SELECT * FROM products WHERE productid=\"" + id + "\";";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            result = new Product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
//            try {rs.close();} catch (SQLException e) {throw new RuntimeException(e);}
//            try {stmt.close();} catch (SQLException e) {throw new RuntimeException(e);}
//            try {conn.close();} catch (SQLException e) {throw new RuntimeException(e);}
            return result;
        }
    }
}
