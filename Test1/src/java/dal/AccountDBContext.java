package dal;

import java.sql.*;
import model.Account;

public class AccountDBContext extends DBContext {

    public Account get(String username, String password) {
        try {
            String sql = "SELECT username, password FROM Account "
                       + "WHERE username = ? AND password = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Account(rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
