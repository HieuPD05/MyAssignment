package controller;

import dal.AdminAccountDBContext;
import dal.NotificationDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import model.iam.User;
import util.BCryptUtil;

@WebServlet(name = "AdminAccountController", urlPatterns = {"/admin/account"})
public class AdminAccountController extends HttpServlet {

    // Danh sách + Form tạo nhanh + Xoá
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = parseInt(req.getParameter("page"), 1);
        int size = 10;
        int offset = (page - 1) * size;

        try {
            AdminAccountDBContext db = new AdminAccountDBContext();
            ArrayList<User> users = db.listUsers(offset, size);
            int total = db.countUsers();
            int totalPages = (int) Math.ceil(total / (double) size);

            req.setAttribute("users", users);
            req.setAttribute("page", page);
            req.setAttribute("totalPages", totalPages);
            req.getRequestDispatcher("/view/admin/account.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Tạo tài khoản (username, displayname, password)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = nvl(req.getParameter("action"));
        try {
            AdminAccountDBContext db = new AdminAccountDBContext();

            if ("create".equalsIgnoreCase(action)) {
                String username = nvl(req.getParameter("username")).trim();
                String display = nvl(req.getParameter("displayname")).trim();
                String rawPass  = nvl(req.getParameter("password"));

                if (username.isEmpty() || display.isEmpty() || rawPass.isEmpty()) {
                    req.getSession().setAttribute("msg", "⚠ Vui lòng nhập đủ username / displayname / password.");
                    resp.sendRedirect(req.getContextPath() + "/admin/account");
                    return;
                }
                String hash = BCryptUtil.hash(rawPass);
                int uid = db.createUser(username, hash, display);
                // Gửi thông báo demo cho chính admin (uid=?) → bỏ qua nếu chưa có uid admin hiện hành
                try {
                    NotificationDBContext notiDb = new NotificationDBContext();
                    notiDb.push(/*toUid*/ uid, "Tạo tài khoản",
                            "Tài khoản " + username + " đã được tạo.",
                            req.getContextPath() + "/admin/account");
                } catch (SQLException ignore) {}

                req.getSession().setAttribute("msg", "✅ Đã tạo tài khoản: " + username);
                resp.sendRedirect(req.getContextPath() + "/admin/account");
                return;
            }

            if ("delete".equalsIgnoreCase(action)) {
                int uid = parseInt(req.getParameter("uid"), -1);
                if (uid > 0) {
                    db.deleteUser(uid);
                    req.getSession().setAttribute("msg", "🗑️ Đã xoá tài khoản UID=" + uid);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/account");
                return;
            }

            req.getSession().setAttribute("msg", "⚠ Action không hợp lệ.");
            resp.sendRedirect(req.getContextPath() + "/admin/account");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
    private String nvl(String s) { return s == null ? "" : s; }
}
