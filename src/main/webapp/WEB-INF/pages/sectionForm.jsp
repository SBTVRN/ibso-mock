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

$(function() {

    var it = $('#input_table');
    var i = $('#input_table tr').size() - 1;
    $('#add_input_param').on('click', function() {
        var t = $('#input_table tr:eq(1)').clone(true).appendTo(it);
        $(t).find('[name*="message"]').each( function() {
            this.name = this.name.replace('0','' + i);
            this.value = '';
            console.log(this.name);
        });
        i++;
        console.log("i=" + i);
    });
    $('#remove_input_param').on('click', function () {
        console.log("i=" + i);
        if (i > 1) {
            $('#input_table tr:last').remove();
            i--;
        }
    });;

    var ot = $('#output_table');
    var o = $('#output_table tr').size() - 1;
    $('#add_output_param').on('click', function() {
        var t = $('#output_table tr:eq(1)').clone(true).appendTo(ot);
        $(t).find('[name*="message"]').each( function() {
            this.name = this.name.replace('0','' + o);
            this.value = '';
            console.log(this.name);
        });
        o++;
    });
    $('#remove_output_param').on('click', function () {
        if (o > 1) {
            $('#output_table tr:last').remove();
            o--;
        }
    });;
});

</script>

<h2><fmt:message key="sectionDetail.heading"/></h2>


<div id="sectionActions" class="form-actions">
    <a class="btn btn-primary" href="<c:url value='/sections' />" >
        <i class="icon-plus icon-white"></i><fmt:message key="button.back"/>
    </a>
</div>
<br />


<s:form id="sectionForm"  theme="bootstrap" action="saveSection" method="post" cssClass="form-horizontal well" validate="true">

    <s:hidden key="section.id" name="currentSection.id"/>
    <s:textfield key="section.name" name="currentSection.name" />
    <s:textarea cols="60" rows="1" key="section.description" name="currentSection.description"/>


    <br>
    <div id="sectionActions" class="form-actions">
        <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
        </s:submit>
        <c:if test="${not empty currentSection.id}">
            <s:submit type="button" cssClass="btn btn-warning" method="delete" key="button.delete"
                      onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                <i class="icon-trash icon-white"></i> <fmt:message key="button.delete"/>
            </s:submit>
        </c:if>
        <s:submit type="button" cssClass="btn" method="cancel" key="button.cancel" theme="simple">
            <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
        </s:submit>
    </div>
</s:form>

<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['sectionForm']).focus();
    });
</script>