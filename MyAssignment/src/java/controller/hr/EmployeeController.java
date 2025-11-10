package controller.hr;

import dal.AccountRequestDBContext;
import dal.EmployeeDBContext;           // <-- dùng lớp con để lấy connection
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class EmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sql = "SELECT e.eid,e.ename,e.position,e.employment_status,d.dcode " +
                     "FROM Employee e JOIN Division d ON d.did=e.did " +
                     "ORDER BY d.dcode,e.ename";

        List<Map<String,Object>> rows = new ArrayList<>();
        try (Connection cn = new EmployeeDBContext().getConnection();      // <-- sửa ở đây
             PreparedStatement s = cn.prepareStatement(sql);
             ResultSet rs = s.executeQuery()) {

            while (rs.next()) {
                Map<String,Object> m = new HashMap<>();
                m.put("eid", rs.getInt("eid"));
                m.put("ename", rs.getString("ename"));
                m.put("position", rs.getString("position"));
                m.put("status", rs.getString("employment_status"));
                m.put("div", rs.getString("dcode"));
                rows.add(m);
            }
            req.setAttribute("emps", rows);
        } catch (SQLException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/view/hr/employee.jsp").forward(req, resp);
    }

    // action=update_status | request_account
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        try {
            if ("update_status".equals(action)) {
                int eid = Integer.parseInt(req.getParameter("eid"));
                String status = req.getParameter("status"); // Official/Probation/Terminated
                String sql = "UPDATE Employee SET employment_status=? WHERE eid=?";

                try (Connection cn = new EmployeeDBContext().getConnection();  // <-- sửa ở đây
                     PreparedStatement s = cn.prepareStatement(sql)) {
                    s.setString(1, status);
                    s.setInt(2, eid);
                    s.executeUpdate();
                }
                resp.sendRedirect(req.getContextPath()+"/hr/employee?msg=updated");
                return;
            }

            if ("request_account".equals(action)) {
                int requesterEid = (Integer) req.getSession().getAttribute("eid"); // HR Head thao tác
                String name = req.getParameter("target_name");
                String div  = req.getParameter("target_div");
                String pos  = req.getParameter("target_position");
                String note = req.getParameter("note");

                new AccountRequestDBContext().create(requesterEid, name, div, pos, note);
                resp.sendRedirect(req.getContextPath()+"/hr/employee?msg=request-sent");
                return;
            }

            doGet(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
