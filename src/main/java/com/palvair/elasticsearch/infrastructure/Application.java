package com.palvair.elasticsearch.infrastructure;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.palvair.elasticsearch")
@EnableEurekaClient
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Swagger2Feature swaggerFeature() {
        return new Swagger2Feature();
    }

}
