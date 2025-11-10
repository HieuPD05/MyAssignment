package controller.request;

import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.RequestForLeave;

public class ListController extends HttpServlet {
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        // lọc
        String sPage   = req.getParameter("page");
        String sStatus = req.getParameter("status"); // "", "0","1","2","3"
        String nameLike= req.getParameter("name");   // lọc theo tên người tạo
        int page = 1;
        try { page = Math.max(1, Integer.parseInt(sPage)); } catch (Exception ignore){}

        // quyền xem: HEAD/CEO được xem cấp dưới; TeamLead xem team; nhân viên chỉ xem của mình
        // Ở đây demo: nếu trong session có flag "includeSubs" thì mở rộng; mặc định false
        Boolean includeSubs = (Boolean) req.getSession().getAttribute("includeSubs");
        if (includeSubs == null) includeSubs = false;

        Integer status = null;
        if (sStatus != null && !sStatus.isEmpty()) {
            try { status = Integer.parseInt(sStatus); } catch (Exception ignore){}
        }

        try {
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            int total = db.countMyAndSubordinates(eid, includeSubs, status, nameLike);
            List<RequestForLeave> list = db.listMyAndSubordinates(eid, includeSubs, status, nameLike, page, PAGE_SIZE);

            req.setAttribute("list", list);
            req.setAttribute("page", page);
            req.setAttribute("pages", (int)Math.ceil(total / (double)PAGE_SIZE));
            req.setAttribute("status", sStatus==null?"":sStatus);
            req.setAttribute("name", nameLike==null?"":nameLike);
            req.getRequestDispatcher("/view/request/list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
