package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;
import model.RequestForLeave;

/**
 * Quản lý dữ liệu bảng RequestForLeave
 * Hỗ trợ lấy danh sách, thêm mới, duyệt (update status)
 * Tự động đóng connection sau mỗi thao tác.
 *
 * KHÔNG thay đổi schema: Loại đơn được nhúng trong reason dạng: [TYPE] body
 */
public class RequestForLeaveDBContext extends DBContext<RequestForLeave> {

    /** Lấy tất cả đơn nghỉ phép của nhân viên và cấp dưới của họ */
    public ArrayList<RequestForLeave> getByEmployeeAndSubodiaries(int eid) {
        ArrayList<RequestForLeave> rfls = new ArrayList<>();
        try {
            String sql = """
                WITH Org AS (
                  SELECT *, 0 as lvl FROM Employee e WHERE e.eid = ?
                  UNION ALL
                  SELECT c.*, o.lvl + 1 FROM Employee c JOIN Org o ON c.supervisorid = o.eid
                )
                SELECT r.rid, r.created_by, e.ename as created_name, r.created_time,
                       r.[from], r.[to], r.reason, r.status, r.processed_by, p.ename as processed_name
                FROM Org e
                INNER JOIN RequestForLeave r ON e.eid = r.created_by
                LEFT JOIN Employee p ON p.eid = r.processed_by
                ORDER BY r.created_time DESC
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                RequestForLeave rfl = new RequestForLeave();
                rfl.setId(rs.getInt("rid")); // quan trọng
                rfl.setCreated_time(rs.getTimestamp("created_time"));
                rfl.setFrom(rs.getDate("from"));
                rfl.setTo(rs.getDate("to"));
                rfl.setReason(rs.getString("reason"));
                rfl.setStatus(rs.getInt("status"));

                Employee created_by = new Employee();
                created_by.setId(rs.getInt("created_by"));
                created_by.setName(rs.getString("created_name"));
                rfl.setCreated_by(created_by);

                int processed_by_id = rs.getInt("processed_by");
                if (processed_by_id != 0) {
                    Employee processed_by = new Employee();
                    processed_by.setId(processed_by_id);
                    processed_by.setName(rs.getString("processed_name"));
                    rfl.setProcessed_by(processed_by);
                }
                rfls.add(rfl);
            }
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return rfls;
    }

    /** Thêm đơn nghỉ phép mới */
    @Override
    public void insert(RequestForLeave model) {
        try {
            String sql = """
                INSERT INTO [RequestForLeave] ([created_by], [created_time], [from], [to], [reason], [status])
                VALUES (?, GETDATE(), ?, ?, ?, 0)
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getCreated_by().getId());
            stm.setDate(2, model.getFrom());
            stm.setDate(3, model.getTo());
            stm.setNString(4, model.getReason());
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    /** Lấy thông tin chi tiết một đơn nghỉ phép */
    @Override
    public RequestForLeave get(int rid) {
        RequestForLeave r = null;
        try {
            String sql = """
                SELECT r.rid, r.[from], r.[to], r.[reason], r.[status],
                       e1.eid as created_id, e1.ename as created_name,
                       e2.eid as processed_id, e2.ename as processed_name
                FROM RequestForLeave r
                JOIN Employee e1 ON e1.eid = r.created_by
                LEFT JOIN Employee e2 ON e2.eid = r.processed_by
                WHERE r.rid = ?
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, rid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                r = new RequestForLeave();
                r.setId(rs.getInt("rid"));
                r.setFrom(rs.getDate("from"));
                r.setTo(rs.getDate("to"));
                r.setReason(rs.getString("reason"));
                r.setStatus(rs.getInt("status"));

                Employee cb = new Employee();
                cb.setId(rs.getInt("created_id"));
                cb.setName(rs.getString("created_name"));
                r.setCreated_by(cb);

                int pbId = rs.getInt("processed_id");
                if (pbId != 0) {
                    Employee pb = new Employee();
                    pb.setId(pbId);
                    pb.setName(rs.getString("processed_name"));
                    r.setProcessed_by(pb);
                }
            }
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return r;
    }

    /** Cập nhật trạng thái duyệt đơn */
    public void updateStatus(int rid, int status, int processedById) {
        try {
            String sql = """
                UPDATE RequestForLeave SET status = ?, processed_by = ? WHERE rid = ?
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, status);
            stm.setInt(2, processedById);
            stm.setInt(3, rid);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    /** Đếm theo status (giữ nguyên hành vi cũ) */
    public int countByStatus(int eid, int status) {
        int count = 0;
        try {
            String sql = """
                WITH Org AS (
                  SELECT *, 0 as lvl FROM Employee e WHERE e.eid = ?
                  UNION ALL
                  SELECT c.*, o.lvl + 1 FROM Employee c JOIN Org o ON c.supervisorid = o.eid
                )
                SELECT COUNT(*) AS total
                FROM Org o JOIN RequestForLeave r ON r.created_by = o.eid
                WHERE r.status = ?
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            stm.setInt(2, status);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) count = rs.getInt("total");
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    // ====== NEW: Sửa & Hủy khi pending và là chính chủ (KHÔNG đổi schema) ======

    /** Sửa đơn (owner + status=0): cập nhật from/to/reason */
    public int updateBasic(RequestForLeave model, int ownerEmployeeId) {
        int rows = 0;
        try {
            String sql = """
                UPDATE RequestForLeave
                   SET [from] = ?, [to] = ?, [reason] = ?
                 WHERE rid = ? AND created_by = ? AND status = 0
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setDate(1, model.getFrom());
            stm.setDate(2, model.getTo());
            stm.setNString(3, model.getReason());
            stm.setInt(4, model.getId());
            stm.setInt(5, ownerEmployeeId);
            rows = stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return rows;
    }

    /** Hủy đơn = xóa record (owner + status=0) */
    public int deleteIfOwnerAndPending(int rid, int ownerEmployeeId) {
        int rows = 0;
        try {
            String sql = """
                DELETE FROM RequestForLeave
                 WHERE rid = ? AND created_by = ? AND status = 0
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, rid);
            stm.setInt(2, ownerEmployeeId);
            rows = stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return rows;
    }

    // Mặc định
    @Override public ArrayList<RequestForLeave> list() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public void update(RequestForLeave model) { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public void delete(RequestForLeave model) { throw new UnsupportedOperationException("Not supported yet."); }
}
