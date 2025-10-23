<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String user = (String) session.getAttribute("loggedUser");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h2>Login success!</h2>
    <p>Welcome, <b><%= user %></b></p>
    <a href="logout.jsp">Logout</a>
</body>
</html>
