package controller.iam;

import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.iam.Feature;
import model.iam.Role;
import model.iam.User;

public abstract class BaseRequiredAuthorizationController extends BaseRequiredAuthenticationController {

    private boolean isAuthorized(HttpServletRequest req, User user) {
        // Lấy roles trực tiếp từ DB theo uid
        List<Role> roles = new RoleDBContext().getByUserId(user.getUid());

        String url = req.getServletPath(); // ví dụ: /request/list
        if (roles != null) {
            for (Role role : roles) {
                if (role.getFeatures() == null) continue;
                for (Feature f : role.getFeatures()) {
                    if (url.equalsIgnoreCase(f.getUrl())) return true;
                }
            }
        }
        return false;
    }

    protected abstract void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    protected abstract void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (isAuthorized(req, user)) {
            processPost(req, resp, user);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("error", "Access denied!");
            req.getRequestDispatcher("/view/common/message.jsp").forward(req, resp);
        }
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (isAuthorized(req, user)) {
            processGet(req, resp, user);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("error", "Access denied!");
            req.getRequestDispatcher("/view/common/message.jsp").forward(req, resp);
        }
    }
}
