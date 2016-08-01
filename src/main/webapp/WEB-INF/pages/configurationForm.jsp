<%@ include file="/taglibs.jsp" %>

<head>
    <title><fmt:message key="configurationDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='configurationDetail.heading'/>"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="configurationList.configuration"/></c:set>

<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";

$(function() {
    var it = $('#input_table');
    $('#add_input_param').on('click', function() {
        var t = $('#input_table tr:eq(1)').clone(true).appendTo(it);
        $(t).find('[name*="msgConfiguration"]').each( function() {
            this.name = this.name.replace('0','' + $('#input_table tr').size() - 1);
            this.value = '';
            console.log(this.name);
        });
        i++;
        console.log("i=" + i);
    });
    var ot = $('#output_table');
    $('#add_output_param').on('click', function() {
        var t = $('#output_table tr:eq(1)').clone(true).appendTo(ot);
        $(t).find('[name*="msgConfiguration"]').each( function() {
            this.name = this.name.replace('0','' + $('#output_table tr').size() - 1);
            this.value = '';
            console.log(this.name);
        });
        o++;
    });
});
function reindexInputParameters() {
    for (var ii = 1; ii < $('#input_table tr').size(); ii++) {
        $('#input_table tr').eq(ii).find('[name*="msgConfiguration"]').each(function () {
            this.name = this.name.replace(/\d/, '' + ii - 1);
        });
        $('#input_table tr').eq(ii).find('[id*="msgConfiguration"]').each(function () {
            this.attr("id", this.id.replace(/\d/, '' + ii - 1));
        });
    }
}
function reindexOutputParameters() {
    for (var ii = 1; ii < $('#output_table tr').size(); ii++) {
        $('#output_table tr').eq(ii).find('[name*="msgConfiguration"]').each(function () {
            this.name = this.name.replace(/\d/, '' + ii - 1);
        });
        $('#output_table tr').eq(ii).find('[id*="msgConfiguration"]').each(function () {
            this.attr("id", this.id.replace(/\d/, '' + ii - 1));
        });
    }
}

</script>

<h2>
    <fmt:message key="configurationDetail.heading"/>
    <s:property value="name" />
</h2>


<%--<s:a href="configurations" name="session" value="session">--%>
    <%--<fmt:message key="button.back"/>--%>
<%--</s:a>--%>

<%--<s:submit type="button" cssClass="btn btn-primary" src="configurationList.jsp" method="listConfigurationsBySection" key="button.back" theme="simple" name="session" value="session">--%>
    <%--<fmt:message key="button.back"/>--%>
<%--</s:submit>--%>


<div id="backButton" class="form-actions">
    <s:url var="backLink" action="backFromEditConfiguration">
        <s:param name="parentSectionId" value="parentSection.id" />
        <c:if test="${not empty query}">
            <s:param name="query" value="query" />
        </c:if>
    </s:url>
    <s:a href="%{backLink}" cssClass="btn btn-primary" type="button" theme="simple">
        <fmt:message key="button.back"/>
    </s:a>
</div>
<br />

<s:form id="configurationForm" theme="bootstrap" action="saveConfiguration" method="post" cssClass="form-horizontal well" validate="true">

    <s:hidden name="parentSectionId" value="%{parentSectionId}" />

    <s:hidden key="configuration.id" name="msgConfiguration.id"/>
    <%--<s:hidden key="configuration.sectionId" name="messageConfiguration.sectionId"/>--%>
    <s:textfield key="configuration.name" name="msgConfiguration.name" />
    <s:textfield key="configuration.parentSection" name="msgConfiguration.section.id" />
    <s:textarea cols="60" rows="1" key="configuration.description" name="msgConfiguration.description"/>

    <br>
    <button id="add_input_param" type="button" class="btn btn-primary"><fmt:message key="configuration.addInputParameter"/></button>
    <table id="input_table" class="table table-bordered">
        <tr>
            <th style="width:15%;"><fmt:message key="configuration.ParameterName"/></th>
            <th><fmt:message key="configuration.ParameterValue"/></th>
            <th></th>
        </tr>
        <s:iterator value="msgConfiguration.inputParameterList" status="status" var="param">
            <tr id="input_param">
                <td><s:textfield value="%{#param.name}" name="msgConfiguration.inputParameterList[%{#status.index}].name"/></td>
                <td><s:textarea cols="60" rows="1" value="%{#param.value}" name="msgConfiguration.inputParameterList[%{#status.index}].value"/></td>
                <td style="width: 60%">
                    <button type="button" class="btn btn-warning" onclick="
                       if ( $('#input_table tr').size() > 2 ) {
                           $(this).parent().parent().remove();
                           reindexInputParameters();
                       } else {
                           $(this).parent().parent().find('[name*=\'msgConfiguration\']').each( function() {
                               this.value = '';
                           });
                       }
                    ">
                        <fmt:message key="configuration.removeInputParameter"/>
                    </button>
                </td>
            </tr>
        </s:iterator>
    </table>

    <button id="add_output_param" type="button" class="btn btn-primary"><fmt:message key="configuration.addOutputParameter"/></button>
    <table id="output_table" class="table table-bordered">
        <tr>
            <th style="width:15%;"><fmt:message key="configuration.ParameterName"/></th>
            <th><fmt:message key="configuration.ParameterValue"/></th>
            <th></th>
        </tr>
        <s:iterator value="msgConfiguration.outputParameterList" status="stat" var="param">
            <tr>
                <td><s:textfield value="%{#param.name}" name="msgConfiguration.outputParameterList[%{#stat.index}].name"/></td>
                <td><s:textarea cols="60" rows="1" value="%{#param.value}" name="msgConfiguration.outputParameterList[%{#stat.index}].value"/></td>
                <td style="width: 60%">
                    <button type="button" class="btn btn-warning" onclick="
                       if ( $('#output_table tr').size() > 2 ) {
                           $(this).parent().parent().remove();
                           reindexOutputParameters();
                       } else {
                           $(this).parent().parent().find('[name*=\'msgConfiguration\']').each( function() {
                               this.value = '';
                           });
                       }
                    ">
                        <fmt:message key="configuration.removeOutputParameter"/>
                    </button>
                </td>
            </tr>
        </s:iterator>
    </table>

    <div>
    <s:textarea cols="80" rows="20" key="configuration.messageTemplate" name="msgConfiguration.messageTemplate" required="false" />
    </div>

    <br>
    <div id="actions" class="form-actions">
        <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
            <fmt:message key="button.save"/>
        </s:submit>
        <c:if test="${not empty msgConfiguration.id}">
            <s:submit type="button" cssClass="btn btn-warning" method="delete" key="button.delete" onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                <fmt:message key="button.delete"/>
            </s:submit>
        </c:if>
        <s:url var="cancelLink" action="configurations">
            <s:param name="parentSectionId" value="parentSection.id" />
        </s:url>
        <s:a href="%{cancelLink}" cssClass="btn btn-primary" type="button" theme="simple">
            <fmt:message key="button.cancel"/>
        </s:a>
    </div>
</s:form>

<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['configurationForm']).focus();
    });
</script>
