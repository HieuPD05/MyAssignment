package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/review")
public class ReviewController extends BaseRequiredAuthorizationController {

  @Override
protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
        throws ServletException, IOException {

    // Đặt encoding UTF-8 cho output (fix lỗi tiếng Việt)
    resp.setContentType("text/html; charset=UTF-8");
    req.setCharacterEncoding("UTF-8");

    String rid_raw = req.getParameter("rid");

    if (rid_raw == null || rid_raw.trim().isEmpty()) {
        resp.getWriter().println("❌ Thiếu mã đơn (rid) trên URL!");
        return;
    }

    try {
        int rid = Integer.parseInt(rid_raw);
        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        RequestForLeave request = db.get(rid);

        if (request == null) {
            resp.getWriter().println("❌ Không tìm thấy đơn nghỉ phép có id = " + rid);
            return;
        }

        req.setAttribute("request", request);
        req.getRequestDispatcher("../view/request/review.jsp").forward(req, resp);

    } catch (NumberFormatException e) {
        resp.getWriter().println("❌ Mã đơn không hợp lệ!");
    }
}


    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        int rid = Integer.parseInt(req.getParameter("rid"));
        String action = req.getParameter("action"); // approve hoặc reject

        int status;
        if ("approve".equalsIgnoreCase(action)) {
            status = 1; // Approved
        } else {
            status = 2; // Rejected
        }

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        db.updateStatus(rid, status, user.getEmployee().getId());

        resp.sendRedirect("list");
    }
}
