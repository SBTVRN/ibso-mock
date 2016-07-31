package ru.sbt.drtmn.lab.dao;

import org.springframework.stereotype.Repository;
import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.model.Section;

import java.util.List;

/**
 * Created: 12.07.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *
 */
@Repository("sectionDao")
public class SectionDaoImpl extends GenericDaoImpl<Section, Long> implements SectionDao {
    public SectionDaoImpl() {
        super(Section.class);
    }
}
