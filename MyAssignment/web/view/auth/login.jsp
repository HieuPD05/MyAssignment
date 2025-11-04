<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Đăng nhập – HP System</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<style>
:root { --primary:#3b4b63; --text:#1e293b; --muted:#6b7280; --border:#d1d5db; }
*{ box-sizing:border-box; margin:0; padding:0 }
html,body{
  height:100%;
  font-family:"Segoe UI", Arial, sans-serif;
  display:flex; align-items:center; justify-content:flex-end;
  background: linear-gradient(0deg, rgba(0,0,0,.22), rgba(0,0,0,.22)),
    url('${pageContext.request.contextPath}/img/860.jpg?v=1') center/cover no-repeat fixed;
  position:relative; overflow:hidden;
}
.panel{
  position:relative; z-index:1; background:#fff; width:430px; border-radius:25px;
  box-shadow:0 10px 40px rgba(0,0,0,.20); padding:40px 38px 45px; margin-right:6vw;
  border:1px solid #e3e6eb; animation:fadeInUp .6s ease-out;
}
@keyframes fadeInUp{ from{ opacity:0; transform:translateY(30px) } to{ opacity:1; transform:translateY(0) } }
.brand{ display:flex; align-items:center; gap:14px; margin-bottom:24px; white-space:nowrap }
.logo{ width:50px; height:50px; border-radius:50%; background:var(--primary); color:#fff; display:grid; place-items:center; font-weight:700; font-size:18px }
.title{ font-size:22px; font-weight:700; color:var(--text) }
label{ display:block; margin-top:16px; font-weight:600; color:var(--text) }
input[type=text], input[type=password]{ width:100%; padding:12px 14px; margin-top:6px; border:1px solid var(--border); border-radius:10px; background:#f9fafb; font-size:15px; transition:.25s }
input:focus{ border-color:var(--primary); background:#fff; box-shadow:0 0 0 3px rgba(59,75,99,.15); outline:none }
.btn{ width:100%; margin-top:18px; padding:12px; background:var(--primary); border:none; border-radius:10px; color:#fff; font-weight:700; letter-spacing:.5px; cursor:pointer; transition:.25s }
.btn:hover{ background:#2f3d52; transform:translateY(-1px) }
.hint{ margin-top:14px; text-align:center; color:var(--muted); font-size:13px }
.error-inline{ margin-top:8px; color:#d32f2f; font-size:13px; font-weight:600 }
@media (max-width:900px){ body{ justify-content:center } .panel{ margin-right:0; width:92% } }
</style>
</head>
<body>
<div class="panel">
  <div class="brand">
    <div class="logo">HP</div>
    <h2 class="title">Hệ thống quản lí nghỉ phép</h2>
  </div>
  <form action="${pageContext.request.contextPath}/login" method="post" accept-charset="UTF-8">
    <label for="txtUsername">Tài khoản đăng nhập</label>
    <input type="text" id="txtUsername" name="username" value="${typedUsername != null ? typedUsername : ''}" required autofocus>

    <label for="txtPassword">Mật khẩu</label>
    <input type="password" id="txtPassword" name="password" required>

    <c:if test="${not empty error}">
      <div class="error-inline">${error}</div>
    </c:if>

    <button type="submit" class="btn">Đăng nhập</button>
    <div class="hint">Tài khoản mẫu: <b>mra/mrb/mrc/mrd/mre</b> – mật khẩu <b>123</b>.</div>
  </form>
</div>
</body>
</html>
