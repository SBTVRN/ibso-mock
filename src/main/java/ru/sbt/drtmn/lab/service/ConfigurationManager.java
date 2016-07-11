package ru.sbt.drtmn.lab.service;

import ru.sbt.drtmn.lab.model.Configuration;

import java.io.File;
import java.util.List;

public interface ConfigurationManager extends GenericManager<Configuration, Long> {

    public Boolean isMsgConfigurationExist(String name);
    public List<Configuration> getMsgConfigurationsByName(String name);
    public Integer activateMsgConfigurations(List<Long> ids);
    public Integer deactivateMsgConfigurations(List<Long> ids);
    public Integer deleteMsgConfigurations(List<Long> ids);
    public Boolean importMsgConfiguration(Configuration configuration);
    public Boolean mergeMsgConfiguration(Configuration configuration);
    public File exportMsgConfigurations(List<Long> ids);
}