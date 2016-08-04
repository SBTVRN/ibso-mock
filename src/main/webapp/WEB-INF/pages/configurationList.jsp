<%@ include file="/taglibs.jsp" %>

<%--<head>--%>
    <%--<title><fmt:message key="configurationList.title"/></title>--%>
    <%--<meta name="menu" content="ConfigurationMenu"/>--%>
<%--</head>--%>

<script type="text/javascript">var msgDelConfirm = "<fmt:message key="delete.confirm.configurations"/>"</script>

<div class="span10">
    <h2>
        <s:property value="pageTitle" />
    </h2>

    <s:form action="configurationActions" id="searchForm" class="form-search" >


         <%--<div id="search" class="input-append">--%>
            <%--<input type="text" size="20" name="q" id="query" value="${param.q}"
                   placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
            --%>
        <%--</div>--%>

        <s:hidden name="parentSectionId" value="%{parentSectionId}" />

        <table width="100%">
            <tr>
                <td>
                    <div id="actions" class="form-actions">
                        <s:a theme="simple" cssClass="btn btn-primary" type="button" action="sections">
                            <fmt:message key="button.back"/>
                        </s:a>

                        <s:url var="createNewConfigurationLink" action="editConfiguration">
                            <s:param name="parentSectionId" value="parentSectionId" />
                        </s:url>
                        <s:a theme="simple" cssClass="btn btn-link" type="button" href="%{createNewConfigurationLink}" >
                            <fmt:message key="button.add"/>
                        </s:a>

                        <s:url var="importConfigurationLink" action="setImportConfigurations">
                            <s:param name="parentSectionId" value="parentSectionId" />
                        </s:url>
                        <s:a theme="simple" cssClass="btn btn-info" type="button" href="%{importConfigurationLink}">
                            <fmt:message key="button.import"/>
                        </s:a>

                        <s:submit id="buttonExport" theme="simple" key="button.export" cssClass="btn btn-info" method="exportSelected" disabled="true"/>

                        <s:submit id="buttonActivate" theme="simple" key="button.activate" cssClass="btn btn-success" method="activateSelected" disabled="true"/>

                        <s:submit id="buttonDeactivate" theme="simple" key="button.deactivate" cssClass="btn btn-warning" method="deactivateSelected" disabled="true"/>

                        <s:submit id="buttonDelete" theme="simple" key="button.delete" cssClass="btn btn-danger"
                                  method="deleteSelected" onclick="return confirmMessage(msgDelConfirm)" disabled="true"/>
                    </div>
                </td>
                <td align="right">
                    <s:url var="configurationsWithPageSize10" action="configurations">
                        <s:param name="parentSectionId" value="parentSectionId" />
                        <s:param name="pageSize" value="10" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSize10}" >
                        <fmt:message key="button.10"/>
                    </s:a>
                    <s:url var="configurationsWithPageSize20" action="configurations">
                        <s:param name="parentSectionId" value="parentSectionId" />
                        <s:param name="pageSize" value="20" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSize20}" >
                        <fmt:message key="button.20"/>
                    </s:a>
                    <s:url var="configurationsWithPageSizeAll" action="configurations">
                        <s:param name="parentSectionId" value="parentSectionId" />
                        <s:param name="pageSize" value="99999" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSizeAll}" >
                        <fmt:message key="button.All"/>
                    </s:a>
                </td>
            </tr>
        </table>

        <display:table name="configurations" class="table table-condensed table-striped table-hover" requestURI="" id="configurationList" export="false" pagesize='${pageSize}'>

            <%--<display:column property="id" sortable="true" href="editConfiguration" media="html" paramId="id" paramProperty="id" titleKey="configuration.id"/>--%>
            <%--<display:column property="id" media="csv excel xml pdf" titleKey="configuration.id"/>--%>
            <display:column title="<input type='checkbox' id='allChecker' onchange='checkAll(); checkboxProcessor()'/>" media="html">
                <input type="checkbox" name="selectedBox" class="selectableCheckbox" id="selectedBox" value="${configurationList.id}" onchange="checkboxProcessor()"/>
            </display:column>
            <display:column property="activeYesNo" sortable="true" titleKey="configuration.active"/>
            <%--<display:column property="name" sortable="true" href="editConfiguration" media="html" paramId="id" paramProperty="id" titleKey="configuration.name">--%>
            <display:column property="name" sortable="true" href="editConfiguration" media="html" titleKey="configuration.name" paramId="id" paramProperty="id"/>
                <%--<s:param name="id" value="id"/>--%>
            <%--</display:column>--%>
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

        </display:table>

    </s:form>

</div>
