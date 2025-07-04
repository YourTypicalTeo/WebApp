package com.example.forum;

import com.example.forum.utils.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class ChangePasswordServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Έλεγχος αν υπάρχει ενεργή συνεδρία και αν ο χρήστης είναι συνδεδεμένος
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("uname") == null) {
            resp.sendRedirect("login.html");  // Ανακατεύθυνση στη σελίδα σύνδεσης αν δεν υπάρχει συνεδρία
            return;
        }

        // Ανάγνωση παραμέτρων από την αίτηση
        String uname = req.getParameter("uname");
        String oldPass = req.getParameter("oldpass");
        String newPass1 = req.getParameter("newpass1");
        String newPass2 = req.getParameter("newpass2");

        String loggedInUser = (String) session.getAttribute("uname");

        // Έλεγχος ταυτοποίησης χρήστη επιτρέπεται αλλαγή μόνο για τον τρέχοντα χρήστη
        if (!uname.equals(loggedInUser)) {
            resp.sendRedirect(redirectWithMsg("Μπορείτε να αλλάξετε μόνο τον δικό σας κωδικό.", "error"));
            return;
        }

        // Έλεγχος ότι όλα τα πεδία έχουν συμπληρωθεί
        if (oldPass == null || newPass1 == null || newPass2 == null ||
            oldPass.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()) {
            resp.sendRedirect(redirectWithMsg("Όλα τα πεδία κωδικού είναι υποχρεωτικά.", "error"));
            return;
        }

        // Έλεγχος ότι τα νέα password ταιριάζουν μεταξύ τους
        if (!newPass1.equals(newPass2)) {
            resp.sendRedirect(redirectWithMsg("Οι νέοι κωδικοί δεν ταιριάζουν.", "error"));
            return;
        }

        // Έλεγχος ότι ο νέος κωδικός διαφέρει από τον παλιο
        if (newPass1.equals(oldPass)) {
            resp.sendRedirect(redirectWithMsg("Ο νέος κωδικός πρέπει να είναι διαφορετικός από τον παλιό.", "error"));
            return;
        }

        // Έλεγχος ότι ο νέος κωδικός(τουλάχιστον 6 χαρακτήρες, μονο γράμματα και αριθμοί)
        if (!newPass1.matches("[A-Za-z0-9]{6,}")) {
            resp.sendRedirect(redirectWithMsg("Ο νέος κωδικός πρέπει να έχει τουλάχιστον 6 χαρακτήρες και να περιέχει μόνο γράμματα και αριθμούς.", "error"));
            return;
        }

        // Δημιουργία hash για τους κωδικούς
        String oldHash = Util.getHash256(oldPass);
        String newHash = Util.getHash256(newPass1);

        // Ενημέρωση της βάσης δεδομένων
        try (Connection conn = Util.getConn()) {
            String sql = "UPDATE users SET upasshash = ? WHERE uname = ? AND upasshash = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newHash);
                ps.setString(2, uname);
                ps.setString(3, oldHash);

                int updated = ps.executeUpdate();

                // Αν η ενημέρωση πέτυχε
                if (updated > 0) {
                    resp.sendRedirect(redirectWithMsg("Ο κωδικός ενημερώθηκε με επιτυχία.", "success"));
                } else {
                    resp.sendRedirect(redirectWithMsg("Λάθος τρέχων κωδικός.", "error"));
                }
            }
        } catch (SQLException e) {
            // Αν προκύψει σφάλμα βάσης δεδομένων, το καταγράφουμε και το πετάμε
            throw new ServletException("Σφάλμα βάσης δεδομένων κατά την αλλαγή κωδικού", e);
        }
    }

    // Βοηθητική μέθοδος για ανακατεύθυνση με μήνυμα στην URL
    private String redirectWithMsg(String msg, String type) throws IOException {
        return "changePassword.html?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8) + "&type=" + type;
    }
}
