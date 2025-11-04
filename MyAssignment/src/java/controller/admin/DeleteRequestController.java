package controller.admin;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.iam.User;

@WebServlet(urlPatterns = "/admin/request/delete")
public class DeleteRequestController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        int rid = Integer.parseInt(req.getParameter("rid"));
        int rows = new RequestForLeaveDBContext().adminDelete(rid, user.getEmployee().getId());
        if (rows > 0) {
            req.getSession().setAttribute("message", "🗑️ Admin đã xoá đơn #" + rid);
        } else {
            req.getSession().setAttribute("message", "❌ Không xoá được đơn #" + rid);
        }
        resp.sendRedirect(req.getContextPath() + "/request/list");
    }
}
