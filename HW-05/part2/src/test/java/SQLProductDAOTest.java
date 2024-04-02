import com.example.part2.Product;
import com.example.part2.SQLProductDAO;
import junit.framework.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLProductDAOTest extends TestCase {
    public void testList() {
        List<Product> products = new SQLProductDAO().list();
        System.out.println("Product count:" + products.size());
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/products", "", "");
            String query = "SELECT * FROM products;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            List<Product> products1 = new ArrayList<>();
            while(rs.next()) {
                Product product = new Product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
                products1.add(product);
            }
            assertEquals(products.size(), products1.size());
            for(int i = 0; i < products.size(); i++) {
                assertEquals(products.get(i).getId(), products1.get(i).getId());
                assertEquals(products.get(i).getName(), products1.get(i).getName());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void testProductById() {
        List<Product> products = new SQLProductDAO().list();

        SQLProductDAO dao = new SQLProductDAO();

        try{
            for (Product p : products) {
                String id = p.getId();

                Product product = dao.getProductById(id);

                assertEquals(p.getName(), product.getName());
                assertEquals(p.getId(), product.getId());
                assertEquals(p.getImagefile(), product.getImagefile());
                assertEquals(p.getPrice(), product.getPrice());
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
