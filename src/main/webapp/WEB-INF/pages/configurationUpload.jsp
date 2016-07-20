<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by Razuvaev S.V.
  Date: 05.07.2016
--%>
<%@ include file="/taglibs.jsp" %>

<body>
<h2><fmt:message key="configurationUpload.heading"/></h2>

<div id="actions" class="form-actions">
    <a class="btn btn-primary" href="<c:url value='/configurations'/>" >
        <i class="icon-plus icon-white"></i><fmt:message key="button.back"/>
    </a>
</div>
<br />
<s:form action="importConfigurations" theme="bootstrap" cssClass="form-horizontal well" method="POST" enctype="multipart/form-data">

    <s:file class="btn btn-info" name="fileUpload" label="Select a File to upload" size="40" type="button" theme="simple" />

    <br />
    <s:submit type="button" cssClass="btn btn-info" value="submit" key="button.uploadfresh" theme="simple" method="importOnlyFresh">
        <i class="icon-ok icon-white"></i> <fmt:message key="button.uploadfresh"/>
    </s:submit>
    <br />
    <br />
    <s:submit type="button" cssClass="btn btn-warning" value="submit" key="button.uploadall" theme="simple" method="importAllWithReplace">
        <i class="icon-ok icon-white"></i> <fmt:message key="button.uploadall"/>
    </s:submit>

</s:form>

</body>