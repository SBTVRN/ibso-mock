package ru.sbt.drtmn.lab.webapp.action;

import com.opensymphony.xwork2.Preparable;
import org.apache.log4j.Logger;
import ru.sbt.drtmn.lab.dao.SearchException;
import ru.sbt.drtmn.lab.model.Section;
import ru.sbt.drtmn.lab.service.SectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 13.07.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *         Action class for sections.
 */
public class SectionAction extends GenericAction implements Preparable {
    private static transient Logger logger = Logger.getLogger(GenericAction.class);

    private SectionManager sectionManager;
    private List sections;
    private Section currentSection;
    private Long sectionId;
    private List<Long> sectionSelectedBox;
    private String query;
    // Variable for paginator
    private Integer pageSize;

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String sectionId = getRequest().getParameter("section.id");
            if (sectionId != null && !sectionId.equals("")) {
                currentSection = (Section)sectionManager.get(new Long(sectionId));
            }
        }
        if (getSession().getAttribute("pageSize") == null) {
            this.pageSize = 20;
            getSession().setAttribute("pageSize", this.pageSize);
        } else {
            this.pageSize = (Integer)getSession().getAttribute("pageSize");
        }
    }

    public String listSections() {
        try {
            sections = sectionManager.getAllDistinct();
        } catch (SearchException se) {
            addActionError(se.getMessage());
        }
        return SUCCESS;
    }

    public String deleteSelected() {
        try {
            Integer count = sectionManager.deleteSections(sectionSelectedBox);
            logger.debug("Deleted: " + count + " section(s)");
        } catch (SearchException se) {
            addActionError(se.getMessage());
            sections = sectionManager.getAllDistinct();
        }
        return SUCCESS;
    }

    public String delete() {
        sectionManager.remove(currentSection.getId());
        saveMessage(getText("Section [" + currentSection.getName() + "] deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (sectionId != null) {
            currentSection = (Section)sectionManager.get(sectionId);
        } else {
            currentSection = new Section();
        }
        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) { return "cancel"; }
        if (delete != null) { return delete(); }
        boolean isNew = (currentSection.getId() == null);
        String key = (isNew) ? "Section " + currentSection.getName() + " added" : "Section " + currentSection.getName() + " updated";
        saveMessage(getText(key));
        if (!isNew) {
            sectionManager.update(currentSection);
            return INPUT;
        } else {
            sectionManager.save(currentSection);
            return SUCCESS;
        }
    }

    // Getters and Setters

    public Section getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(Section currentSection) {
        this.currentSection = currentSection;
    }

    public List getSections() {
        return sections;
    }

    public void setSections(List sections) {
        this.sections = sections;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public List<Long> getSectionSelectedBox() {
        if (sectionSelectedBox == null) {
            sectionSelectedBox = new ArrayList<Long>();
        }
        return sectionSelectedBox;
    }

    public void setSectionSelectedBox(List<Long> sectionSelectedBox) {
        this.sectionSelectedBox = sectionSelectedBox;
    }

    public SectionManager getSectionManager() {
        return sectionManager;
    }

    public void setSectionManager(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        getSession().setAttribute("pageSize", this.pageSize);
    }
}
