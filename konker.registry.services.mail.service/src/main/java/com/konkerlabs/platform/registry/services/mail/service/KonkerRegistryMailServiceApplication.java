package com.konkerlabs.platform.registry.services.mail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KonkerRegistryMailServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(KonkerRegistryMailServiceApplication.class);

    public static void main(String[] args) {
        if(LOG.isInfoEnabled()){
            LOG.info("Start the konker Platform Mail Service...");
        }
        SpringApplication.run(KonkerRegistryMailServiceApplication.class, args);
    }
}
