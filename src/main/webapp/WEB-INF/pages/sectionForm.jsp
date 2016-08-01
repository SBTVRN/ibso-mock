<%--
  Created by IntelliJ IDEA.
  User: RSG
  Date: 13.07.2016
  Time: 12:09
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/taglibs.jsp" %>

<head>
    <title><fmt:message key="sectionDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='sectionDetail.heading'/>"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="sectionList.configuration"/></c:set>
<script type="text/javascript">var msgDelConfirm =
        "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<h2><fmt:message key="sectionDetail.heading"/></h2>


<div id="backButton" class="form-actions">
    <s:a action="sections" cssClass="btn btn-primary">
        <fmt:message key="button.back" />
    </s:a>
</div>
<br />


<s:form id="sectionForm"  theme="bootstrap" action="saveSection" method="post" cssClass="form-horizontal well" validate="true">

    <s:hidden key="section.id" name="currentSection.id"/>
    <s:textfield key="section.name" name="currentSection.name" />
    <s:textarea cols="60" rows="1" key="section.description" name="currentSection.description"/>


    <br>
    <div id="sectionActions" class="form-actions">
        <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
            <fmt:message key="button.save"/>
        </s:submit>
        <c:if test="${not empty currentSection.id}">
            <s:submit type="button" cssClass="btn btn-warning" method="delete" key="button.delete"
                      onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                <fmt:message key="button.delete"/>
            </s:submit>
        </c:if>
        <s:submit type="button" cssClass="btn btn-primary" method="cancel" key="button.cancel" theme="simple">
            <fmt:message key="button.cancel"/>
        </s:submit>
    </div>
</s:form>

<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['sectionForm']).focus();
    });
</script>