<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thông báo</title>
<style>
.container{max-width:900px;margin:18px auto;padding:0 14px}
.card{border:1px solid #e5e7eb;border-radius:10px;background:#fff;margin:8px 0;padding:12px}
.meta{color:#6b7280;font-size:12px}
a.link{color:#2563eb;text-decoration:none}
.empty{color:#6b7280}
</style>
</head>
<body>
<jsp:include page="/view/common/topbar.jspf"/>
<div class="container">
  <h1>Thông báo</h1>
  <c:forEach items="${notis}" var="n">
    <div class="card">
      <div><b>${n.title}</b></div>
      <div class="meta">${n.createdAt}</div>
      <c:if test="${not empty n.content}"><div>${n.content}</div></c:if>
      <c:if test="${not empty n.linkUrl}">
        <div><a class="link" href="${pageContext.request.contextPath}${n.linkUrl}">Mở trang xử lý →</a></div>
      </c:if>
    </div>
  </c:forEach>
  <c:if test="${empty notis}">
    <div class="empty">Không có thông báo.</div>
  </c:if>
</div>
</body>
</html>
