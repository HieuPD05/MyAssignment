package controller.iam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import dal.RoleDBContext;
import model.iam.Role;
import model.iam.User;

public abstract class BaseRequiredAuthenticationController extends HttpServlet {

    // Controller con implement 2 hàm này (đã có tham số User)
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = ensureAuth(req, resp);
        if (u == null) return;
        doGet(req, resp, u);
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = ensureAuth(req, resp);
        if (u == null) return;
        doPost(req, resp, u);
    }

    /** Kiểm tra đăng nhập; nếu thiếu → redirect /login. Đồng thời gắn roleName (ưu tiên cao nhất) vào session. */
    private User ensureAuth(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession ss = req.getSession(false);
        if (ss == null || ss.getAttribute("auth") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        User user = (User) ss.getAttribute("auth");

        // Gắn roleName nếu chưa có (lấy trực tiếp từ DB theo uid)
        if (ss.getAttribute("roleName") == null) {
            List<Role> roles = new RoleDBContext().getByUserId(user.getUid());
            ss.setAttribute("roleName", resolveTopRole(roles));
        }
        return user;
    }

    /** Ưu tiên: ADMIN > CEO > HEAD > TEAM_LEAD > EMPLOYEE */
    private String resolveTopRole(List<Role> roles) {
        String top = "EMPLOYEE";
        if (roles == null) return top;
        for (Role r : roles) {
            String rn = r.getName(); // <-- dùng đúng getter của bạn
            if ("ADMIN".equalsIgnoreCase(rn)) return "ADMIN";
            if ("CEO".equalsIgnoreCase(rn))   top = "CEO";
            else if ("HEAD".equalsIgnoreCase(rn) && !"CEO".equals(top)) top = "HEAD";
            else if ("TEAM_LEAD".equalsIgnoreCase(rn)
                    && !"CEO".equals(top) && !"HEAD".equals(top)) top = "TEAM_LEAD";
        }
        return top;
    }
}
