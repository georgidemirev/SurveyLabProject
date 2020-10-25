package io.bosch.surveylab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableConfigurationProperties
public class SurveyLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyLabApplication.class, args);
    }

}
