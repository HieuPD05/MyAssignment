package controller;

import dal.NotificationDBContext;
import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.PasswordResetToken;
import model.iam.User;

public class PasswordResetRequestController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/view/auth/reset_request.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String minutesStr = req.getParameter("ttl");
        int ttl = 30;
        try { ttl = Integer.parseInt(minutesStr); } catch (Exception ignore) {}

        try {
            UserDBContext udb = new UserDBContext();
            User u = udb.getByUsername(username);
            if (u == null) {
                req.setAttribute("error", "Không tìm thấy tài khoản!");
                req.getRequestDispatcher("/view/auth/reset_request.jsp").forward(req, resp);
                return;
            }
            PasswordResetToken t = udb.createResetToken(u.getUid(), ttl);
            String link = req.getContextPath() + "/auth/reset?token=" + t.getToken();
            // demo: hiện link ra giao diện + đẩy notification chuông
            req.setAttribute("success", "Đã tạo token đặt lại mật khẩu (hết hạn trong " + ttl + " phút).");
            req.setAttribute("resetLink", link);

            try {
                new NotificationDBContext().push(u.getUid(),
                        "Yêu cầu đặt lại mật khẩu",
                        "Nhấn để đặt lại mật khẩu.",
                        link);
            } catch (Exception ignore) {}

            req.getRequestDispatcher("/view/auth/reset_request.jsp").forward(req, resp);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
