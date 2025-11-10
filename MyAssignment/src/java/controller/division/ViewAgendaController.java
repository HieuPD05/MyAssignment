package controller.division;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import dal.EmployeeDBContext;   // ← mượn connection từ lớp con của DBContext

public class ViewAgendaController extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String dcode = req.getParameter("div");           // IT/HR/MKT/EXE (optional)
        int page = 1;
        try {
            page = Math.max(1, Integer.parseInt(req.getParameter("page")));
        } catch (Exception ignore) {
        }

        LocalDate today = LocalDate.now();
        int offset = (page - 1) * PAGE_SIZE;

        String whereDiv = (dcode == null || dcode.isBlank()) ? "" : " AND d.dcode = ? ";

        String sqlCount
                = "SELECT COUNT(*) FROM Employee e JOIN Division d ON d.did=e.did WHERE 1=1 " + whereDiv;

        String sqlList
                = "SELECT e.eid,e.ename,e.position,e.employment_status,d.dcode, "
                + "       r.[status] AS leave_status "
                + "FROM Employee e JOIN Division d ON d.did=e.did "
                + "LEFT JOIN RequestForLeave r "
                + "  ON r.created_by=e.eid AND r.[from] <= ? AND r.[to] >= ? "
                + "WHERE 1=1 " + whereDiv + " "
                + // ← thêm filter phòng
                "ORDER BY e.ename OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        int total = 0;
        List<Map<String, Object>> rows = new ArrayList<>();

        // mượn connection từ một DBContext cụ thể
        try (Connection cn = new EmployeeDBContext().getConnection()) {

            // count
            try (PreparedStatement s = cn.prepareStatement(sqlCount)) {
                int i = 1;
                if (!whereDiv.isBlank()) {
                    s.setString(i++, dcode);
                }
                try (ResultSet rs = s.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt(1);
                    }
                }
            }

            // list
            try (PreparedStatement s = cn.prepareStatement(sqlList)) {
                int i = 1;
                s.setDate(i++, java.sql.Date.valueOf(today));
                s.setDate(i++, java.sql.Date.valueOf(today));

                if (!whereDiv.isBlank()) {
                    s.setString(i++, dcode);
                }
                s.setInt(i++, offset);
                s.setInt(i++, PAGE_SIZE);

                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("eid", rs.getInt("eid"));
                        m.put("ename", rs.getString("ename"));
                        m.put("position", rs.getString("position"));
                        m.put("status", rs.getString("employment_status"));
                        m.put("dcode", rs.getString("dcode"));
                        m.put("leaveStatus", rs.getObject("leave_status")); // null/0/1/2/3
                        rows.add(m);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // thống kê đơn giản: số người đang nghỉ (status 0 hoặc 1)
        long off = rows.stream().filter(x
                -> Objects.equals(x.get("leaveStatus"), 0) || Objects.equals(x.get("leaveStatus"), 1)
        ).count();

        req.setAttribute("stats_off", off);
        req.setAttribute("list", rows);
        req.setAttribute("page", page);
        req.setAttribute("pages", (int) Math.ceil(total / (double) PAGE_SIZE));
        req.setAttribute("div", dcode);
        req.getRequestDispatcher("/view/division/agenda.jsp").forward(req, resp);
    }
}
