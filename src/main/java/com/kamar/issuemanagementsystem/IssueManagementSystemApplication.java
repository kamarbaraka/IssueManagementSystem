package com.kamar.issuemanagementsystem;

import io.swagger.v3.oas.annotations.OpenAPI31;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IssueManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssueManagementSystemApplication.class, args);
    }

}
