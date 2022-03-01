package com.rc.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author rc
 */
@SpringBootApplication
@MapperScan(basePackages = "com.rc.crm.*.dao")
public class CrmSpringbootApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CrmSpringbootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CrmSpringbootApplication.class, args);
    }

}
