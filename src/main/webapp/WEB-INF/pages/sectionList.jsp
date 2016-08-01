<%--
  Created by RazuvaevSV
  Date: 13.07.2016
--%>
<%@ include file="/taglibs.jsp" %>

<%--<head>--%>
<%--<title><fmt:message key="configurationList.title"/></title>--%>
<%--<meta name="menu" content="ConfigurationMenu"/>--%>
<%--</head>--%>

<div class="span10">
    <h2><fmt:message key="sectionList.heading"/></h2>

    <s:form action="search" cssClass="form-search" theme="simple">
        <s:textfield name="query" id="search-field" theme="simple" />
        <s:submit action="search" cssClass="btn btn-success" theme="simple" key="button.search" />
    </s:form>

    <form method="get" action="sectionActions" id="searchForm" class="form-search">

        <%--<div id="search" class="input-append">--%>
            <%--<input type="text" size="20" name="q" id="query" value="${param.q}"--%>
               <%--placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>--%>
        <%--</div>--%>

        <table width="100%">
            <tr>
                <td>
                    <div id="sectionActions" class="form-actions">
                        <s:url var="createNewSectionLink" action="editSection" />
                        <s:a href="%{createNewSectionLink}" cssClass="btn btn-primary" type="button" theme="simple">
                            <fmt:message key="button.add"/>
                        </s:a>

                        <%--<s:submit method="deleteSelected" cssClass="btn btn-danger" type="button" theme="simple" key="button.delete">--%>
                            <%--<fmt:message key="button.delete"/>--%>
                        <%--</s:submit>--%>

                        <s:submit theme="simple" key="button.delete" cssClass="btn btn-danger"  method="deleteSelected" onclick="return confirmDeleteSection()" />
                    </div>
                </td>
                <td align="right">
                    <s:url var="configurationsWithPageSize10" action="sections">
                        <s:param name="pageSize" value="10" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSize10}" >
                        <fmt:message key="button.10"/>
                    </s:a>
                    <s:url var="configurationsWithPageSize20" action="sections">
                        <s:param name="pageSize" value="20" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSize20}" >
                        <fmt:message key="button.20"/>
                    </s:a>
                    <s:url var="configurationsWithPageSizeAll" action="sections">
                        <s:param name="pageSize" value="99999" />
                    </s:url>
                    <s:a theme="simple" id="page-size-link" href="%{configurationsWithPageSizeAll}" >
                        <fmt:message key="button.All"/>
                    </s:a>
                </td>
            </tr>
        </table>

        <!-- У каждого элемента должно быть две ссылки и чекбокс. Выделить / Зайти в раздел / редактировать  -->
        <display:table name="sections" class="table table-condensed table-striped table-hover" requestURI="" id="sectionList" export="false" pagesize='${pageSize}'>

            <%--<display:column property="id" sortable="true" href="editSection" media="html" paramId="id" paramProperty="id" titleKey="section.id"/>--%>
            <display:column titleKey="section.select" media="html" >
                <input type="checkbox" class="selectableCheckbox" name="sectionSelectedBox" id="sectionSelectedBox" value="${sectionList.id}"/>
            </display:column>
            <display:column titleKey="section.count" media="html" sortable="false" property="configurationsSize" />
            <display:column titleKey="section.name" media="html" sortable="true" href="configurations" property="name" paramId="parentSectionId" paramProperty="id" />
            <display:column titleKey="section.description" sortable="true" property="description" />
            <display:column titleKey="section.edit" media="html" sortable="false" href="editSection" paramId="sectionId" paramProperty="id"  >
                <fmt:message key="section.edit"/>
            </display:column>

            <%--<display:column sortable="true" href="editSection" media="html" paramId="id" paramProperty="id" titleKey="section.edit"/>--%>

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
