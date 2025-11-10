package controller.request;

import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class CreateController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        // Hiển thị form create.jsp (form sẽ có select loại nghỉ: ANNUAL/UNPAID/MATERNITY/OTHER)
        req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        String sFrom = req.getParameter("from");
        String sTo   = req.getParameter("to");
        String sLtid = req.getParameter("ltid"); // ltid (int) hoặc bạn có thể cho code rồi map ở DAL
        String reason= req.getParameter("reason");

        try {
            LocalDate from = LocalDate.parse(sFrom);
            LocalDate to   = LocalDate.parse(sTo);
            int ltid = Integer.parseInt(sLtid);

            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            int rid = db.create(eid, from, to, ltid, reason);
            if (rid <= 0) throw new Exception("Không tạo được đơn.");

            resp.sendRedirect(req.getContextPath()+"/request/list?msg=created");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("typed_from", sFrom);
            req.setAttribute("typed_to", sTo);
            req.setAttribute("typed_ltid", sLtid);
            req.setAttribute("typed_reason", reason);
            req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
        }
    }
}
