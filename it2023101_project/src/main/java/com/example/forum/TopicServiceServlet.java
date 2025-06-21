package com.example.forum;

import com.example.forum.utils.ServletUtil;
import com.example.forum.utils.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class TopicServiceServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Εσωτερική κλάση για την αναπαράσταση των δεδομένων ενός θέματος
    static class TopicData {
        public String name;
        public String description;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Έλεγχος αν ο χρήστης είναι συνδεδεμένος
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("uname") == null) {
            // Αν οχι επιστρέφουμε μήνυμα μη εξουσιοδοτημένης πρόσβασης
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Μη εξουσιοδοτημένη πρόσβαση: παρακαλώ συνδεθείτε.");
            ServletUtil.sendResponseData(errorResponse, resp);
            return;
        }

        // Ανάγνωση των δεδομένων του αιτήματος σε αντικείμενο TopicData
        TopicData data = ServletUtil.getRequestData(TopicData.class, req);
        HashMap<String, Object> result = new HashMap<>();

        // Έλεγχος ότι έχει δοθεί όνομα θέματος
        if (data.name == null || data.name.isBlank()) {
            result.put("success", false);
            result.put("error", "Το όνομα του θέματος είναι υποχρεωτικό.");
            ServletUtil.sendResponseData(result, resp);
            return;
        }

        // Εισαγωγή του νέου θέματος στη βάση
        try (Connection conn = Util.getConn()) {
            String sql = "INSERT INTO topics (NAME, DESCRIPTION) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, data.name);
                ps.setString(2, data.description);
                ps.executeUpdate();
                result.put("success", true);
            }
        } catch (SQLException e) {
            // Σε περίπτωση σφάλματος βάσης επιστρέφουμε μήνυμα
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        // Επιστροφή απάντησης σε μορφή JSON
        ServletUtil.sendResponseData(result, resp);
    }
}
