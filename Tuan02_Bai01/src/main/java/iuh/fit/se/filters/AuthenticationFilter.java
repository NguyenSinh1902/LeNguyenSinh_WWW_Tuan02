package iuh.fit.se.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    private String username;
    private String password;

    @Override
    public void init(FilterConfig filterConfig) {
        username = filterConfig.getInitParameter("username");
        password = filterConfig.getInitParameter("password");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String u = req.getParameter("username");
        String p = req.getParameter("password");

        if (username.equals(u) && password.equals(p)) {
            chain.doFilter(request, response);
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("Invalid username or password!");
        }
    }

    @Override
    public void destroy() {}
}
