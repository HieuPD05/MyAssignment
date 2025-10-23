package controller.course;

import dal.CourseDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import model.Course;
import model.User;

public class CreateCourseController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession();
        User auth = (User) session.getAttribute("auth");

        if (auth == null) {
            out.print("Access denied");
        } else {
            req.getRequestDispatcher("/view/course/create.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
                PrintWriter out = resp.getWriter();


        HttpSession session = req.getSession();
        User auth = (User) session.getAttribute("auth");

        if (auth == null) {
            out.print("Access denied");

            return;
        }

        String name = req.getParameter("name");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        boolean online = req.getParameter("online") != null;
        String subject = req.getParameter("subject");

        Course c = new Course();
        c.setName(name);
        c.setFrom(from);
        c.setTo(to);
        c.setOnline(online);
        c.setSubject(subject);
        c.setCreatedBy(auth);

        CourseDBContext db = new CourseDBContext();
        db.insert(c);

        req.setAttribute("message", "Course created successfully!");
        req.getRequestDispatcher("/view/auth/message.jsp").forward(req, resp);
    }
}
