package com.servicedesk.packageservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://matodzikutama:Kirkney2@springcluster.e2s7vd0.mongodb.net/?retryWrites=true&w=majority");
    }
    @Override
    protected String getDatabaseName() {
        return "springcluster";
    }
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
