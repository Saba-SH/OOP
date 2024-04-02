import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class MetropolisModel extends AbstractTableModel {

    protected JTable dataTable;

    public MetropolisModel() {
        dataTable = new JTable(
                new DefaultTableModel(
                        new Object[][]{}, new Object[]{"Metropolis", "Continent", "Population"}
                ));
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    private static final String insertStatement = "INSERT INTO metropolises(metropolis, continent, population) " +
            "VALUES(?, ?, ?)";

    private static final String selectStatement = "SELECT * FROM metropolises WHERE ";

    /**
     * Adds data to the database and updates the table model.
     *
     * @param metropolis name of metropolis
     * @param continent name of continent of metropolis
     * @param populationStr population of the metropolis, in string form
     * */
    public ResultSet addData(String metropolis, String continent, String populationStr) {

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection conn = null;

        long population;
        if(populationStr.equals(""))
            population = 0;
        else
            population = Long.parseLong(populationStr);

        try {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/metropolises", "", "");
            } catch (SQLException e) {
                System.out.println("Error while trying to establish database connection");
                e.printStackTrace();
                return null;
            }

            try {
                pstmt = conn.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, metropolis);
                pstmt.setString(2, continent);
                pstmt.setLong(3, population);
            } catch (SQLException e) {
                System.out.println("Error while trying to create SQL statement");
                e.printStackTrace();
                return null;
            }

            int affectedRows = 0;
            try {
                affectedRows = pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error while trying to add data to base");
                e.printStackTrace();
                return null;
            }

            if (affectedRows > 0) {
                try {
                    rs = pstmt.getGeneratedKeys();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            updateTable(new Object[]{metropolis, continent, population});
            return rs;
        } finally {
//            try {rs.close();} catch (SQLException e) {}
//            try {pstmt.close();} catch (SQLException e) {}
//            try {conn.close();} catch (SQLException e) {}
        }
    }

    /**
     * Retrieves data from the database and updates the table model.
     *
     * @param metropolis name of metropolis
     * @param continent name of continent of metropolis
     * @param populationStr population of the metropolis, in string form
     * @param populationLarger true if we're looking for population larger than the given value,
     *                         false if looking for less than or equal to the given value
     * @param exactMatch true if we're looking for an exact match, false if we're looking for a partial match
     * */
    public ResultSet getData(String metropolis, String continent, String populationStr,
                             boolean populationLarger, boolean exactMatch) {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;

        long population;
        if(populationStr.equals(""))
            population = 0;
        else
            population = Long.parseLong(populationStr);

        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/metropolises", "", "");
            String statement = selectStatement;
            if(population != 0) {
                if (populationLarger) {
                    statement += " population > " + population + " AND ";
                } else {
                    statement += " population <= " + population + " AND ";
                }
            }
            if(!metropolis.equals("")) {
                if(exactMatch) {
                    statement += " metropolis LIKE \"" + metropolis + "\" AND ";
                } else {
                    statement += " metropolis LIKE " + "\"%" + metropolis + "%\"" + " AND ";
                }
            }
            if(!continent.equals("")) {
                if(exactMatch) {
                    statement += " continent LIKE \"" + continent + "\" AND ";
                } else {
                    statement += " continent LIKE " + "\"%" + continent + "%\"" + " AND ";
                }
            }

            if(statement.endsWith("AND ")) {
                statement = statement.substring(0, statement.length() - 4);
            }
            if(statement.endsWith("WHERE ")) {
                statement = statement.substring(0, statement.length() - 6);
            }

            statement += ";";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(statement);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
//            try {rs.close();} catch (SQLException e) {}
//            try {stmt.close();} catch (SQLException e) {}
//            try {conn.close();} catch (SQLException e) {}
        }

        updateTable(rs);
        return rs;
    }

    /**
     * Updates the table model based on given data.
     *
     * @param data result set of a query
     * */
    protected void updateTable(ResultSet data) {
        DefaultTableModel dtm = (DefaultTableModel) dataTable.getModel();
        dtm.setRowCount(0);

        while(true) {
            try {
                if (!data.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ((DefaultTableModel)dataTable.getModel()).addRow(
                        new Object[]{data.getString(1), data.getString(2), data.getString(3)});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the table model to display a single row.
     *
     * @param row the row to be displayed
     * */
    private void updateTable(Object[] row) {
        DefaultTableModel dtm = (DefaultTableModel) dataTable.getModel();
        dtm.setRowCount(0);
        dtm.addRow(row);
    }

    @Override
    public int getRowCount() {
        return dataTable.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return dataTable.getColumnCount();
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return dataTable.getValueAt(i, i1);
    }
}
