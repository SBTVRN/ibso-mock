package ru.sbt.drtmn.lab.util;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BlobType;
import org.hibernate.type.StringType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import ru.sbt.drtmn.lab.xml.ObjectFactory;
import ru.sbt.drtmn.lab.xml.ParamList;

/**
 This is a custom Hibernate Type that serialize java objects as XML String using JAXB.
 the parameter 'packages' represent the list of package required by JAXB to be initialized (Packages are serapated with :).
 The paramater 'class' is the fullname of the class being serialized.
 */
public class JaxbXMLType implements UserType, ParameterizedType {

    protected final Log log = LogFactory.getLog(getClass());

    private static final int[] SQL_TYPES = { Types.CLOB };

    /**
     * JAXBContext pool. a context for a package is instantiated only one time.
     * JAXBContext are ThreadSafe
     */
    private static final ConcurrentMap<String, JAXBContext> JAXBCONTEXTPOOL = new ConcurrentHashMap<String, JAXBContext>();

    private static final String PACKAGES = "packages";

    private static final String CLASSNAME = "class";

    private Properties parameters = null;

    /**
     * Make deep copy by using Serialization or JAXB Serialization. This
     * Implementaion assume that Java serialization is faster than JAXB
     * Serialization. Serializable Objects will be copied using Serialization,
     * the other will be copied using JAXB binding
     */
    public Object deepCopy(Object value) throws HibernateException {
        return fromXMLString(toXMLString(value));
    }

    /**
     * Deserialize using Unmarshaller
     */
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        if (!(cached instanceof String)) {
            throw new IllegalArgumentException("Cached value mus be a String");
        }
        return fromXMLString((String) cached);
    }

    /**
     * Serialize as XML String using JAXB
     */
    public Serializable disassemble(Object value) throws HibernateException {
        return toXMLString(value);
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if ((x == null) && (y == null)) {
            return true;
        }
        if ((x == null) || (y == null)) {
            return false;
        }
        return x.equals(y);
    }

    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            throw new IllegalArgumentException(
                    " Parameter for hashCode must not be null");
        }
        return x.hashCode();
    }

    public boolean isMutable() {
        return true;
    }

    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return deepCopy(original);
    }

    private Class returnedClass = null;

    public Class returnedClass() {
        if (returnedClass == null) {
            String className = getParameterValues().getProperty(CLASSNAME);
            if (className == null) {
                className = "";
            }
            try {
                returnedClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                // Oh my God, very bad
                IllegalArgumentException ex = new IllegalArgumentException(
                        "The parameter 'class' is not correct : "
                                + e.getMessage());
                ex.setStackTrace(e.getStackTrace());
                throw ex;
            }
        }
        return returnedClass;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     * parameter injection point
     */
    public void setParameterValues(Properties parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the parameters
     */
    protected Properties getParameterValues() {
        return parameters;
    }

    protected JAXBContext getContext() {
        JAXBContext xmlSerializer = JAXBCONTEXTPOOL.get(parameters
                .getProperty(PACKAGES));
        if (xmlSerializer == null) {
            if (parameters == null) {
                throw new IllegalStateException(
                        "No parameter available: JAXB  context cannot be instantiated. Please call setParameterValues first");
            }
            try {
                xmlSerializer = JAXBContext.newInstance(parameters
                        .getProperty(PACKAGES));
            } catch (JAXBException e) {
                IllegalArgumentException ex = new IllegalArgumentException(
                        "Cannot instantiate JAXB Context with given parameters ");
                ex.setStackTrace(e.getStackTrace());
                e.printStackTrace();
                throw ex;
            }
            if (xmlSerializer != null) {
                JAXBCONTEXTPOOL.put(parameters.getProperty(PACKAGES),
                        xmlSerializer);
            }
        }
        return xmlSerializer;
    }

    /**
     * the unmarshaller. Rather than accessing this field directy, please use
     * the getter method
     */
    private Unmarshaller unmarshaller;

    protected Unmarshaller getUnmarshaller() {
        if (unmarshaller == null) {
            JAXBContext jc = getContext();
            try {
                unmarshaller = jc.createUnmarshaller();
            } catch (JAXBException e) {
                IllegalArgumentException ex = new IllegalArgumentException(
                        "Cannot instantiate unmarshaller");
                ex.setStackTrace(e.getStackTrace());
                throw ex;
            }
        }
        return unmarshaller;
    }

    /**
     * the marshaller. Rather than accessing this field directy, please use the
     * getter method
     */
    private Marshaller marshaller;

    protected Marshaller getMarshaller() {
        if (marshaller == null) {
            JAXBContext jc = getContext();
            try {
                marshaller = jc.createMarshaller();
            } catch (JAXBException e) {
                IllegalArgumentException ex = new IllegalArgumentException(
                        "Cannot instantiate marshaller");
                ex.setStackTrace(e.getStackTrace());
                throw ex;
            }
        }
        return marshaller;
    }

    protected String toXMLString(Object value) {
        StringWriter stringWriter = new StringWriter();
        try {
            if (value == null) {
                return null;
            }
            getMarshaller().marshal(value, stringWriter);
            String result = stringWriter.toString();
            stringWriter.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            IllegalArgumentException ex = new IllegalArgumentException(
                    "cannot disassemble the object");
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    protected Object fromXMLString(String xmlString) {
        Unmarshaller unmarshaller = getUnmarshaller();
        if (xmlString == null) {
            return null;
        }
        StreamSource source = new StreamSource(new StringReader(xmlString));
        try {
            return unmarshaller.unmarshal(source);
        } catch (JAXBException e) {
            IllegalArgumentException ex = new IllegalArgumentException(
                    "cannot assemble the cached object");
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        String xmlString = (String) StringType.INSTANCE.nullSafeGet(resultSet, strings, sessionImplementor, o);
        log.debug(xmlString);
        return fromXMLString(xmlString);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        String xmlString = toXMLString(o);
        log.debug(xmlString);
        StringType.INSTANCE.nullSafeSet(preparedStatement, xmlString, i, sessionImplementor);
    }

    /*public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String xmlString = (String) StringType.INSTANCE.nullSafeGet(rs, names[0]);
        return fromXMLString(xmlString);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        String xmlString = toXMLString(value);
        StringType.INSTANCE.nullSafeSet(st, xmlString, index);
    }*/
}
