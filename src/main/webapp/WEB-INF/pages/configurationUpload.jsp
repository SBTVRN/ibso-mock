<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by Razuvaev S.V.
  Date: 05.07.2016
--%>
<%@ include file="/taglibs.jsp" %>

<body>
<h2>
    <fmt:message key="configurationUpload.heading"/><br/>
    <s:property value="parentSection.name"/>
</h2>

<div id="actions" class="form-actions">
    <s:url var="backLink" action="configurations">
        <s:param name="parentSectionId" value="parentSectionId" />
    </s:url>
    <s:a href="%{backLink}" cssClass="btn btn-primary" type="button" theme="simple">
        <fmt:message key="button.back"/>
    </s:a>
</div>
<br />

<s:form action="importConfigurations" theme="bootstrap" cssClass="form-horizontal well" method="POST" enctype="multipart/form-data">
    <s:hidden name="parentSectionId" value="%{parentSectionId}" />
    <s:file class="btn btn-info" name="fileUpload" label="Select a File to upload" size="40" type="button" theme="simple" />

    <br />
    <s:submit type="button" cssClass="btn btn-info" value="submit" key="button.uploadfresh" theme="simple" method="importOnlyFresh">
        <fmt:message key="button.uploadfresh"/>
    </s:submit>
    <br />
    <br />
    <s:submit type="button" cssClass="btn btn-warning" value="submit" key="button.uploadall" theme="simple" method="importAllWithReplace">
        <fmt:message key="button.uploadall"/>
    </s:submit>

</s:form>

</body>