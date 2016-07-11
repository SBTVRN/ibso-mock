package ru.sbt.drtmn.lab.bus;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.drtmn.lab.model.Configuration;
import ru.sbt.drtmn.lab.service.ConfigurationManager;
import ru.sbt.drtmn.lab.xml.Param;

import java.io.StringWriter;
import java.util.List;

/**
  */
public class MessageProcessor {
    private static Logger logger = Logger.getLogger(MessageProcessor.class);

    @Autowired
    private ConfigurationManager configurationManager;
    public final static String METHOD_NAME = "/Message/SimpleReqElem/Method/text()";
    private VelocityEngine velocityEngine;
    private CamelContext camelContext;
    private VelocityContext velocityContext = new VelocityContext();
    private ExchangeCallback exchangeCallback;

    public MessageProcessor(VelocityEngine velocityEngine, CamelContext camelContext, ExchangeCallback exchangeCallback) {
        this.velocityEngine = velocityEngine;
        this.camelContext = camelContext;
        this.exchangeCallback = exchangeCallback;
    }

    public String process(String message) {
        Configuration configuration = getMessageConfiguration(message);
        String response = null;
        if (configuration == null) {
            return response;
        }
        parseInputParameters(message, configuration);
        parseOutputParameters(configuration);
        response = generateResponse(configuration);
        exchangeCallback.send(response);
        return response;
    }

    private Configuration getMessageConfiguration(String message) {
        String method = XPathBuilder.xpath(METHOD_NAME).evaluate(camelContext, message, String.class);
        logger.debug("Configuration method=" + method);
        List<Configuration> configurations = configurationManager.getMsgConfigurationsByName(method);
        if (configurations.size() == 0 || configurations.size() > 1) {
            logger.warn("Can't find proper configuration " + configurations.size());
            return null;
        }
        Configuration configuration = configurations.get(0);
        logger.debug("Use configuration with id =" + configuration.getId() + ", name=" + configuration.getName());
        return configuration;
    }

    private void parseOutputParameters(Configuration configuration) {
        for (Param param : configuration.getOutputParameterList()) {
            logger.debug("Response param name=" + param.getName() + ", value=" + param.getValue());
            if ( (param.getValue()).equals("{randomUUID()}") ) {
                try {
                    velocityContext.put(param.getName(), java.util.UUID.randomUUID());
                } catch (Exception e) {
                    logger.warn("Exception on generating new UUID ", e);
                }
            } else {
                velocityContext.put(param.getName(), param.getValue());
            }
        }
    }

    private void parseInputParameters(String message, Configuration configuration) {
        for (Param param : configuration.getInputParameterList()) {
            logger.debug("Request param name=" + param.getName() + ", xpath=" + param.getValue());
            String value = null;
            try {
                value = XPathBuilder.xpath(param.getValue()).evaluate(camelContext, message, String.class);
                logger.debug("Request evaluated value=" + value);
            } catch (Exception e) {
                logger.error("Can' find parameter in xml" + param.getName(), e);
            }
            velocityContext.put(param.getName(), value);
        }
    }

    private String generateResponse(Configuration configuration) {
        if (configuration.getMessageTemplate() == null) {
            logger.warn("Response template is empty for configuration id =" + configuration.getId());
            return "";
        }
        StringWriter writer = new StringWriter();
        try {
            velocityEngine.evaluate(velocityContext, writer, "response", configuration.getMessageTemplate());
        } catch (Exception e) {
            logger.error("Error while generating response", e);
        }
        return writer.toString();
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

}
