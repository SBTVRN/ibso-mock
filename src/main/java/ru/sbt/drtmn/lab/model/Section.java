package ru.sbt.drtmn.lab.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created: 12.07.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *         Entity class for groups of configurations.
 */





@Entity
@Table(name = "SECTIONS")
@NamedQueries({
    @NamedQuery(
        name = "findSectionsByName",
        query = " from Section s where s.name = :name"
    )
})
public class Section extends GenericModel implements Serializable {
    private static final long serialVersionUID = 1458648994782241146L;

    private Long id;
    private String name;
    private String description;
    private Set<Configuration> configurations = new HashSet<Configuration>();

    public Section() {
    }

    /**
     * Create a new instance and set the name.
     *
     * @param name name of the role.
     */
    public Section(final String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "SECT_ID_GEN")
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

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Configuration> getConfigurations() {
        return configurations;
    }
    public void setConfigurations(Set<Configuration> configurations) {
        this.configurations = configurations;
    }
    public void addConfiguration(Configuration configuration) {
        configuration.setSection(this);
        getConfigurations().add(configuration);
    }
    public void removeConfiguration(Configuration configuration) {
        getConfigurations().remove(configuration);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        final Section sec = (Section) o;
        return !(name != null ? !name.equals(sec.name) : sec.name != null);
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
        return "Section: " + this.id
            + ",\n Section Name: " + this.name
            + ",\n Section Description: " + this.description;
    }

}
