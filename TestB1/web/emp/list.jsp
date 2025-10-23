<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Employee"%>
<!DOCTYPE html>
<html>
<head><title>Employee List</title></head>
<body>
<h2>Employee List</h2>

<%
    List<Employee> list = (List<Employee>) session.getAttribute("employees");
    if (list == null || list.isEmpty()) {
%>
    <p>No employees added yet.</p>
<%
    } else {
%>
    <table border="1" cellspacing="0" cellpadding="10" width="300">
        <%
            for (Employee e : list) {
        %>
        <tr>
            <td>
                <b><%= e.getName() %></b><br/>
                <%
                    for (String s : e.getSkills()) {
                %>
                    • <%= s %><br/>
                <%
                    }
                %>
            </td>
        </tr>
        <%
            }
        %>
    </table>
<%
    }
%>

</body>
</html>
