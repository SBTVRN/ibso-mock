package ru.sbt.drtmn.lab.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.drtmn.lab.dao.ConfigurationDao;
import ru.sbt.drtmn.lab.dao.SectionDao;
import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.model.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 12.07.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *         Section service
 */
public class SectionManagerImpl extends GenericManagerImpl<Section, Long> implements SectionManager {
    private static Logger logger = Logger.getLogger(SectionManagerImpl.class);
    SectionDao sectionDao;

    ConfigurationDao configurationDao;

    @Autowired
    public SectionManagerImpl(SectionDao sectionDao ) {
        super(sectionDao);
        this.sectionDao = sectionDao;
    }

    @Override
    public Integer deleteSections(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return 0;
        }
        for (Long id : ids) {
            logger.debug("Section id=" + id);
            sectionDao.remove(id);
        }
        return ids.size();
    }

    @Override
    public List<Configuration> getSectionConfigurations(Section section) {
        List<Configuration> ret = new ArrayList<Configuration>();
        if (section == null) {
            return ret;
        }
        logger.debug("Section name=" + section);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("section", section);
        ret = this.configurationDao.findByNamedQuery("findAllConfigurationsBySection", params);
        return ret;
    }

    @Override
    public void update(Section section) {
        Section oldSection = get(section.getId());
        oldSection.setName(section.getName());
        oldSection.setDescription(section.getDescription());
    }

    @Override
    public List<Section> getSectionsByName(String name) {
        List<Section> ret = new ArrayList<Section>();
        if (name == null) {
            return ret;
        }
        logger.debug("Section name=" + name);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        ret = sectionDao.findByNamedQuery("findSectionsByName", params);
        return ret;
    }

    public ConfigurationDao getConfigurationDao() {
        return configurationDao;
    }
    @Autowired
    public void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }
}
