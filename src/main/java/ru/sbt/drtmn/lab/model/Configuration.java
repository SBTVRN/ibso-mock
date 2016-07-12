package ru.sbt.drtmn.lab.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.sbt.drtmn.lab.util.JaxbXMLType;
import ru.sbt.drtmn.lab.xml.Param;
import ru.sbt.drtmn.lab.xml.ParamList;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

/**
 * This class is used to represent configuration.
 *
  */
@Entity
@Table(name = "CONFIGURATIONS")
@TypeDefs(
{
    @TypeDef(name = "xmlType", typeClass = JaxbXMLType.class, parameters = {
            @Parameter(name = "packages", value = "ru.sbt.drtmn.lab.xml"),
            @Parameter(name = "class", value = "ru.sbt.drtmn.lab.xml.ParamList")}
    )
})
@NamedQueries({
    @NamedQuery(
        name = "findConfigurationsByName",
        query = " from Configuration c where c.name = :name and c.active = true"
    ),
    @NamedQuery(
        name = "findAllConfigurationsByName",
        query = " from Configuration c where c.name = :name"
    ),
    @NamedQuery(
        name = "findAllConfigurationsBySectionId",
        query = " from Configuration c where c.section = :section"
    )
})
@XmlRootElement
@XmlType(propOrder = {"id", "name", "description", "section", "active", "inputParams", "outputParams", "messageTemplate"})
public class Configuration extends GenericModel implements Serializable {
    private static final long serialVersionUID = 3690197650654049848L;

    private Long id;
    // method name
    private String name;
    private String description;
    private Section section;
    private Boolean active;
    // params and template
    private String messageTemplate;
    private ParamList inputParams;
    private ParamList outputParams;

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public Configuration() {
    }

    /**
     * Create a new instance and set the name.
     *
     * @param name name of the role.
     */
    public Configuration(final String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "CONF_ID_GEN")
    public Long getId() {
        return id;
    }

    @Column(length = 64,nullable = false)
    public String getName() {
        return this.name;
    }

    @Column(length = 100)
    public String getDescription() {
        return this.description;
    }

    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    public Section getSection() { return this.section; }

    @Column(nullable = false)
    public Boolean getActive() {
        return active;
    }

    @Lob
    @Column(name = "MESSAGE_TEMPLATE", nullable = false)
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Type(type="xmlType")
    @Column(name = "INPUT_PARAMS")
    public ParamList getInputParams() {
        if (inputParams == null) {
            inputParams = new ParamList();
        }
        return inputParams;
    }

    @Type(type="xmlType")
    @Column(name = "OUTPUT_PARAMS")
    public ParamList getOutputParams() {
        if (outputParams == null) {
            outputParams = new ParamList();
        }
        return outputParams;
    }

    // Setters
    @XmlElement
    public void setId(Long id) {
        this.id = id;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }
    @XmlElement
    public void setSection(Section section) {
        this.section = section;
    }
    @XmlElement
    public void setActive(Boolean active) {
        this.active = active;
    }
    @XmlElement
    public void setInputParams(ParamList inputParams) {
        this.inputParams = inputParams;
    }
    @XmlElement
    public void setOutputParams(ParamList outputParams) {
        this.outputParams = outputParams;
    }
    @XmlElement
    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    // Transient
    @Transient
    public String getActiveYesNo() {
        return true == active ? "Да" : "Нет";
    }

    @Transient
    public List<Param> getInputParameterList() {
        List<Param> list = getInputParams().getParamList();
        return list;
    }

    @Transient
    public int getInputParameterSize() {
        return getInputParameterList().size();
    }

    public void addInputParam(String name, String value) {
        Param param = new Param();
        param.setName(name);
        param.setValue(value);
        //param.setDescription(description);
        getInputParameterList().add(param);
    }

    @Transient
    public List<Param> getOutputParameterList() {
        List<Param> list = getOutputParams().getParamList();
        return list;
    }

    @Transient
    public int getOutputParameterSize() {
        return getOutputParameterList().size();
    }

    public void addOutputParam(String name, String value) {
        Param param = new Param();
        param.setName(name);
        param.setValue(value);
        //param.setDescription(description);
        getOutputParameterList().add(param);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Configuration)) {
            return false;
        }
        final Configuration cfg = (Configuration) o;
        return !(name != null ? !name.equals(cfg.name) : cfg.name != null);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Configuration: " + this.id
               + ",\n Configuration Name: " + this.name
               + ",\n Configuration Description: " + this.description
               + ",\n Configuration Section: " + this.section
               + ",\n Configuration Active: " + this.active
               + ",\n Configuration Input Params [" + this.inputParams + "]"
               + ",\n Configuration Output Params [" + this.outputParams + "]"
               + ",\n Configuration Message [" + this.messageTemplate + "]";
    }
}
