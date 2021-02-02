package com.mingzhang.jiegon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.mingzhang.jiegon.*")
public class JiegonApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiegonApplication.class, args);
    }

}
