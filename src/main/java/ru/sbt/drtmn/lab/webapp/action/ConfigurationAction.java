package ru.sbt.drtmn.lab.webapp.action;

import com.opensymphony.xwork2.Preparable;
import org.apache.log4j.Logger;
import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.service.ConfigurationManager;
import ru.sbt.drtmn.lab.dao.SearchException;
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
    private File fileUpload;
    private String fileUploadContentType;
    private String fileUploadFileName;
    private File exportedFile;
    private ConfigurationManager configurationManager;
    private List configurations;
    private Configuration configuration;
    private Long id;
    private String query;
    private List<Long> selectedBox;
    private static String pageSize;

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String configurationId = getRequest().getParameter("configuration.id");
            if (configurationId != null && !configurationId.equals("")) {
                configuration = configurationManager.get(new Long(configurationId));
            }
        }
    }

    public String list() {
        try {
            configurations = configurationManager.search(query, Configuration.class);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = configurationManager.getAll();
        }
        return SUCCESS;
    }

    public String activateSelected() {
        try {
            Integer count  = configurationManager.activateMsgConfigurations(selectedBox);
            logger.debug("Activated: " + count);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = configurationManager.getAll();
        }
        return SUCCESS;
    }

    public String deactivateSelected() {
        try {
            Integer count  = configurationManager.deactivateMsgConfigurations(selectedBox);
            logger.debug("Deactivated: " + count);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = configurationManager.getAll();
        }
        return SUCCESS;
    }

    public String deleteSelected() {
        try {
            Integer count = configurationManager.deleteMsgConfigurations(selectedBox);
            logger.debug("Deleted: " + count);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = configurationManager.getAll();
        }
        return SUCCESS;
    }

    public String delete() {
        configurationManager.remove(configuration.getId());
        saveMessage(getText("configuration.deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (id != null) {
            configuration = configurationManager.get(id);
        } else {
            configuration = new Configuration();
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
        if (cancel != null) {
            return "cancel";
        }
        if (delete != null) {
            return delete();
        }
        boolean isNew = (configuration.getId() == null);
        configuration.setActive(true);
        configurationManager.save(configuration);
        String key = (isNew) ? "configuration.added" : "configuration.updated";
        saveMessage(getText(key));
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }

    public String importAllWithReplace() {
        List<File> uploadedFiles = null;
        try {
            uploadedFiles = FileUtil.processUploadedFile(fileUpload, fileUploadFileName);
        } catch (Exception e) {
            saveMessage(e.getMessage());
            logger.error("Error when process uploaded file", e);
        }
        if ( uploadedFiles != null ) {
            for (File file : uploadedFiles) {
                Configuration configuration = FileUtil.importConfigurationFromFile(file);
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
            uploadedFiles = FileUtil.processUploadedFile(fileUpload, fileUploadFileName);
        } catch (Exception e) {
            saveMessage(e.getMessage());
            logger.error("Error when process uploaded file", e);
        }
        if ( uploadedFiles != null ) {
            for (File file : uploadedFiles) {
                Configuration configuration = FileUtil.importConfigurationFromFile(file);
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
            exportedFile = configurationManager.exportMsgConfigurations(selectedBox);
            if ( ( exportedFile == null ) && ( selectedBox == null ) ) {
                saveMessage(getText("configuration.nodataforexport"));
                throw new SearchException(new RuntimeException("File was not generated! " + getText("configuration.nodataforexport")));
            } else if ( ( exportedFile == null ) && ( selectedBox.size() > 0 ) ) {
                saveMessage(getText("configuration.errorwhenexport"));
                throw new SearchException(new RuntimeException("File was not generated! " + getText("configuration.errorwhenexport")));
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

                    //Get it from web path
                    //jndi:/localhost/StrutsExample/upload/superfish.zip
                    //URL url = getServlet().getServletContext().getResource("upload/superfish.zip");
                    //InputStream in = url.openStream();

                    //Get it from bytes array
                    //byte[] bytes = new byte[4096];
                    //InputStream in = new ByteArrayInputStream(bytes);

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
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) { logger.warn("Error when closing FileInputStream: " + exportedFile, e); }
                    try {
                        if (out != null) {
                            out.flush();
                        }
                    } catch (IOException e) { logger.warn("Error when flushing: " + exportedFile, e);  }
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) { logger.warn("Error when closing ServletOutputStream: " + exportedFile, e);  }
                }
            }
        } catch (SearchException se) {
            addActionError(se.getMessage());
            configurations = configurationManager.getAll();
        }
        return SUCCESS;
    }

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

    public List<Long> getSelectedBox() {
        if (selectedBox == null) {
            selectedBox = new ArrayList<Long>();
        }
        return selectedBox;
    }

    public void setSelectedBox(List<Long> selectedBox) {
        this.selectedBox = selectedBox;
    }

    public List getConfigurations() {
        return configurations;
    }

    public void setQ(String q) {
        this.query = q;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void setMessageConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getMessageConfiguration() {
        return configuration;
    }

    public String getPageSize() {return pageSize;}

    public void setPageSize(String pageSize) {ConfigurationAction.pageSize = pageSize;}
}