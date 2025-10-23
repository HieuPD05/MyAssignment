<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Course</title>
</head>
<body>
    
    <form action="create" method="POST">
        Name: <input type="text" id="txt_name" name="name"/><br/>
        From: <input type="text" id="txt_from" name="from"/><br/>
        To: <input type="text" id="txt_to" name="to"/><br/>
        Online: <input type="checkbox" id="cb_online" name="online"/><br/>
        Subject: <input type="text" id="cb_sub" name="subject"/><br/>
        <input type="submit" id="btn_submit" value="Save"/>
    </form>
</body>
</html>
