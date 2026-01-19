package org.example.ecommercebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration class.
 * Configures MongoDB connection and customizes the MongoTemplate.
 */
@Configuration
@EnableMongoRepositories(basePackages = "org.example.ecommercebackend.repository")
public class MongoConfig {

    /**
     * Custom MongoTemplate that removes the _class field from documents.
     * This provides cleaner documents in MongoDB.
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory, 
                                        MongoMappingContext mongoMappingContext) {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDatabaseFactory), 
                mongoMappingContext
        );
        // Remove _class field from MongoDB documents
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDatabaseFactory, converter);
    }
}
