package dal;

import model.BaseModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sonnt
 */
public abstract class DBContext<T extends BaseModel> {
    //open connection
    protected Connection connection = null;

    public DBContext() {
        try {
            String user = "HieuPD";
            String pass = "11111111";
            String url = "jdbc:sqlserver://localhost:1433;databaseName= MyAssignment;encrypt=true;trustServerCertificate=true;";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract ArrayList<T> list();
    public abstract T get(int id);
    public abstract void insert(T model);
    public abstract void update(T model);
    public abstract void delete(T model);
}
