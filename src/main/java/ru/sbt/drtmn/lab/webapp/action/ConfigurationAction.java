package ru.sbt.drtmn.lab.webapp.action;

import com.opensymphony.xwork2.Preparable;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.model.Section;
import ru.sbt.drtmn.lab.service.ConfigurationManager;
import ru.sbt.drtmn.lab.dao.SearchException;
import ru.sbt.drtmn.lab.service.SectionManager;
import ru.sbt.drtmn.lab.util.FileUtil;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ConfigurationAction extends GenericAction implements Preparable {
    private static transient Logger logger = Logger.getLogger(GenericAction.class);
    // Variables for imported file
    private File fileUpload;
    private String fileUploadContentType;
    private String fileUploadFileName;
    // Variable for exported file
    private File exportedFile;
    // Variables for work with Section
    private SectionManager sectionManager;
    private String pageTitle;
    private Long parentSectionId;
    private Section parentSection;
    // Variables for work with Configuration
    private ConfigurationManager configurationManager;
    private List configurations;
    private Long id;
    private Configuration configuration;
    private List<Long> selectedBox;
    private String query;
    // Variable for paginator
    private Integer pageSize;

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String configurationId = getRequest().getParameter("configuration.id");
            if (configurationId != null && !configurationId.equals("")) {
                configuration = (Configuration)configurationManager.get(new Long(configurationId));
            }
        }
        if (getSession().getAttribute("pageSize") == null) {
            this.pageSize = 20;
            getSession().setAttribute("pageSize", this.pageSize);
        } else {
            this.pageSize = (Integer)getSession().getAttribute("pageSize");
        }
    }

    public String listConfigurationsBySearch() {
        try {
            configurations = configurationManager.search(query, Configuration.class);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = configurationManager.getAll();
        }
        return INPUT;
    }

    public String listConfigurationsBySection() {
        logger.debug("Get configurations for parentSectionId = " + parentSectionId);
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
            pageTitle = parentSection.getName();
            configurations = sectionManager.getSectionConfigurations(parentSection);
            logger.debug("Find: " + configurations.size() + " configurations for Section: " + pageTitle);
        } catch (Exception e) {
            addActionError(e.getMessage());
            configurations = new ArrayList<Configuration>();
        }
        return SUCCESS;
    }

    public String back() {
        if ( query != null ) {
            return listConfigurationsBySearch();
        } else {
            return listConfigurationsBySection();
        }
    }

    public String activateSelected() {
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
            pageTitle = parentSection.getName();
            Integer count  = configurationManager.activateMsgConfigurations(selectedBox);
            configurations = sectionManager.getSectionConfigurations(parentSection);
            logger.debug("Activated: " + count + " configurations");
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = sectionManager.getSectionConfigurations(parentSection);
        }
        return SUCCESS;
    }

    public String deactivateSelected() {
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
            pageTitle = parentSection.getName();
            Integer count  = configurationManager.deactivateMsgConfigurations(selectedBox);
            configurations = sectionManager.getSectionConfigurations(parentSection);
            logger.debug("Deactivated: " + count + " configurations");
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = sectionManager.getSectionConfigurations(parentSection);
        }
        return SUCCESS;
    }

    public String deleteSelected() {
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
            pageTitle = parentSection.getName();
            Integer count = configurationManager.deleteMsgConfigurations(selectedBox);
            configurations = sectionManager.getSectionConfigurations(parentSection);
            logger.debug("Deleted: " + count + " configurations");
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = sectionManager.getSectionConfigurations(parentSection);
        }
        return SUCCESS;
    }

    public String delete() {
        parentSection = configuration.getSection();
        pageTitle = parentSection.getName();
        parentSection.removeConfiguration(configuration);
        configurationManager.remove(configuration.getId());
        configurations = sectionManager.getSectionConfigurations(parentSection);
        saveMessage(getText("configuration.deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (id != null) {
            configuration = (Configuration)configurationManager.get(id);
            parentSection = configuration.getSection();
            pageTitle = parentSection.getName();
        } else {
            parentSection = sectionManager.get(parentSectionId);
            pageTitle = parentSection.getName();
            configuration = new Configuration();
            configuration.setSection(parentSection);
        }
        if (configuration.getInputParameterSize() == 0) {
            configuration.addInputParam("","");
        }
        if (configuration.getOutputParameterSize() == 0) {
            configuration.addOutputParam("", "");
        }
        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) { return "cancel"; }
        if (delete != null) { return delete(); }
        boolean isNew = (configuration.getId() == null);
        configuration.setActive(true);
        configurationManager.save(configuration);
        parentSection = configuration.getSection();
        pageTitle = parentSection.getName();
        configurations = sectionManager.getSectionConfigurations(parentSection);
        String key = (isNew) ? "Configuration [" + configuration.getName() + "] added" : "Configuration [" + configuration.getName() + "] updated";
        saveMessage(getText(key));
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }

    public String setImportConfiguration() {
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
            logger.debug("Configurations will be imported into Section: " + parentSection.getName());
        } catch (SearchException se) {
            addActionError(se.getMessage());
        }
        return SUCCESS;
    }

    public String importAllWithReplace() {
        List<File> uploadedFiles = null;
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
        } catch (SearchException se) {
            addActionError(se.getMessage());
        }
        try {
            uploadedFiles = FileUtil.processUploadedFile(fileUpload, fileUploadFileName);
        } catch (Exception e) {
            saveMessage(e.getMessage());
            logger.error("Error when process uploaded file", e);
        }
        if ( uploadedFiles != null ) {
            for (File file : uploadedFiles) {
                Configuration configuration = FileUtil.importConfigurationFromFile(file);
                configuration.setSection(parentSection);
                logger.debug(configuration);
                Boolean result = configurationManager.importMsgConfiguration(configuration);
                if ( result ) {
                    saveMessage("Imported: " + configuration.getName());
                } else {
                    saveMessage("Not imported: " + configuration.getName());
                }
            }
        }
        return SUCCESS;
    }

    public String importOnlyFresh() {
        List<File> uploadedFiles = null;
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
        } catch (SearchException se) {
            addActionError(se.getMessage());
        }
        try {
            uploadedFiles = FileUtil.processUploadedFile(fileUpload, fileUploadFileName);
        } catch (Exception e) {
            saveMessage(e.getMessage());
            logger.error("Error when process uploaded file", e);
        }
        if ( uploadedFiles != null ) {
            for (File file : uploadedFiles) {
                Configuration configuration = FileUtil.importConfigurationFromFile(file);
                configuration.setSection(parentSection);
                logger.debug(configuration);
                Boolean result = configurationManager.mergeMsgConfiguration(configuration);
                if ( result ) {
                    saveMessage("Imported: " + configuration.getName());
                } else {
                    saveMessage("Not imported: " + configuration.getName());
                }
            }
        }
        return SUCCESS;
    }

    public String exportSelected() {
        try {
            parentSection = (Section)sectionManager.get(new Long(parentSectionId));
        } catch (SearchException se) {
            addActionError(se.getMessage());
        }
        try {
            exportedFile = configurationManager.exportMsgConfigurations(selectedBox);
            if ( ( exportedFile == null ) && ( selectedBox == null ) ) {
                saveMessage(getText("configuration.nodataforexport"));
            } else if ( ( exportedFile == null ) && ( selectedBox.size() > 0 ) ) {
                saveMessage(getText("configuration.errorwhenexport"));
            }  else {
                logger.debug("Export File: " + exportedFile);
                //return an application file instead of html page
                getResponse().setContentType("application/octet-stream");
                getResponse().setHeader("Content-Disposition","attachment;filename=" + exportedFile.getName());
                FileInputStream in = null;
                ServletOutputStream out = null;
                try {
                    //Get it from file system
                    in = new FileInputStream(exportedFile);
                    out = getResponse().getOutputStream();
                    byte[] outputByte = new byte[4096];
                    //copy binary content to output stream
                    while( in.read(outputByte, 0, 4096) != -1 ) {
                        out.write(outputByte, 0, 4096);
                    }
                    logger.debug("Export File: " + exportedFile + " ...complete");
                } catch(IOException ioe) {
                    logger.error("Error when transferring file to the user: " + exportedFile, ioe);
                } finally {
                    try {  in.close(); } catch (IOException e) { logger.warn("Error when closing FileInputStream: " + exportedFile, e); }
                    try { out.flush(); } catch (IOException e) { logger.warn("Error when flushing: " + exportedFile, e);  }
                    try { out.close(); } catch (IOException e) { logger.warn("Error when closing ServletOutputStream: " + exportedFile, e); }
                }
            }
        } catch (SearchException se) {
            addActionError(se.getMessage());
        }
        return SUCCESS;
    }

    // Getters and Setters


    public File getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getFileUploadContentType() {
        return fileUploadContentType;
    }

    public void setFileUploadContentType(String fileUploadContentType) {
        this.fileUploadContentType = fileUploadContentType;
    }

    public String getFileUploadFileName() {
        return fileUploadFileName;
    }

    public void setFileUploadFileName(String fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }

    public File getExportedFile() {
        return exportedFile;
    }

    public void setExportedFile(File exportedFile) {
        this.exportedFile = exportedFile;
    }

    public SectionManager getSectionManager() {
        return sectionManager;
    }

    public void setSectionManager(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Long getParentSectionId() {
        return parentSectionId;
    }

    public void setParentSectionId(Long parentSectionId) {
        this.parentSectionId = parentSectionId;
    }

    public Section getParentSection() {
        return parentSection;
    }

    public void setParentSection(Section parentSection) {
        this.parentSection = parentSection;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public List getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List configurations) {
        this.configurations = configurations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMsgConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getMsgConfiguration() {
        return configuration;
    }

    public List<Long> getSelectedBox() {
        if (selectedBox == null) {
            selectedBox = new ArrayList<Long>();
        }
        return selectedBox;
    }

    public void setSelectedBox(List<Long> selectedBox) {
        this.selectedBox = selectedBox;
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