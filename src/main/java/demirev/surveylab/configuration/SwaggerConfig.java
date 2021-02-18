package demirev.surveylab.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
@Profile({"dev", "qa", "mydb"})
@ConfigurationProperties(prefix = "survey-lab.swagger")
public class SwaggerConfig {

    private String apiTitle;
    private String apiVersion;
    private String apiDescription;
    private String serviceName;
    private String serviceUrl;
    private String serviceEmail;
    private String termsOfServiceUrl;
    private String license;
    private String licenseUrl;

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/**"))
                .apis(RequestHandlerSelectors.basePackage("demirev.surveylab"))
                .build()
                .ignoredParameterTypes(Pageable.class)
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(apiTitle,
                apiDescription,
                apiVersion,
                termsOfServiceUrl,
                new springfox.documentation.service.Contact(serviceName, serviceUrl, serviceEmail),
                license,
                licenseUrl,
                Collections.emptyList());
    }

    public void setApiTitle(String apiTitle) {
        this.apiTitle = apiTitle;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setServiceEmail(String serviceEmail) {
        this.serviceEmail = serviceEmail;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }
}
