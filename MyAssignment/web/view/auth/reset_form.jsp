<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Đặt lại mật khẩu</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 24px; }
    .wrap { max-width: 480px; margin: auto; }
    .msg { padding:10px; margin:10px 0; border-radius:6px; }
    .err { background:#ffecec; color:#b10000; border:1px solid #ffbcbc; }
    label { display:block; margin-top:10px; }
    input[type=password] { width:100%; padding:8px; }
    button { margin-top:12px; padding:8px 16px; }
  </style>
</head>
<body>
  <jsp:include page="/view/common/bell.jspf"/>

  <div class="wrap">
    <h2>Đặt lại mật khẩu</h2>

    <c:if test="${not empty error}">
      <div class="msg err">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/reset">
      <input type="hidden" name="token" value="${token}">
      <label>Mật khẩu mới</label>
      <input type="password" name="password" minlength="4" required>

      <label>Nhập lại mật khẩu</label>
      <input type="password" name="confirm" minlength="4" required>

      <button type="submit">Cập nhật</button>
    </form>

    <p style="margin-top:16px;">
      <a href="${pageContext.request.contextPath}/login">← Quay lại đăng nhập</a>
    </p>
  </div>
</body>
</html>
