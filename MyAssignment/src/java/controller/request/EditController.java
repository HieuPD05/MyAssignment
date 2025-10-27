package controller.request;

import controller.iam.BaseRequiredAuthenticationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/edit")
public class EditController extends BaseRequiredAuthenticationController {

    // GET: hiển thị form sửa (chỉ cho phép nếu là chủ đơn & status=0)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String rid_raw = req.getParameter("rid");
        if (rid_raw == null) { resp.getWriter().println("Thiếu rid"); return; }
        int rid = Integer.parseInt(rid_raw);

        RequestForLeave r = new RequestForLeaveDBContext().get(rid);
        if (r == null) { resp.getWriter().println("Không tìm thấy đơn"); return; }

        // Chỉ chủ đơn và đơn đang In Progress (status=0) mới được vào form sửa
        if (r.getStatus() != 0 || r.getCreated_by().getId() != user.getEmployee().getId()) {
            resp.getWriter().println("Bạn không có quyền sửa đơn này.");
            return;
        }

        // Tách [TYPE] body từ reason
        String raw = r.getReason() == null ? "" : r.getReason();
        String type = "N/A";
        String body = raw;
        int close = raw.indexOf(']');
        if (raw.startsWith("[") && close > 0) {
            type = raw.substring(1, close);
            if (raw.length() >= close + 2) body = raw.substring(close + 2);
        }
        req.setAttribute("rtype", type);
        req.setAttribute("bodyReason", body);
        req.setAttribute("r", r);
        req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
    }

    // POST: lưu sửa (chỉ update nếu là chủ đơn & status=0)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            int rid = Integer.parseInt(req.getParameter("rid"));
            String from_raw = req.getParameter("from");
            String to_raw   = req.getParameter("to");
            String reason   = req.getParameter("reason");
            String rtype    = req.getParameter("rtype");

            if (from_raw == null || to_raw == null
                || reason == null || reason.trim().isEmpty()
                || rtype == null || rtype.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                RequestForLeave cur = new RequestForLeaveDBContext().get(rid);
                req.setAttribute("r", cur);
                req.setAttribute("rtype", rtype);
                req.setAttribute("bodyReason", reason);
                req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
                return;
            }

            RequestForLeave r = new RequestForLeave();
            r.setId(rid);
            r.setFrom(Date.valueOf(from_raw));
            r.setTo(Date.valueOf(to_raw));
            r.setReason("[" + rtype.trim() + "] " + reason.trim());

            int rows = new RequestForLeaveDBContext().updateBasic(r, user.getEmployee().getId());
            if (rows > 0) {
                req.getSession().setAttribute("message", "✏️ Đã cập nhật đơn.");
            } else {
                req.getSession().setAttribute("message",
                    "❌ Không thể cập nhật (đơn đã duyệt/bị từ chối hoặc không phải của bạn).");
            }
            resp.sendRedirect(req.getContextPath() + "/request/list");
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Lỗi: " + ex.getMessage());
            req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
        }
    }
}
