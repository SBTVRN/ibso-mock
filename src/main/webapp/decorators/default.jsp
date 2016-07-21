<%--<%@ page contentType="text/html;charset=UTF-8"  pageEncoding="UTF-8"%>--%>
<%@ include file="/taglibs.jsp" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <title><%--<decorator:title default="Welcome"/> | --%><fmt:message key="webapp.name"/></title>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="${ctx}/images/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${ctx}/webjars/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/styles/app.css">
    <script type="text/javascript" src="${ctx}/webjars/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/webjars/jquery/jquery.i18n.properties.min.js"></script>
    <script type="text/javascript" src="${ctx}/webjars/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/app.js"></script>
    <decorator:head/>
</head>

<body
    <decorator:getProperty property="body.id" writeEntireProperty="true"/>
    <decorator:getProperty property="body.class" writeEntireProperty="true"/>
>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span7">
            <%@ include file="/messages.jsp"%>
            <decorator:body/>
            <decorator:getProperty property="page.underground"/>
        </div>

    </div>
</div>

<div id="footer">
    <p>
        <fmt:message key="default.footer"/>
    </p>
</div>

</body>
</html>
