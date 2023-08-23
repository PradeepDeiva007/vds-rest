package com.ford.vds.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.ford.vds.repository" })
@EntityScan(basePackages = { "com.ford.vds.domain", "com.ford.vds.model" })
@ComponentScan(basePackages = {"com.ford.vds.webservice"})
public class JPAConfig {

}
