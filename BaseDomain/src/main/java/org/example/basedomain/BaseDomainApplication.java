package org.example.basedomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BaseDomainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseDomainApplication.class, args);
    }

}
