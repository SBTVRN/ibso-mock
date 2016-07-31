<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/taglibs.jsp" %>

<h3>Ошибка получения данных</h3>
<p>
    <c:out value="${requestScope.exception.message}"/>
</p>

<!--
<% 
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace();
%>
-->

<a href="<c:url value='/'/>">&#171; Назад</a>
