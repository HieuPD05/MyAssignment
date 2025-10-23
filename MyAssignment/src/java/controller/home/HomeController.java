package controller.home;

import controller.iam.BaseRequiredAuthenticationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.iam.User;

@WebServlet(urlPatterns = "/home")
public class HomeController extends BaseRequiredAuthenticationController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();

        int totalInProgress = db.countByStatus(user.getEmployee().getId(), 0);
        int totalApproved = db.countByStatus(user.getEmployee().getId(), 1);
        int totalRejected = db.countByStatus(user.getEmployee().getId(), 2);

        req.setAttribute("user", user);
        req.setAttribute("inprogress", totalInProgress);
        req.setAttribute("approved", totalApproved);
        req.setAttribute("rejected", totalRejected);

        req.getRequestDispatcher("/view/home/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        doGet(req, resp, user);
    }
}
