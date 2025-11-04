package controller.admin;

import controller.iam.BaseRequiredAuthorizationController;
import dal.AuditDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import model.AuditLog;
import model.iam.User;

@WebServlet(urlPatterns = "/admin/audit")
public class AuditController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        ArrayList<AuditLog> logs = new AuditDBContext().list();
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("../view/admin/audit.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processGet(req, resp, user);
    }
}
