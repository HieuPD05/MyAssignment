package dal;

import java.util.ArrayList;
import model.iam.User;
import model.Employee;
import model.Department;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Đăng nhập: lấy User + Employee + Division từ Enrollment (active=1) */
public class UserDBContext extends DBContext<User> {

    public User get(String username, String password) {
        try {
            String sql = """
            SELECT u.uid, u.username, u.displayname,
                   e.eid, e.ename,
                   d.did, d.dname
            FROM [User] u
            INNER JOIN [Enrollment] en ON u.[uid] = en.[uid]
            INNER JOIN [Employee] e ON e.eid = en.eid
            INNER JOIN [Division] d ON d.did = e.did
            WHERE u.username = ? AND u.[password] = ? AND en.active = 1
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("uid"));
                u.setUsername(rs.getString("username"));
                u.setDisplayname(rs.getString("displayname"));

                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                e.setDept(d);

                u.setEmployee(e);
                rs.close(); stm.close();
                return u;
            }
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return null;
    }

    @Override public ArrayList<User> list() { throw new UnsupportedOperationException("Not supported."); }
    @Override public User get(int id)        { throw new UnsupportedOperationException("Not supported."); }
    @Override public void insert(User m)     { throw new UnsupportedOperationException("Not supported."); }
    @Override public void update(User m)     { throw new UnsupportedOperationException("Not supported."); }
    @Override public void delete(User m)     { throw new UnsupportedOperationException("Not supported."); }
}
