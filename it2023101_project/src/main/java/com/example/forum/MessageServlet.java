package com.example.forum;

import com.example.forum.utils.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class MessageServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Έλεγχος αν υπάρχει ενεργή συνεδρία και αν ο χρήστης είναι συνδεδεμένος
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.html");  // Ανακατεύθυνση στη login.html
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String topicIdStr = req.getParameter("topicId");
        String msg = req.getParameter("msg");

        // Έλεγχος ότι υπάρχουν τα απαραίτητα στοιχεία
        if (topicIdStr == null || msg == null || msg.trim().isEmpty()) {
            req.setAttribute("message", "Το θέμα και το μήνυμα είναι υποχρεωτικά.");
            req.setAttribute("messageType", "error");
            req.getRequestDispatcher("message.jsp").forward(req, resp);
            return;
        }

        int topicId = Integer.parseInt(topicIdStr);

        try (Connection conn = Util.getConn()) {
            // Εισαγωγή νέου μηνύματος στη βάση
            String insertSQL = "INSERT INTO messages VALUES (NULL, ?, ?, ?, NULL)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, topicId);
                ps.setInt(2, userId);
                ps.setString(3, msg.trim());
                ps.executeUpdate();
            }

            // Υπολογισμός πλήθους μηνυμάτων για το συγκεκριμένο θέμα
            int count = 0;
            String countSQL = "SELECT COUNT(*) FROM messages WHERE topic_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(countSQL)) {
                ps.setInt(1, topicId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) count = rs.getInt(1);
                }
            }

            // Επιστροφή με επιβεβαίωση και εμφάνιση αριθμού μηνυμάτων
            req.setAttribute("message", "Το μήνυμα στάλθηκε! Συνολικά μηνύματα στο θέμα: " + count);
            req.setAttribute("messageType", "success");
            req.getRequestDispatcher("message.jsp").forward(req, resp);

        } catch (SQLException e) {
            // Αν προκύψει σφάλμα βάσησ
            throw new ServletException("Σφάλμα κατά την αποστολή μηνύματος", e);
        }
    }
}
