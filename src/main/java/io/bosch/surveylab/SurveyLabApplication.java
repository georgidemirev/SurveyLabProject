package io.bosch.surveylab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SurveyLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyLabApplication.class, args);
    }

}