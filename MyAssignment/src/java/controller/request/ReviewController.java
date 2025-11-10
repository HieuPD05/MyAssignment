package controller.request;

import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ReviewController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer approverEid = (Integer) req.getSession().getAttribute("eid");
        if (approverEid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        String action = req.getParameter("action"); // approve | reject
        String sRid   = req.getParameter("rid");
        String note   = req.getParameter("note");

        try {
            int rid = Integer.parseInt(sRid);
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();

            if ("approve".equals(action)) {
                db.approve(rid, approverEid, note == null ? "" : note);
                resp.sendRedirect(req.getContextPath()+"/request/list?msg=approved");
                return;
            }
            if ("reject".equals(action)) {
                db.reject(rid, approverEid, note == null ? "" : note);
                resp.sendRedirect(req.getContextPath()+"/request/list?msg=rejected");
                return;
            }
            resp.sendRedirect(req.getContextPath()+"/request/list");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
