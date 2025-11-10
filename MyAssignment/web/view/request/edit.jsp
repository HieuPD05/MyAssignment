<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Sửa đơn nghỉ</title>
<style>
.container{max-width:880px;margin:18px auto;padding:0 14px}
h1{margin:14px 0}
form{background:#fff;border:1px solid #e5e7eb;border-radius:10px;padding:16px}
.row{display:grid;grid-template-columns:160px 1fr;gap:10px;align-items:center;margin:8px 0}
input,select,textarea{padding:10px;border:1px solid #cbd5e1;border-radius:8px}
.btn{padding:10px 14px;border:none;border-radius:8px;background:#1f2937;color:#fff;cursor:pointer}
.error{color:#dc2626;margin:8px 0 0 0}
</style>
</head>
<body>
<jsp:include page="/view/common/topbar.jspf"/>
<div class="container">
  <h1>Sửa đơn #${r.rid}</h1>
  <form method="post" action="${pageContext.request.contextPath}/request/edit">
    <input type="hidden" name="rid" value="${r.rid}">
    <div class="row">
      <label>Từ ngày</label>
      <input type="date" name="from"
             value="${not empty stick_from ? stick_from : r.from}" required>
    </div>
    <div class="row">
      <label>Đến ngày</label>
      <input type="date" name="to"
             value="${not empty stick_to ? stick_to : r.to}" required>
    </div>
    <div class="row">
      <label>Loại nghỉ</label>
      <select name="ltid" required>
        <c:set var="currentLtid" value="${not empty stick_ltid ? stick_ltid : r.ltid}" />
        <option value="1" ${currentLtid=='1' || currentLtid==1 ? 'selected':''}>Nghỉ phép năm</option>
        <option value="2" ${currentLtid=='2' || currentLtid==2 ? 'selected':''}>Nghỉ không lương</option>
        <option value="3" ${currentLtid=='3' || currentLtid==3 ? 'selected':''}>Nghỉ thai sản</option>
        <option value="4" ${currentLtid=='4' || currentLtid==4 ? 'selected':''}>Khác</option>
      </select>
    </div>
    <div class="row">
      <label>Lý do</label>
      <textarea name="reason" rows="3">${not empty stick_reason ? stick_reason : r.reason}</textarea>
    </div>
    <div class="row"><label></label><button class="btn" type="submit">Lưu</button></div>
    <c:if test="${not empty error}">
      <div class="row"><label></label><div class="error">${error}</div></div>
    </c:if>
  </form>
</div>
</body>
</html>
