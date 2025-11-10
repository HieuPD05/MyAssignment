package dal;

import java.sql.*;
import java.util.ArrayList;
import model.BaseModel;
import model.iam.User;

public class AdminAccountDBContext extends DBContext {

    // ===== Nghiệp vụ riêng (Admin quản lý tài khoản) =====
    public ArrayList<User> listUsers(int offset, int fetch) throws SQLException {
        String sql = "SELECT uid, username, passwordhash, displayname FROM [User] " +
                     "ORDER BY uid OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, offset);
            st.setInt(2, fetch);
            ArrayList<User> list = new ArrayList<>();
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setUid(rs.getInt("uid"));
                    u.setUsername(rs.getString("username"));
                    // DB: passwordhash -> model: password
                    u.setPassword(rs.getString("passwordhash"));
                    u.setDisplayname(rs.getString("displayname"));
                    list.add(u);
                }
            }
            return list;
        }
    }

    public int countUsers() throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM [User]");
             ResultSet rs = st.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Integer findUidByUsername(String username) throws SQLException {
        String sql = "SELECT uid FROM [User] WHERE username=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt("uid");
            }
        }
        return null;
    }

    public int createUser(String username, String passwordHash, String displayName) throws SQLException {
        String sql = "INSERT INTO [User](username, passwordhash, displayname) VALUES(?,?,?)";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, username);
            st.setString(2, passwordHash);
            st.setString(3, displayName);
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public void deleteUser(int uid) throws SQLException {
        try (PreparedStatement st1 = connection.prepareStatement("DELETE FROM UserRole WHERE uid=?");
             PreparedStatement st2 = connection.prepareStatement("DELETE FROM Enrollment WHERE uid=?");
             PreparedStatement st3 = connection.prepareStatement("DELETE FROM PasswordResetToken WHERE uid=?");
             PreparedStatement st4 = connection.prepareStatement("DELETE FROM [User] WHERE uid=?")) {
            st1.setInt(1, uid); st1.executeUpdate();
            st2.setInt(1, uid); st2.executeUpdate();
            st3.setInt(1, uid); st3.executeUpdate();
            st4.setInt(1, uid); st4.executeUpdate();
        }
    }

    public void updatePasswordHash(int uid, String newHash) throws SQLException {
        String sql = "UPDATE [User] SET passwordhash=? WHERE uid=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, newHash);
            st.setInt(2, uid);
            st.executeUpdate();
        }
    }

    public Integer findRoleIdByName(String rname) throws SQLException {
        String sql = "SELECT rid FROM [Role] WHERE rname=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, rname);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt("rid");
            }
        }
        return null;
    }

    public void addUserRole(int uid, int rid) throws SQLException {
        String sql = "IF NOT EXISTS(SELECT 1 FROM UserRole WHERE uid=? AND rid=?) " +
                     "INSERT INTO UserRole(uid,rid) VALUES(?,?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, uid); st.setInt(2, rid);
            st.setInt(3, uid); st.setInt(4, rid);
            st.executeUpdate();
        }
    }

    public void addEnrollment(int uid, int eid) throws SQLException {
        String sql = "IF NOT EXISTS(SELECT 1 FROM Enrollment WHERE uid=? AND eid=?) " +
                     "INSERT INTO Enrollment(uid,eid,active) VALUES(?,?,1)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, uid); st.setInt(2, eid);
            st.setInt(3, uid); st.setInt(4, eid);
            st.executeUpdate();
        }
    }

    // ===== Override abstract của DBContext =====
    @Override public ArrayList list() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public BaseModel get(int id) { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public void insert(BaseModel model) { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public void update(BaseModel model) { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public void delete(BaseModel model) { throw new UnsupportedOperationException("Not supported yet."); }
}
