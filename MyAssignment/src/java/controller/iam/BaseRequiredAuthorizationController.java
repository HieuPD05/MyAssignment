package controller.iam;

import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.iam.Feature;
import model.iam.Role;
import model.iam.User;

public abstract class BaseRequiredAuthorizationController extends BaseRequiredAuthenticationController {

    private boolean isAuthorized(HttpServletRequest req, User user) {
        if (user.getRoles().isEmpty()) {
            RoleDBContext db = new RoleDBContext();
            user.setRoles(db.getByUserId(user.getId()));
            req.getSession().setAttribute("auth", user);
        }
        String url = req.getServletPath();
        for (Role role : user.getRoles()) {
            for (Feature f : role.getFeatures()) {
                if (f.getUrl().equals(url)) return true;
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
        if (isAuthorized(req, user)) processPost(req, resp, user);
        else resp.getWriter().println("access denied!");
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (isAuthorized(req, user)) processGet(req, resp, user);
        else resp.getWriter().println("access denied!");
    }
}
