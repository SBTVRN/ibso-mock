package ru.sbt.drtmn.lab.bus;

import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.jms.JmsQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.jms.JMSException;
import java.util.Properties;

/**
 * Created by RSG on 28.06.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *         Jms Component Factory for creating Component for connection with MQ.
 */
public class JmsComponentFactory {
    private static Logger logger = Logger.getLogger(JmsComponentFactory.class);

    public static Component createActiveMQComponent(Properties properties) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(properties.getProperty("activeMQ.url"));
//            connectionFactory.setPassword(properties.getProperty("activeMQ.user"));
//            connectionFactory.setUserName(properties.getProperty("activeMQ.password"));
            JmsComponent component = JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
            logger.debug("ActiveMQ JmsComponent created :" + component);
            return component;
        } catch (Exception e) {
            throw new RuntimeException("Can't create WS MQ component", e);
        }
    }

    public static JmsComponent createWSMQComponent(Properties properties) throws JMSException {
        JmsFactoryFactory jmsFactoryFactory = JmsFactoryFactory.getInstance(JmsConstants.WMQ_PROVIDER);
        JmsQueueConnectionFactory factory = jmsFactoryFactory.createQueueConnectionFactory();
        factory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        factory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, properties.getProperty("system.webSphereMq.manager"));
        factory.setStringProperty(WMQConstants.WMQ_HOST_NAME, properties.getProperty("system.webSphereMq.host"));
        factory.setIntProperty(WMQConstants.WMQ_PORT, Integer.valueOf(properties.getProperty("system.webSphereMq.port")));
        factory.setStringProperty(WMQConstants.WMQ_CHANNEL, properties.getProperty("system.webSphereMq.channelId"));
        if (StringUtils.hasText(properties.getProperty("system.webSphereMq.cipher"))) {
            factory.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, properties.getProperty("system.webSphereMq.cipher"));
        }
        if (properties.getProperty("system.webSphereMq.ccsid") != null) {
            factory.setIntProperty(WMQConstants.WMQ_CCSID, Integer.valueOf(properties.getProperty("system.webSphereMq.ccsid")));
        }
        JmsComponent component = JmsComponent.jmsComponentAutoAcknowledge(factory);
        logger.debug("WS MQ JmsComponent created :" + component);
        return component;
    }
}
