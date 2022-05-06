package com.psjw.dmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing// 자동으로 최종 시점 저장 :@CreatedDate, @LastModifiedDate
@SpringBootApplication
public class DMakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DMakerApplication.class, args);
    }

}
