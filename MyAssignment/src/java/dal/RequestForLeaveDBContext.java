package dal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import model.BaseModel;
import model.RequestForLeave;

public class RequestForLeaveDBContext extends DBContext {

    public int countByCreator(int createdBy) throws SQLException {
        String sql = "SELECT COUNT(*) FROM RequestForLeave WHERE created_by=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, createdBy);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public ArrayList<RequestForLeave> listByCreatorPaged(int createdBy, int offset, int fetch) throws SQLException {
        String sql = "SELECT rid, created_by, created_time, [from], [to], ltid, reason, [status], " +
                     "processed_by, processed_time, process_note " +
                     "FROM RequestForLeave WHERE created_by=? " +
                     "ORDER BY created_time DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, createdBy);
            st.setInt(2, offset);
            st.setInt(3, fetch);
            ArrayList<RequestForLeave> list = new ArrayList<>();
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        }
    }

    // Map kết quả sang model (tên setter phổ biến)
    private RequestForLeave map(ResultSet rs) throws SQLException {
        RequestForLeave r = new RequestForLeave();
        try { r.getClass().getMethod("setRid", int.class).invoke(r, rs.getInt("rid")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setCreatedBy", int.class).invoke(r, rs.getInt("created_by")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setCreatedTime", Timestamp.class).invoke(r, rs.getTimestamp("created_time")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setFrom", Date.class).invoke(r, rs.getDate("from")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setTo", Date.class).invoke(r, rs.getDate("to")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setLtid", int.class).invoke(r, rs.getInt("ltid")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setReason", String.class).invoke(r, rs.getString("reason")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setStatus", int.class).invoke(r, rs.getInt("status")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setProcessedBy", int.class).invoke(r, rs.getInt("processed_by")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setProcessedTime", Timestamp.class).invoke(r, rs.getTimestamp("processed_time")); } catch (Exception ignored) {}
        try { r.getClass().getMethod("setProcessNote", String.class).invoke(r, rs.getString("process_note")); } catch (Exception ignored) {}
        return r;
        }

    // ====== Quota phép năm (ANNUAL) ======
    public boolean hasAnnualQuota(int eid, double daysNeeded, int year) throws SQLException {
        String sql = "SELECT quota_days - used_days AS remain " +
                     "FROM LeaveQuota WHERE eid=? AND [year]=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, eid);
            st.setInt(2, year);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return false;
                return rs.getDouble("remain") >= daysNeeded - 1e-6;
            }
        }
    }

    public void consumeAnnualQuota(int eid, double days, int year) throws SQLException {
        String sql = "UPDATE LeaveQuota SET used_days = used_days + ? WHERE eid=? AND [year]=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDouble(1, days);
            st.setInt(2, eid);
            st.setInt(3, year);
            st.executeUpdate();
        }
    }

    public static double daysInclusive(LocalDate from, LocalDate to) {
        return (to.toEpochDay() - from.toEpochDay()) + 1;
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
