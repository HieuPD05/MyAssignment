<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Create Employee</title></head>
<body>
<h2>Create Employee</h2>

<form action="create" method="post">
    Name: <input type="text" id="txt_name " name="name"/><br/>
    Gender:
    <input type="radio" id="rad_male" name="gender" value="Male"/> Male
    <input type="radio"id="rad_female" name="gender" value="Female"/> Female<br/>
    Date of Birth: <input type="date" name="dob"/><br/>
    Skills:
    <input type="checkbox" id="cb_java" name="skills" value="Java"/> Java
    <input type="checkbox" id="cb_C" name="skills" value="C#"/> C#
    <input type="checkbox" id="cb_php" name="skills" value="PHP"/> PHP<br/>
    <input type="submit" id="btn_save" value="Save"/>
</form>

<%
    String msg = (String) request.getAttribute("msg");
    if (msg != null) {
        out.println("<p>" + msg + "</p>");
    }
%>

</body>
</html>
