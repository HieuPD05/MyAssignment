package dal;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;
import model.BaseModel;
import model.PasswordResetToken;

public class PasswordResetTokenDBContext extends DBContext {

    // Tạo token, trả về chuỗi token (dễ dùng ngay cho in link)
    public String issueTokenReturnString(int uid, int expireMinutes) throws SQLException {
        String tokenStr = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime exp = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(expireMinutes);
        String sql = "INSERT INTO PasswordResetToken(uid, token, expire_at, used, created_at) " +
                     "VALUES(?, ?, ?, 0, SYSUTCDATETIME())";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, uid);
            st.setString(2, tokenStr);
            st.setTimestamp(3, Timestamp.valueOf(exp));
            st.executeUpdate();
        }
        return tokenStr;
    }

    // Nếu bạn muốn lấy hẳn đối tượng model
    public PasswordResetToken issueToken(int uid, int expireMinutes) throws SQLException {
        String token = issueTokenReturnString(uid, expireMinutes);
        PasswordResetToken t = new PasswordResetToken();
        try { t.getClass().getMethod("setUid", int.class).invoke(t, uid); } catch (Exception ignored) {}
        try { t.getClass().getMethod("setToken", String.class).invoke(t, token); } catch (Exception ignored) {}
        // expire_at lấy lại từ DB (để đồng bộ)
        String sql = "SELECT expire_at FROM PasswordResetToken WHERE token=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, token);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    try { t.getClass().getMethod("setExpireAt", Timestamp.class).invoke(t, rs.getTimestamp("expire_at")); } catch (Exception ignored) {}
                }
            }
        }
        try { t.getClass().getMethod("setUsed", boolean.class).invoke(t, false); } catch (Exception ignored) {}
        return t;
    }

    public Integer validateTokenAndGetUid(String token) throws SQLException {
        String sql = "SELECT uid FROM PasswordResetToken " +
                     "WHERE token=? AND used=0 AND expire_at > SYSUTCDATETIME()";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, token);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt("uid");
            }
        }
        return null;
    }

    public void markUsed(String token) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement(
                "UPDATE PasswordResetToken SET used=1 WHERE token=?")) {
            st.setString(1, token);
            st.executeUpdate();
        }
    }

    @Override
    public ArrayList list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BaseModel get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insert(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
