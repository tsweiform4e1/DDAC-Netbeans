/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@SessionScoped
public class loginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        loginController session = (loginController) req.getSession().getAttribute("loginController");
        String url = req.getRequestURI();

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.  NO CACHE
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.   NO CACHE
        resp.setDateHeader("Expires", 0); // Proxies.   NO CACHE

        if (session == null || !session.islogged) {
            System.out.println("1");
            if (url.indexOf("/faces/admin") >= 0 || url.indexOf("/faces/agent") >= 0) {
                System.out.println("2");
                resp.sendRedirect(req.getServletContext().getContextPath() + "/faces/index.xhtml");
            } else {
                System.out.println("3");
                chain.doFilter(request, response);
            }
        } else {
            System.out.println("4");
            if (url.indexOf("logout.xhtml") >= 0) {
                System.out.println("5");
                req.getSession().removeAttribute("loginController");
                resp.sendRedirect(req.getServletContext().getContextPath() + "/faces/index.xhtml");
            } else if (session.getUserType().equals("Admin")) {
                if (url.indexOf("/faces/agent") >= 0) {
                    resp.sendRedirect(req.getServletContext().getContextPath() + "/faces/admin/adminHome.xhtml");
                } else {
                    chain.doFilter(request, response);
                }
            } else if (session.getUserType().equals("Agent")) {
                if (url.indexOf("/faces/admin") >= 0) {
                    resp.sendRedirect(req.getServletContext().getContextPath() + "/faces/agent/agentHome.xhtml");
                }else{
                    chain.doFilter(request, response);
                }
            } else {
                System.out.println("6");
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

}
