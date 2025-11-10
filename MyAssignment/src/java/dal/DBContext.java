package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection connection;

    // ==== ĐỔI 3 GIÁ TRỊ NÀY NẾU BẠN MUỐN HARD-CODE ====
    // Mặc định: host=localhost, port=1433, db=HP_CT, user=sa, pass=123456
    // Bạn có thể thay trực tiếp ở đây hoặc dùng biến môi trường / system properties (xem dưới)
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "1433";
    private static final String DEFAULT_DB   = "HP_CT";        // đổi thành FALL25_Assignment nếu bạn đang dùng DB đó
    private static final String DEFAULT_USER = "HieuPD";
    private static final String DEFAULT_PASS = "11111111";
    // ===================================================

    public DBContext() {
        try {
            connect();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Cannot connect to SQL Server: " + e.getMessage(), e);
        }
    }

    private void connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Cho phép override bằng JVM -D hoặc ENV
        String host = getCfg("MSSQL_HOST", DEFAULT_HOST);
        String port = getCfg("MSSQL_PORT", DEFAULT_PORT);
        String db   = getCfg("MSSQL_DB",   DEFAULT_DB);
        String user = getCfg("MSSQL_USER", DEFAULT_USER);
        String pass = getCfg("MSSQL_PASS", DEFAULT_PASS);

        // Nếu bạn dùng SQL Server local mặc định, encrypt=false + trustServerCertificate=true giúp chạy nhanh trong dev.
        String url = "jdbc:sqlserver://" + host + ":" + port
                + ";databaseName=" + db
                + ";encrypt=false;trustServerCertificate=true";

        connection = DriverManager.getConnection(url, user, pass);
    }

    private String getCfg(String key, String fallback) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp;
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;
        return fallback;
    }

    public Connection getConnection() { return connection; }

    public void close() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
        }
    }
}
