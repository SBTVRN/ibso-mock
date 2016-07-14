<%@ include file="/taglibs.jsp" %>

<%--<head>--%>
    <%--<title><fmt:message key="configurationList.title"/></title>--%>
    <%--<meta name="menu" content="ConfigurationMenu"/>--%>
<%--</head>--%>

<div class="span10">
    <h2><fmt:message key="configurationList.heading"/></h2>

    <form method="get" action="${ctx}/actions" id="searchForm" class="form-search">

        <div id="search" class="input-append">
            <%--<input type="text" size="20" name="q" id="query" value="${param.q}"
                   placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
            --%>
        </div>

        <div id="actions" class="form-actions">
            <a class="btn btn-primary" href="<c:url value='/editConfiguration'/>" >
                <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
            </a>
            <a class="btn btn-info" href="<c:url value='/setImportConfigurations'/>" >
                <i class="icon-plus icon-white"></i> <fmt:message key="button.import"/>
            </a>
            <s:submit type="button" cssClass="btn btn-info" method="exportSelected" key="button.export" theme="simple">
                <i class="icon-remove"></i> <fmt:message key="button.export"/>
            </s:submit>
            <s:submit type="button" cssClass="btn btn-success" method="activateSelected" key="button.activate" theme="simple">
                <i class="icon-remove"></i> <fmt:message key="button.activate"/>
            </s:submit>
            <s:submit type="button" cssClass="btn btn-warning" method="deactivateSelected" key="button.deactivate" theme="simple">
                <i class="icon-remove"></i> <fmt:message key="button.deactivate"/>
            </s:submit>
            <s:submit type="button" cssClass="btn btn-danger" method="deleteSelected" key="button.delete" theme="simple" onclick="return confirmDeleteConfiguration('msg_confirm_delete_configurations');">
                <i class="icon-remove"></i> <fmt:message key="button.delete"/>
            </s:submit>
            <%--<s:submit type="button" cssClass="btn btn-default" method="/editConfiguration" key="button.history" theme="simple">--%>
                <%--<i class="icon-remove"></i> <fmt:message key="button.history"/>--%>
            <%--</s:submit>--%>
        </div>



        <display:table name="configurations" class="table table-condensed table-striped table-hover" requestURI="" id="configurationList" export="true" pagesize="20">

            <display:column property="id" sortable="true" href="editConfiguration" media="html" paramId="id" paramProperty="id" titleKey="configuration.id"/>
            <%--<display:column property="id" media="csv excel xml pdf" titleKey="configuration.id"/>--%>
            <display:column titleKey="configuration.select" media="html">
                <input type="checkbox" name="selectedBox" class="selectableCheckbox" id="selectedBox" value="${configurationList.id}"/>
            </display:column>
            <display:column property="activeYesNo" sortable="true" titleKey="configuration.active"/>
            <display:column property="name" sortable="true" href="editConfiguration" media="html" paramId="id" paramProperty="id" titleKey="configuration.name"/>
            <display:column property="description" sortable="true" titleKey="configuration.description"/>

            <%--Хрипушин А.В. Русификация display tag-ов--%>
            <display:setProperty name="export.banner"><div class="exportlinks"> <fmt:message key="configurationList.export.banner"/>{0} </div></display:setProperty>
            <display:setProperty name="paging.banner.item_name"><fmt:message key="configurationList.configuration"/></display:setProperty>
            <display:setProperty name="paging.banner.items_name"><fmt:message key="configurationList.configurations"/></display:setProperty>
            <display:setProperty name="paging.banner.no_items_found"><span class="pagebanner"><fmt:message key="configurationList.paging.banner.no_items_found"/>.</span></display:setProperty>
            <display:setProperty name="paging.banner.one_item_found"><span class="pagebanner"><fmt:message key="configurationList.paging.banner.records"/> 1.</span></display:setProperty>
            <display:setProperty name="paging.banner.all_items_found">
                    <span class="pagebanner"><fmt:message key="configurationList.paging.banner.records"/> {0}, <fmt:message key="configurationList.paging.banner.displaying_all"/> {2}.
                    </span>
            </display:setProperty>
            <display:setProperty name="paging.banner.some_items_found">
                    <span class="pagebanner"><fmt:message key="configurationList.paging.banner.records"/> {0},
                        <fmt:message key="configurationList.paging.banner.displaying_from"/> {2}
                        <fmt:message key="configurationList.paging.banner.displaying_to"/> {3}.
                    </span>
            </display:setProperty>
            <display:setProperty name="paging.banner.full">
                <span class="pagelinks">
                    [<a href="{1}"><fmt:message key="configurationList.paging.banner.first"/></a>
                    /<a href="{2}"><fmt:message key="configurationList.paging.banner.prev"/></a></a>]
                    {0}
                    [<a href="{3}"><fmt:message key="configurationList.paging.banner.next"/></a></a>
                    /<a href="{4}"><fmt:message key="configurationList.paging.banner.last"/></a></a>]
                </span>
            </display:setProperty>
            <display:setProperty name="paging.banner.first">
                <span class="pagelinks">
                    [<fmt:message key="configurationList.paging.banner.first"/>/<fmt:message key="configurationList.paging.banner.prev"/>] {0}
                    [<a href="{3}"><fmt:message key="configurationList.paging.banner.next"/></a>
                    /<a href="{4}"><fmt:message key="configurationList.paging.banner.last"/></a>]
                </span>
            </display:setProperty>
            <display:setProperty name="paging.banner.last">
                <span class="pagelinks">
                    [<a href="{1}"><fmt:message key="configurationList.paging.banner.first"/></a>
                    /<a href="{2}"><fmt:message key="configurationList.paging.banner.prev"/></a>]
                    {0} [<fmt:message key="configurationList.paging.banner.next"/>/<fmt:message key="configurationList.paging.banner.last"/>]
                </span>
            </display:setProperty>
            <display:setProperty name="paging.banner.page.link"><a href="{1}" title=<fmt:message key="configurationList.paging.banner.link"/> {0}>{0}</a></display:setProperty>
            <display:setProperty name="basic.msg.empty_list"><fmt:message key="configurationList.basic.msg.empty_list"/></display:setProperty>


            <%--<display:setProperty name="export.excel.filename"><fmt:message key="configurationList.title"/>.xls</display:setProperty>--%>
            <%--<display:setProperty name="export.csv.filename"><fmt:message key="configurationList.title"/>.csv</display:setProperty>--%>
            <%--<display:setProperty name="export.pdf.filename"><fmt:message key="configurationList.title"/>.pdf</display:setProperty>--%>
        </display:table>



    </form>

</div>
