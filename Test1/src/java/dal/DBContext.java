package dal;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    protected Connection connection;

    public DBContext() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;"
                       + "databaseName=Test2;"           // 👈 thay bằng tên DB của bạn
                       + "encrypt=true;trustServerCertificate=true;";
            String user = "sa";                           // 👈 user SQL Server
            String pass = "123456";                       // 👈 mật khẩu SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected to database successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
