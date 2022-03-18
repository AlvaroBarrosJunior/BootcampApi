package com.bootcamp.api.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootConfiguration
public class SwaggerConfig {

    @Value("${swagger.info.version}")
    private String version;
    
    @Value("${swagger.info.title}")
    private String title;
    
    @Value("${swagger.info.description}")
    private String description;
    
    @Value("${swagger.info.termsOfServiceUrl}")
    private String termsOfServiceUrl;
    
    @Value("${swagger.info.license}")
    private String license;
    
    @Value("${swagger.info.licenseUrl}")
    private String licenseUrl;
    
    @Value("${swagger.info.contact.name}")
    private String contactName;
    
    @Value("${swagger.info.contact.url}")
    private String contactUrl;
    
    @Value("${swagger.info.contact.email}")
    private String contactEmail;
    
    @Value("${swagger.path_api_scan}")
    private String pathApiScan;

    @Bean
    public Docket api() throws Exception {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(pathApiScan)).paths(PathSelectors.any()).build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact(contactName, contactUrl, contactEmail);
        return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl,
        		Collections.emptyList());
    }


    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }

}
