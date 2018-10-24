package org.entando.entando.aps.system.init;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import org.entando.entando.aps.system.services.oauth2.OAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SwaggerInitializer  implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerInitializer.class);


    private BaseConfigManager baseConfigManager;
    private OAuthConsumerManager consumerManager;

    public SwaggerInitializer(BaseConfigManager baseConfigManager, OAuthConsumerManager consumerManager) {
        this.baseConfigManager = baseConfigManager;
        this.consumerManager = consumerManager;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            ConsumerRecordVO swaggerConsumer = consumerManager.getConsumerRecord("swagger");
            if (swaggerConsumer == null) createSwaggerConsumer();
        } catch (ApsSystemException e) {
            logger.warn("Can't configure Swagger.", e);
        }
    }

    private void createSwaggerConsumer() throws ApsSystemException {
        logger.info("Creating Swagger consumer");

        ConsumerRecordVO swaggerConsumer;
        String authUrl = baseConfigManager.getParam(SystemConstants.PAR_APPL_BASE_URL);

        swaggerConsumer = new ConsumerRecordVO();

        swaggerConsumer.setKey("swagger");
        swaggerConsumer.setSecret("swaggerswagger");
        swaggerConsumer.setName("Swagger");
        swaggerConsumer.setDescription("Swagger");
        swaggerConsumer.setCallbackUrl(authUrl + "api/webjars/springfox-swagger-ui/oauth2-redirect.html");
        swaggerConsumer.setScope("global");
        swaggerConsumer.setAuthorizedGrantTypes("authorization_code");
        swaggerConsumer.setIssuedDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        swaggerConsumer.setExpirationDate(Date.from(LocalDateTime.now().plusYears(10).toInstant(ZoneOffset.UTC)));

        consumerManager.addConsumer(swaggerConsumer);
    }
}
