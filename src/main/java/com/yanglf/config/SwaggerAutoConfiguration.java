package com.yanglf.config;

import com.yanglf.prop.SwaggerProperties;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * @author ylf
 */
@ComponentScan
@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties properties;

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        // DocumentationType.OAS_30
        Set<String> protocols = new HashSet<>();
        protocols.add("http");
        protocols.add("https");
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(properties.isEnabled())
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                // 支持的协议
                .protocols(protocols)
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
        return docket;
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", In.HEADER.toValue());
        apiKeyList.add(apiKey);
        return apiKeyList;
    }


    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(new SecurityReference("Authorization",
                                new AuthorizationScope[]{new AuthorizationScope("global", "Authorization")})))
                        .build()
        );
    }


    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact(properties.getAuthor().getName(), properties.getAuthor().getUrl(), properties.getAuthor().getEmail());
        // 用ApiInfoBuilder进行定制
        return new ApiInfo(
                properties.getApiInfo().getTitle(),
                properties.getApiInfo().getDescription(),
                properties.getApiInfo().getVersion(),
                properties.getApiInfo().getTermsOfServiceUrl(),
                contact,
                properties.getApiInfo().getLicense(),
                properties.getApiInfo().getLicenseUrl(), new ArrayList<>());
    }
}
