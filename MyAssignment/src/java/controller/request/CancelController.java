package controller.request;

import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class CancelController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        String sRid = req.getParameter("rid");
        try {
            int rid = Integer.parseInt(sRid);
            new RequestForLeaveDBContext().cancel(rid, eid);
            resp.sendRedirect(req.getContextPath()+"/request/list?msg=canceled");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
