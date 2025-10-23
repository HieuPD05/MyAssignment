package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class UserDBContext extends DBContext<User> {

    public User get(String username, String password) {
        try {
            String sql = "SELECT username, [password] FROM Account WHERE username = ? AND [password] = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                return u;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return null;
    }

    @Override
    public ArrayList<User> list() { return null; }
    @Override
    public User get(int id) { return null; }
    @Override
    public void insert(User model) {}
    @Override
    public void update(User model) {}
    @Override
    public void delete(User model) {}
}
