package controller.request;

import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        String ridStr = req.getParameter("rid");
        if (ridStr == null) { resp.sendError(400, "Missing rid"); return; }

        try {
            int rid = Integer.parseInt(ridStr);
            RequestForLeaveDBContext rdb = new RequestForLeaveDBContext();
            var r = rdb.get(rid);
            if (r == null || r.getCreatedBy() != eid || r.getStatus() != 0) {
                req.setAttribute("error", "Bạn không thể sửa đơn này.");
                req.getRequestDispatcher("/view/common/message.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("r", r);
            req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
        } catch (NumberFormatException nfe) {
            resp.sendError(400, "Invalid rid");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        String ridStr = req.getParameter("rid");
        String sFrom  = trim(req.getParameter("from"));
        String sTo    = trim(req.getParameter("to"));
        String sLtid  = trim(req.getParameter("ltid"));
        String reason = trim(req.getParameter("reason"));

        try {
            int rid = Integer.parseInt(ridStr);
            RequestForLeaveDBContext rdb = new RequestForLeaveDBContext();
            var r = rdb.get(rid);
            if (r == null || r.getCreatedBy() != eid || r.getStatus() != 0) {
                req.setAttribute("error", "Bạn không thể sửa đơn này.");
                req.getRequestDispatcher("/view/common/message.jsp").forward(req, resp);
                return;
            }

            if (sFrom == null || sTo == null || sLtid == null) {
                stick(req, r, sFrom, sTo, sLtid, reason);
                req.setAttribute("error", "Vui lòng điền đủ ngày bắt đầu/kết thúc và loại nghỉ.");
                req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
                return;
            }

            LocalDate from = LocalDate.parse(sFrom); // format mặc định yyyy-MM-dd
            LocalDate to   = LocalDate.parse(sTo);
            if (to.isBefore(from)) {
                stick(req, r, sFrom, sTo, sLtid, reason);
                req.setAttribute("error", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
                req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
                return;
            }

            int ltid = Integer.parseInt(sLtid);

            // Luật 8:00 cho “nghỉ trong ngày hôm nay”
            if (!rdb.validateSubmitDate(from)) {
                stick(req, r, sFrom, sTo, sLtid, reason);
                req.setAttribute("error", "Phải sửa trước 08:00 nếu nghỉ hôm nay.");
                req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
                return;
            }

            rdb.updateBasic(rid, from, to, ltid, reason);
            resp.sendRedirect(req.getContextPath()+"/request/list?msg=updated");
        } catch (NumberFormatException nfe) {
            req.setAttribute("error", "Dữ liệu không hợp lệ (sai định dạng số/ngày).");
            req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
        } catch (SQLException sqle) {
            req.setAttribute("error", sqle.getMessage());
            req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }

    private static void stick(HttpServletRequest req, Object r,
                              String sFrom, String sTo, String sLtid, String reason) {
        req.setAttribute("r", r);                 // để JSP có dữ liệu cũ
        req.setAttribute("stick_from", sFrom);    // giữ lại input người dùng
        req.setAttribute("stick_to", sTo);
        req.setAttribute("stick_ltid", sLtid);
        req.setAttribute("stick_reason", reason);
    }
}
