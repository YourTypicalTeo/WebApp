package com.example.forum;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Δεν απαιτείται αρχικοποίηση
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Παίρνουμε το URI του αιτήματος
        String uri = req.getRequestURI();

        // Ελέγχουμε αν υπάρχει ενεργό session και αν ο χρήστης είναι συνδεδεμένος
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("uname") != null);

        // Αν ο χρήστης δεν είναι συνδεδεμένος και προσπαθεί να μπει σε προστατευμένες σελίδες, τον κάνουμε redirect
        if ((uri.endsWith("topic.html") || uri.endsWith("changePassword.html")) && !loggedIn) {
            res.sendRedirect("login.html"); // Μεταφορά στη σελίδα index
            return;
        }

        // Συνεχίζουμε την αλυσίδα
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Δεν χρειάζεται καθαρισμός πόρων
    }
}
