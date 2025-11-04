package controller.request;

import controller.iam.BaseRequiredAuthenticationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/create")
public class CreateController extends BaseRequiredAuthenticationController {

    @SuppressWarnings("unchecked")
    private boolean canCreate(HttpServletRequest req) {
        Object obj = req.getSession().getAttribute("allowed");
        if (obj == null) return false;
        try {
            HashMap<String, Boolean> allowed = (HashMap<String, Boolean>) obj;
            Boolean v = allowed.get("/request/create");
            return v != null && v;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    private void showPermissionError(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("permError", true);
        req.setAttribute("error",
          "❌ Tài khoản của bạn chưa đủ quyền tạo đơn (ví dụ: nhân viên chưa chính thức). "
        + "Vui lòng liên hệ quản lý để được cấp quyền.");
        req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (!canCreate(req)) { showPermissionError(req, resp); return; }
        req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (!canCreate(req)) { showPermissionError(req, resp); return; }
        try {
            String from_raw = req.getParameter("from");
            String to_raw   = req.getParameter("to");
            String reason   = req.getParameter("reason");
            String rtype    = req.getParameter("rtype");

            if (from_raw == null || to_raw == null
                || reason == null || reason.trim().isEmpty()
                || rtype == null || rtype.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
                return;
            }

            Date from = Date.valueOf(from_raw);
            Date to   = Date.valueOf(to_raw);
            String combinedReason = "[" + rtype.trim() + "] " + reason.trim();

            RequestForLeave r = new RequestForLeave();
            r.setFrom(from);
            r.setTo(to);
            r.setReason(combinedReason);
            r.setCreated_by(user.getEmployee());
            r.setStatus(0);

            new RequestForLeaveDBContext().insert(r);

            req.getSession().setAttribute("message", "✅ Tạo đơn nghỉ phép thành công!");
            resp.sendRedirect(req.getContextPath() + "/request/list");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Lỗi khi tạo đơn nghỉ phép: " + e.getMessage());
            req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
        }
    }
}
