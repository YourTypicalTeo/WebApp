<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ page import="jakarta.servlet.http.*, java.sql.*, com.example.forum.utils.Util" %>

<%
  // Έλεγχος αν υπάρχει ενεργή συνεδρία και αν ο χρήστης είναι συνδεδεμένος
  if (session == null || session.getAttribute("userId") == null) {
    // Αν όχι, ανακατεύθυνση στη σελίδα login
    response.sendRedirect("login.html");
    return;
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Post a Message</title>
    <!-- link για to style.css preset που ειναι στο webapp/css/style.css -->
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<h2>Post a Message</h2>

<!-- Φόρμα για αποστολή μηνύματος -->
<form method="post" action="message">
  Topic:
  <select name="topicId" required>
    <%
      // Φόρτωση των διαθέσιμων topic από τη βάση δεδομένων
      try (Connection conn = Util.getConn();
           Statement st = conn.createStatement();
           ResultSet rs = st.executeQuery("SELECT id, name FROM topics")) {
        while (rs.next()) {
    %>
    <!-- Δημιουργία επιλογων στο dropdown με τα topics -->
    <option value="<%= rs.getInt("id") %>"><%= rs.getString("name") %></option>
    <%
        }
      }
    %>
  </select><br><br>

  Message:<br>
  <!-- Περιοχή κειμένου για το μήνυμα -->
  <textarea name="msg" rows="4" cols="50" required></textarea><br><br>

  <button type="submit">Send</button>
</form>

<!-- Περιοχή εμφάνισης μηνύματος από το server -->
<div id="message" style="margin-top: 1em; font-weight: bold; 
  color: <%= "success".equals(request.getAttribute("messageType")) ? "green" : "red" %>;">
  <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
</div>

<script>
  // Προαιρετική λειτουργία JS για εμφάνιση μηνυμάτων δυναμικά στο μελλον
  function showMessage(text, isSuccess = true) {
    const messageDiv = document.getElementById('message');
    messageDiv.style.color = isSuccess ? 'green' : 'red';
    messageDiv.textContent = text;
  }
</script>

</body>
</html>
