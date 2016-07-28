<%@ include file="/taglibs.jsp" %>

<%--<head>--%>
<%--<title><fmt:message key="configurationList.title"/></title>--%>
<%--<meta name="menu" content="ConfigurationMenu"/>--%>
<%--</head>--%>

<div class="span10">
    <h2>
        <fmt:message key="search.titleResult"/>
    </h2>

    <s:form action="configurationActions" id="searchForm" class="form-search" >

        <table width="100%">
            <tr>
                <td>
                    <div id="actions" class="form-actions">
                        <s:a theme="simple" cssClass="btn btn-primary" type="button" action="sections">
                            <fmt:message key="button.back"/>
                        </s:a>
                    </div>
                </td>
                <td align="right">
                    <s:url var="configurationsWithPageSize10" action="search">
                        <s:param name="query" value="query" />
                        <s:param name="pageSize" value="10" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSize10}" >
                        <fmt:message key="button.10"/>
                    </s:a>
                    <s:url var="configurationsWithPageSize20" action="search">
                        <s:param name="query" value="query" />
                        <s:param name="pageSize" value="20" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSize20}" >
                        <fmt:message key="button.20"/>
                    </s:a>
                    <s:url var="configurationsWithPageSizeAll" action="search">
                        <s:param name="query" value="query" />
                        <s:param name="pageSize" value="99999" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSizeAll}" >
                        <fmt:message key="button.All"/>
                    </s:a>
                </td>
            </tr>
        </table>

        <%--<s:hidden name="query" value="%{query}" />--%>

        <display:table name="configurations" class="table table-condensed table-striped table-hover" requestURI="" id="configurationList" export="false" pagesize='${pageSize}'>

            <display:column titleKey="configuration.select" media="html">
                <input type="checkbox" name="selectedBox" class="selectableCheckbox" id="selectedBox" value="${configurationList.id}"/>
            </display:column>
            <display:column property="activeYesNo" sortable="true" titleKey="configuration.active"/>
            <display:column property="name" sortable="true" href="editConfiguration?query=${query}&id=${id}"  media="html" titleKey="configuration.name" paramId="id" paramProperty="id" />
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
