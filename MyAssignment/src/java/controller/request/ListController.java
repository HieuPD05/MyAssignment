package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/list")
public class ListController extends BaseRequiredAuthorizationController {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        ArrayList<RequestForLeave> requests = db.getByEmployeeAndSubodiaries(user.getEmployee().getId());

        req.setAttribute("requests", requests);
        req.getRequestDispatcher("../view/request/list.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processRequest(req, resp, user);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processRequest(req, resp, user);
    }
}
