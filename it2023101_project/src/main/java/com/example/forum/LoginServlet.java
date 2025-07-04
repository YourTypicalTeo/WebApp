package com.example.forum;

import com.example.forum.utils.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Λήψη των στοιχείων εισόδου από τη φόρμα
        String uname = req.getParameter("uname");
        String upass = req.getParameter("upass");

        // Έλεγχος αν τα πεδία είναι κενά ή λείπουν
        if (uname == null || upass == null || uname.isEmpty() || upass.isEmpty()) {
            resp.sendRedirect("login.html"); // Ανακατεύθυνση πισω στη φόρμα Login
            return;
        }

        // Δημιουργία hash του κωδικού
        String passHash = Util.getHash256(upass);

        // Έλεγχος στοιχείων σύνδεσης στη βάση δεδομένων
        try (Connection conn = Util.getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, uname FROM users WHERE uname = ? AND upasshash = ?")) {

            ps.setString(1, uname);
            ps.setString(2, passHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Αν τα στοιχεία είναι σωστά, δημιουργείται συνεδρία
                    HttpSession session = req.getSession();
                    session.setAttribute("userId", rs.getInt("id"));
                    session.setAttribute("uname", rs.getString("uname"));

                    // Ανακατεύθυνση στην κεντρική σελίδα
                    resp.sendRedirect("main.jsp");
                } else {
                    // Αν αποτύχει η σύνδεση, επιστροφή στη φόρμα εισόδου
                    resp.sendRedirect("login.html");
                }
            }
        } catch (SQLException e) {
            // Αν υπάρξει σφάλμα βάσης, το πετάμε για να εμφανιστεί stack trace
            throw new ServletException("Σφάλμα βάσης δεδομένων", e);
        }
    }
}
