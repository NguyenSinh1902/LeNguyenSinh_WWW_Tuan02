package iuh.fit.se;

import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String u = req.getParameter("username");

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("Hello " + u + ", Login Successful!");
    }
}
