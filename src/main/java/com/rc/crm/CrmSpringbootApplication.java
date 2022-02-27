package com.rc.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author rc
 */
@SpringBootApplication
@MapperScan(basePackages = "com.rc.crm.*.dao")
public class CrmSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmSpringbootApplication.class, args);
    }

}
