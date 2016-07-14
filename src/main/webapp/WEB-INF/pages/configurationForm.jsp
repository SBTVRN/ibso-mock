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

<h2><fmt:message key="configurationDetail.heading"/></h2>


<div id="actions" class="form-actions">
    <a class="btn btn-primary" href="<c:url value='/configurations'/>" >
        <i class="icon-plus icon-white"></i><fmt:message key="button.back"/>
    </a>
</div>
<br />
<s:form id="configurationForm"  theme="bootstrap" action="saveConfiguration" method="post" cssClass="form-horizontal well" validate="true">

    <s:hidden key="configuration.id" name="messageConfiguration.id"/>
    <s:textfield key="configuration.name" name="messageConfiguration.name" />
    <s:textarea cols="60" rows="1" key="configuration.description" name="messageConfiguration.description"/>

    <br>
    <button id="add_input_param" type="button" class="btn btn-primary"><fmt:message key="configuration.addInputParameter"/></button>
    <button id="remove_input_param" type="button" class="btn btn-warning"><fmt:message key="configuration.removeInputParameter"/></button>
    <table id="input_table" class="table table-bordered">
        <tr>
            <th style="width:15%;"><fmt:message key="configuration.ParameterName"/></th>
            <th><fmt:message key="configuration.ParameterValue"/></th>
        </tr>
        <s:iterator value="messageConfiguration.inputParameterList" status="status" var="param">
            <tr id="input_param">
                <td><s:textfield value="%{#param.name}" name="messageConfiguration.inputParameterList[%{#status.index}].name"/></td>
                <td><s:textarea cols="100" rows="1" value="%{#param.value}" name="messageConfiguration.inputParameterList[%{#status.index}].value"/></td>
                <%--<td><s:textfield size="60" value="%{#param.value}" name="messageConfiguration.inputParameterList[%{#status.index}].value"/></td>--%>
            </tr>
        </s:iterator>
    </table>

    <button id="add_output_param" type="button" class="btn btn-primary"><fmt:message key="configuration.addOutputParameter"/></button>
    <button id="remove_output_param" type="button" class="btn btn-warning"><fmt:message key="configuration.removeOutputParameter"/></button>
    <table id="output_table" class="table table-bordered">
        <tr>
            <th style="width:15%;"><fmt:message key="configuration.ParameterName"/></th>
            <th><fmt:message key="configuration.ParameterValue"/></th>
        </tr>
        <s:iterator value="messageConfiguration.outputParameterList" status="stat" var="param">
            <tr>
                <td><s:textfield value="%{#param.name}" name="messageConfiguration.outputParameterList[%{#stat.index}].name"/></td>
                <td><s:textarea cols="100" rows="1" value="%{#param.value}" name="messageConfiguration.outputParameterList[%{#stat.index}].value"/></td>
            </tr>
        </s:iterator>
    </table>

    <div>
    <s:textarea cols="80" rows="20" key="configuration.messageTemplate" name="messageConfiguration.messageTemplate"  required="false" />
    </div>

    <br>
    <div id="actions" class="form-actions">
        <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
        </s:submit>
        <c:if test="${not empty messageConfiguration.id}">
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
        $("input[type='text']:visible:enabled:first", document.forms['configurationForm']).focus();
    });
</script>
