<%@ page session="true" %>
<%
    String uname = (String) session.getAttribute("uname");
    if (uname == null) {
        response.sendRedirect("login.html");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Main Menu</title>
    <style>
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f9f9f9;
            margin: 2rem;
            color: #333;
        }

        .main-menu {
            max-width: 400px;
            margin: 3rem auto;
            background-color: white;
            padding: 2.5rem 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: center;
        }

        .main-menu h1 {
            margin-bottom: 2rem;
            font-weight: 700;
            font-size: 2.2rem;
        }

        .main-menu p {
            margin-bottom: 2rem;
            font-size: 1.1rem;
            color: #555;
        }

        .main-menu a {
            display: block;
            width: 100%;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            padding: 0.75rem 0;
            margin: 0.75rem 0;
            font-size: 1.1rem;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .main-menu a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="main-menu">
        <h1>Welcome, <%= uname %>!</h1>
        <p>Select an option:</p>
        <a href="topic.html">Create Topic</a>
        <a href="message.jsp">Send Message</a>
        <a href="changePassword.html">Change Password</a>
    </div>
</body>
</html>
