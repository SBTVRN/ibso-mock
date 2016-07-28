package ru.sbt.drtmn.lab.webapp;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MessageFilter implements Filter {
    private static transient Logger logger = Logger.getLogger( MessageFilter.class);
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        // grab messages from the session and put them into request. This is so they're not lost in a redirect
        Object message = request.getSession().getAttribute("message");
        if (message != null) {
            logger.debug("Message Filter's message is not null; chain.doFilter ");
            request.setAttribute("message", message);
            request.getSession().removeAttribute("message");
        }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}
