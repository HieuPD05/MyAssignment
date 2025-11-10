package dal;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;
import model.PasswordResetToken;
import model.iam.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserDBContext extends DBContext<User> {

    @Override public ArrayList<User> list() { return new ArrayList<>(); }
    @Override public User get(int id) { return null; }
    @Override public void insert(User m) { throw new UnsupportedOperationException("Not used"); }
    @Override public void update(User m) { throw new UnsupportedOperationException("Not used"); }
    @Override public void delete(User m) { throw new UnsupportedOperationException("Not used"); }

    public User getByUsername(String username) {
        final String sql = "SELECT uid, username, passwordhash, displayname FROM [User] WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUid(rs.getInt("uid"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("passwordhash"));
                    u.setDisplayname(rs.getString("displayname"));
                    return u;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean verifyAndAutoHash(User dbUser, String rawPassword) {
        if (dbUser == null) return false;
        String db = dbUser.getPassword();
        if (db == null) return false;
        if (db.startsWith("$2")) return BCrypt.checkpw(rawPassword, db);
        if (rawPassword.equals(db)) {
            String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
            updatePasswordHash(dbUser.getUid(), hash);
            dbUser.setPassword(hash);
            return true;
        }
        return false;
    }

    public void updatePasswordHash(int uid, String bcryptHash) {
        final String sql = "UPDATE [User] SET passwordhash=? WHERE uid=?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, bcryptHash);
            stm.setInt(2, uid);
            stm.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public int createUser(String username, String display, String rawPass) throws SQLException {
        final String sql = "INSERT INTO [User](username,passwordhash,displayname) VALUES(?,?,?)";
        String hash = BCrypt.hashpw(rawPass, BCrypt.gensalt(10));
        try (PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, username);
            stm.setString(2, hash);
            stm.setString(3, display);
            stm.executeUpdate();
            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public PasswordResetToken createResetToken(int uid, int minutes) throws SQLException {
        String tokenStr = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expire = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(minutes <= 0 ? 30 : minutes);
        String sql = "INSERT INTO PasswordResetToken(uid, token, expire_at, used) VALUES (?,?,?,0)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, uid);
            stm.setString(2, tokenStr);
            stm.setTimestamp(3, Timestamp.valueOf(expire));
            stm.executeUpdate();
            PasswordResetToken t = new PasswordResetToken();
            t.setUid(uid); t.setToken(tokenStr); t.setExpireAt(expire); t.setUsed(false);
            return t;
        }
    }

    public boolean consumeTokenAndReset(String token, String newRaw) throws SQLException {
        connection.setAutoCommit(false);
        try {
            Integer uid = null;
            String q = "SELECT token_id, uid, expire_at, used FROM PasswordResetToken WHERE token=? FOR UPDATE";
            try (PreparedStatement s1 = connection.prepareStatement(q)) {
                s1.setString(1, token);
                try (ResultSet rs = s1.executeQuery()) {
                    if (!rs.next()) { connection.rollback(); return false; }
                    boolean used = rs.getBoolean("used");
                    Timestamp exp = rs.getTimestamp("expire_at");
                    if (used || exp.toInstant().isBefore(java.time.Instant.now())) { connection.rollback(); return false; }
                    uid = rs.getInt("uid");
                }
            }
            try (PreparedStatement s2 = connection.prepareStatement("UPDATE PasswordResetToken SET used=1 WHERE token=?")) {
                s2.setString(1, token); s2.executeUpdate();
            }
            String hash = BCrypt.hashpw(newRaw, BCrypt.gensalt(10));
            updatePasswordHash(uid, hash);
            connection.commit();
            return true;
        } catch (Exception ex) {
            connection.rollback(); throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    // === ADMIN: Liệt kê tất cả users (đơn giản) ===
public java.util.List<User> listAll() throws SQLException {
    java.util.List<User> list = new java.util.ArrayList<>();
    String sql = "SELECT uid, username, passwordhash, displayname FROM [User] ORDER BY uid";
    try (PreparedStatement st = connection.prepareStatement(sql);
         ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
            User u = new User();
            u.setUid(rs.getInt("uid"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("passwordhash"));
            u.setDisplayname(rs.getString("displayname"));
            list.add(u);
        }
    }
    return list;
}

// === ADMIN: Gán role theo tên rname ===
public void assignRole(int uid, String roleName) throws SQLException {
    String sql = "INSERT INTO UserRole(uid, rid) " +
                 "SELECT ?, rid FROM [Role] WHERE rname = ?";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setInt(1, uid);
        st.setString(2, roleName);
        st.executeUpdate();
    }
}

// === ADMIN: Enroll user ↔ employee ===
public void enroll(int uid, int eid) throws SQLException {
    String sql = "INSERT INTO Enrollment(uid, eid, active) VALUES(?,?,1)";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setInt(1, uid);
        st.setInt(2, eid);
        st.executeUpdate();
    }
}

// === ADMIN: Xóa user kèm dữ liệu phụ thuộc (demo) ===
public void deleteUserCascade(int uid) throws SQLException {
    connection.setAutoCommit(false);
    try {
        try (PreparedStatement s = connection.prepareStatement(
                "DELETE FROM PasswordResetToken WHERE uid=?")) {
            s.setInt(1, uid); s.executeUpdate();
        }
        try (PreparedStatement s = connection.prepareStatement(
                "DELETE FROM Notification WHERE to_uid=?")) {
            s.setInt(1, uid); s.executeUpdate();
        }
        try (PreparedStatement s = connection.prepareStatement(
                "DELETE FROM UserRole WHERE uid=?")) {
            s.setInt(1, uid); s.executeUpdate();
        }
        try (PreparedStatement s = connection.prepareStatement(
                "DELETE FROM Enrollment WHERE uid=?")) {
            s.setInt(1, uid); s.executeUpdate();
        }
        try (PreparedStatement s = connection.prepareStatement(
                "DELETE FROM [User] WHERE uid=?")) {
            s.setInt(1, uid); s.executeUpdate();
        }
        connection.commit();
    } catch (Exception ex) {
        connection.rollback(); throw ex;
    } finally {
        connection.setAutoCommit(true);
    }
}
public Integer getUidByEmployee(int eid) throws SQLException {
    String sql="SELECT TOP 1 uid FROM Enrollment WHERE eid=? AND active=1";
    try (PreparedStatement s=connection.prepareStatement(sql)){ s.setInt(1,eid);
        try(ResultSet rs=s.executeQuery()){ return rs.next()? rs.getInt(1): null; } }
}
public Integer getEidByUid(int uid) throws SQLException {
    String sql="SELECT TOP 1 eid FROM Enrollment WHERE uid=? AND active=1";
    try (PreparedStatement s=connection.prepareStatement(sql)){ s.setInt(1,uid);
        try(ResultSet rs=s.executeQuery()){ return rs.next()? rs.getInt(1): null; } }
}


}
