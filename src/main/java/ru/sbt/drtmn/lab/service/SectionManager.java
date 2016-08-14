package ru.sbt.drtmn.lab.service;

import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.model.Section;

import java.util.List;

/**
 * Created: 12.07.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *         Short Class Description.
 */
public interface SectionManager extends GenericManager<Section, Long> {
    public Integer deleteSections(List<Long> ids);
    public List<Configuration> getSectionConfigurations(Section section);
    public List<Section> getSectionsByName(String name);
    public void update(Section section);
}