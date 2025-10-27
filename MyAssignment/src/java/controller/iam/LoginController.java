package controller.iam;

import dal.UserDBContext;
import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import model.iam.User;
import model.iam.Role;
import model.iam.Feature;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDBContext db = new UserDBContext();
        User u = db.get(username, password);
        if (u != null) {
            // nạp roles & features
            RoleDBContext rdb = new RoleDBContext();
            u.setRoles(rdb.getByUserId(u.getId()));

            // build allowed map để ẩn/hiện menu
            HashMap<String, Boolean> allowed = new HashMap<>();
            for (Role r : u.getRoles()) {
                for (Feature f : r.getFeatures()) {
                    allowed.put(f.getUrl(), true);
                }
            }

            HttpSession session = req.getSession();
            session.setAttribute("auth", u);
            session.setAttribute("allowed", allowed);
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            // ❗Trả về chính trang login và hiển thị lỗi NGAY DƯỚI ô mật khẩu
            req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không hợp lệ.");
            req.setAttribute("typedUsername", username); // giữ lại username đã gõ
            req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // mở trang login
        req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
    }
}
