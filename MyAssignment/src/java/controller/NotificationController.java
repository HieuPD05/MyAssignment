package controller;

import dal.NotificationDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class NotificationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // demo: nếu có session auth -> lấy uid; nếu chưa đăng nhập thì danh sách rỗng
        model.iam.User auth = (model.iam.User) req.getSession().getAttribute("auth");
        if (auth != null) {
            try {
                req.setAttribute("notis",
                        new NotificationDBContext().latest(auth.getUid(), 20));
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.getRequestDispatcher("/view/notification/list.jsp").forward(req, resp);
    }
}
