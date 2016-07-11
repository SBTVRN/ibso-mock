package ru.sbt.drtmn.lab.dao;

import ru.sbt.drtmn.lab.model.Configuration;
import org.springframework.stereotype.Repository;

@Repository("configurationDao")
public class ConfigurationDaoImpl extends GenericDaoImpl<Configuration, Long> implements ConfigurationDao {
    public ConfigurationDaoImpl() {
        super(Configuration.class);
    }
}
