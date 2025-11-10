<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thông báo</title>
<style>
.container{max-width:820px;margin:18px auto;padding:14px;border:1px solid #e5e7eb;border-radius:10px;background:#fff}
.msg{font-size:16px}
</style>
</head>
<body>
<jsp:include page="/view/common/topbar.jspf"/>
<div class="container">
  <div class="msg">
    ${error != null ? error : (success != null ? success : 'OK')}
  </div>
</div>
</body>
</html>
