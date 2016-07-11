package ru.sbt.drtmn.lab.bus;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import ru.sbt.drtmn.lab.service.ConfigurationManager;
import ru.sbt.drtmn.lab.util.FileUtil;

import javax.jms.JMSException;
import java.util.Properties;

/**
 * Настройка обработки сообщений
 *
 * @author SBT-Kranchev-DF
 * @since 16.09.2013
 */

public class ExchangeConfigurator {
    private static Logger logger = Logger.getLogger(ExchangeConfigurator.class);

    private VelocityEngine velocityEngine;
    private CamelContext context;
    private ConfigurationManager configurationManager;

    private Properties properties;
    private String source;
    private String target;
    private String settingsFile;

    public ExchangeConfigurator() throws Exception {}

    public void init() throws Exception {
        velocityEngine = new VelocityEngine();
        velocityEngine.init();
        context = new DefaultCamelContext();
        properties = FileUtil.getProperties(settingsFile);
        registerComponents(context);
        source = properties.getProperty("queue.to_bus");
        target = properties.getProperty("queue.from_bus");

        configure();
        context.start();
    }

    private void registerComponents(CamelContext context) {
        if ( (properties.getProperty("mqImplementation")).equalsIgnoreCase("ApacheActiveMQ") ) {
            try {
                Component component = JmsComponentFactory.createActiveMQComponent(properties);
                context.addComponent("webSphereMq", component);
            } catch (RuntimeException e) {
                logger.error("Active MQ Component registration failed ", e);
            }
        } else if ( (properties.getProperty("mqImplementation")).equalsIgnoreCase("WSMQ") ) {
            try {
                Component component = JmsComponentFactory.createWSMQComponent(properties);
                context.addComponent("webSphereMq", component);
            } catch (JMSException e) {
                logger.error("WS MQ Component registration failed ", e);
            }
        } else {
            throw new IllegalArgumentException("Incorrect parameter for component creating");
        }
    }

    public void configure() {
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    MessageProcessor messageProcessor = new MessageProcessor(velocityEngine, context, createExchangeCallback("direct:response", context));
                    messageProcessor.setConfigurationManager(configurationManager);
                    this
                            .from(source)
                            .to("log:" + logger.getName() + "?multiline=true")
                            .bean(messageProcessor, "process");

                    this
                            .from("direct:response")
                            .to("log:" + logger.getName() + "?multiline=true")
                            .to(target);
                }
            });
        } catch (Exception e) {
            logger.error("Can't configure camel context for source [" + source + "] and target [" + target + "] ", e);
            throw new IllegalArgumentException(e);
        }
    }

    private ExchangeCallback createExchangeCallback(final String endpointUri, CamelContext context) {
        final ProducerTemplate producerTemplate = context.createProducerTemplate();
        return new ExchangeCallback() {
            @Override
            public void send(Object message) {
                logger.debug("About to send message [\n" + message +  "\n] to endpoint [" + endpointUri + "]...");
                producerTemplate.asyncSendBody(endpointUri, message);
                logger.debug("Message [\n" + message + "\n] is sent to endpoint [" + endpointUri + "]");
            }
        };
    }

    public void destroy() throws Exception {
        context.stop();
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void setSettingsFile(String settingsFile) {
        this.settingsFile = settingsFile;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
