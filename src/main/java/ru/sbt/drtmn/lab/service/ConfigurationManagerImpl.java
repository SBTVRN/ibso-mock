package ru.sbt.drtmn.lab.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.drtmn.lab.dao.ConfigurationDao;
import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationManagerImpl extends GenericManagerImpl<Configuration, Long> implements ConfigurationManager {
    private static Logger logger = Logger.getLogger(ConfigurationManagerImpl.class);
    ConfigurationDao configurationDao;

    @Autowired
    public ConfigurationManagerImpl(ConfigurationDao  configurationDao ) {
        super(configurationDao );
        this.configurationDao = configurationDao ;
    }

    @Override
    public Integer activateMsgConfigurations(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return 0;
        }
        for (Long id : ids) {
            logger.debug("Configuration id=" + id);
            Configuration configuration = configurationDao.get(id);
            configuration.setActive(true);
        }
        return ids.size();
    }

    @Override
    public Integer deactivateMsgConfigurations(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return 0;
        }
        for (Long id : ids) {
            logger.debug("Configuration id=" + id);
            Configuration configuration = configurationDao.get(id);
            configuration.setActive(false);
        }
        return ids.size();
    }

    @Override
    public Integer deleteMsgConfigurations(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return 0;
        }
        for (Long id : ids) {
            logger.debug("Configuration id=" + id);
            configurationDao.remove(id);
        }
        return ids.size();
    }

    @Override
    public Boolean isMsgConfigurationExist(String name) {
        if (name == null) {
            return false;
        }
        logger.debug("Configuration name=" + name);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        List<Configuration> ret = configurationDao.findByNamedQuery("findAllConfigurationsByName", params);
        if ( ret.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Configuration> getMsgConfigurationsByName(String name) {
        List<Configuration> ret = new ArrayList<Configuration>();
        if (name == null) {
            return ret;
        }
        logger.debug("Configuration name=" + name);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        ret = configurationDao.findByNamedQuery("findConfigurationsByName", params);
        return ret;
    }

    // Replace old Configurations
    @Override
    public Boolean importMsgConfiguration(Configuration configuration) {
        boolean result = isMsgConfigurationExist(configuration.getName());
        if ( result ) {
            Configuration currentConfiguration = null;
            List<Configuration> oldConfigurations = getMsgConfigurationsByName(configuration.getName());
            if ( oldConfigurations.size() == 1 ) {
                currentConfiguration = oldConfigurations.get(0);
            } else {
                logger.error("Search by name returns more than one result");
                return false;
            }
            currentConfiguration.setDescription(configuration.getDescription());
            currentConfiguration.setActive(configuration.getActive());
            currentConfiguration.setMessageTemplate(configuration.getMessageTemplate());
            currentConfiguration.setInputParams(configuration.getInputParams());
            currentConfiguration.setOutputParams(configuration.getOutputParams());
        } else {
            try {
                configurationDao.save(configuration);
                result = true;
            } catch (Exception e) {
                logger.error("Error when saving configuration", e);
                result = false;
            }
        }
        return result;
    }

    // Merge only new Configurations
    @Override
    public Boolean mergeMsgConfiguration(Configuration configuration) {
        boolean result = isMsgConfigurationExist(configuration.getName());
        if ( !result ) {
            try {
                configurationDao.save(configuration);
                return true;
            } catch (Exception e) {
                logger.error("Error when saving configuration", e);
            }
        }
        return false;
    }

    @Override
    public File exportMsgConfigurations(List<Long> ids) {
        List<Configuration> configurations = new ArrayList<Configuration>();
        if (ids == null || ids.size() == 0) {
            return null;
        }
        for (Long id : ids) {
            logger.debug("Configuration id=" + id);
            configurations.add(configurationDao.get(id));
        }
        File exportedConfigurations = FileUtil.exportConfigurations(configurations);
        return exportedConfigurations;
    }
}