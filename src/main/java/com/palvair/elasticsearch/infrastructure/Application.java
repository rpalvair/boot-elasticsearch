package com.palvair.elasticsearch.infrastructure;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.ws.rs.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = {"com.palvair.elasticsearch"})
@EnableEurekaClient
public class Application {

    @Autowired
    private Bus bus;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endPoint = new JAXRSServerFactoryBean();
        endPoint.setBus(bus);
        final List<Class<?>> resourceClasses = getResourceClasses();
        endPoint.setResourceClasses(resourceClasses);
        endPoint.setAddress("/");
        endPoint.setFeatures(Collections.singletonList(new Swagger2Feature()));
        endPoint.setProvider(new JacksonJsonProvider());
        return endPoint.create();
    }

    private List<Class<?>> getResourceClasses() {
        return Stream.of(applicationContext.getBeanNamesForAnnotation(Service.class))
                .filter(this::isResource)
                .map(this::getClass)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Class<?> getClass(final String beanName) {
        return applicationContext.getBean(beanName).getClass();
    }

    private boolean isResource(final String beanName) {
        return applicationContext.findAnnotationOnBean(beanName, Path.class) != null;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
