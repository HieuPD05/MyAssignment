<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Sửa đơn nghỉ phép</title>
  <style>
    :root { --base:#3b4b63; --base-strong:#334257; --base-soft:#51637d; --ink:#ffffff; --line:#2d394a; --shadow:0 8px 20px rgba(0,0,0,.25); }
    body { margin:0; font-family:Inter,"Segoe UI",Arial,sans-serif; min-height:100vh; display:flex; justify-content:center; align-items:center;
      background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)), url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed; }
    body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}
    .form-box { position:relative; z-index:1; width:520px; max-width:92vw; background:var(--base); border:1px solid var(--line); border-radius:18px; padding:28px; box-shadow:var(--shadow); }
    h2 { margin:0 0 18px; color:#fff; font-size:22px; display:flex; align-items:center; gap:10px; justify-content:center; }
    label { display:block; margin-top:14px; margin-bottom:6px; font-weight:600; color:#fff; }
    .input, textarea { width:100%; padding:12px 14px; border-radius:12px; border:1px solid var(--line); background:var(--base-strong); color:#fff; font-size:15px; outline:none; box-sizing: border-box; }
    .input:focus, textarea:focus { border-color:#51637d; box-shadow:0 0 0 3px rgba(81,99,125,.35); }
    textarea { resize:vertical; min-height:100px }

    /* Buttons */
    .actions{ display:flex; gap:12px; margin-top:22px; }
    .btn{ flex:1; padding:12px; border:none; border-radius:12px; font-weight:700; cursor:pointer; font-size:16px; transition:all .25s; }
    /* dùng cùng màu cho cả Hủy và Lưu thay đổi */
    .btn-primary{ background:#1e293b; color:#fff; }
    .btn-primary:hover{ background:#2d3e59; transform:translateY(-1px); }

    .error { background:rgba(239,68,68,.15); border:1px solid rgba(239,68,68,.4); color:#fee2e2; padding:10px; border-radius:12px; margin-bottom:12px; }
    input[type="date"]::-webkit-calendar-picker-indicator { filter: invert(1); opacity: 1; display: block; cursor: pointer; }

    @media (max-width:480px){
      .actions{ flex-direction:column; }
      .btn{ width:100%; }
    }
  </style>
</head>
<body>
<div class="form-box">
  <h2>✏️ Sửa đơn nghỉ phép</h2>
  <c:if test="${not empty error}"><div class="error">${error}</div></c:if>

  <form action="${pageContext.request.contextPath}/request/edit" method="post">
    <input type="hidden" name="rid" value="${r.id}"/>

    <label>Loại đơn:</label>
    <input class="input" type="text" name="rtype" value="${rtype}" required/>

    <label>Từ ngày:</label>
    <input class="input" type="date" name="from" value="${r.from}" required>

    <label>Đến ngày:</label>
    <input class="input" type="date" name="to" value="${r.to}" required>

    <label>Lý do nghỉ phép:</label>
    <textarea name="reason" required>${bodyReason}</textarea>

    <div class="actions">
      <!-- nút Hủy (trái) cùng màu với nút Lưu -->
      <button type="button" class="btn btn-primary" onclick="history.back()">Hủy</button>
      <!-- nút Lưu thay đổi (phải) -->
      <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
    </div>
  </form>
</div>
</body>
</html>
