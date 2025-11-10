<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.topbar{display:flex;align-items:center;justify-content:space-between;padding:10px 14px;border-bottom:1px solid #e5e7eb;background:#fff;position:sticky;top:0;z-index:99}
.brand{font-weight:700;color:#1f2937}
.nav a{margin-left:14px;text-decoration:none;color:#374151}
.bell{position:relative;display:inline-block;margin-left:14px}
.bell a{font-size:18px;text-decoration:none}
.badge{position:absolute;top:-6px;right:-8px;background:#ef4444;color:#fff;border-radius:999px;padding:0 6px;font-size:11px;line-height:18px;min-width:18px;text-align:center}
.user{color:#374151;font-size:14px}
</style>
<div class="topbar">
  <div class="brand">HP_CT</div>
  <div class="nav">
    <a href="${pageContext.request.contextPath}/home">Home</a>
    <a href="${pageContext.request.contextPath}/request/create">Create</a>
    <a href="${pageContext.request.contextPath}/request/list">My Requests</a>
    <a href="${pageContext.request.contextPath}/division/agenda">Agenda</a>
    <a href="${pageContext.request.contextPath}/hr/employee">HR</a>
    <a href="${pageContext.request.contextPath}/admin/account">Admin</a>

    <span class="bell">
      <a href="${pageContext.request.contextPath}/notification/list" title="Thông báo">?</a>
      <c:if test="${not empty requestScope.notiCount && requestScope.notiCount > 0}">
        <span class="badge">${requestScope.notiCount}</span>
      </c:if>
    </span>
    <span class="user">
      <c:if test="${not empty sessionScope.auth}">
        ${sessionScope.auth.displayname}
      </c:if>
      &nbsp;| <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </span>
  </div>
</div>
