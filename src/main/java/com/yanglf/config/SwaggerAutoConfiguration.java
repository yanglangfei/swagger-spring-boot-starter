package com.yanglf.config;

import com.yanglf.config.SwaggerConfig;
import com.yanglf.prop.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ylf
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnClass(SwaggerConfig.class)
@EnableSwagger2
public class SwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnMissingBean(SwaggerConfig.class)
    SwaggerConfig swaggerConfig() {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        swaggerConfig.setProperties(swaggerProperties);
        return swaggerConfig;
    }
}
