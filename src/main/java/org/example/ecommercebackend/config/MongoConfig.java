package org.example.ecommercebackend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration class.
 * Configures MongoDB connection and customizes the MongoTemplate.
 * ENFORCES Atlas connection - will NOT connect to localhost.
 */
@Configuration
@EnableMongoRepositories(basePackages = "org.example.ecommercebackend.repository")
public class MongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    /**
     * Create MongoClient explicitly using Atlas URI.
     * Prevents Spring Boot from auto-detecting localhost.
     */
    @Bean
    public MongoClient mongoClient() {
        String mongoUri = System.getProperty("spring.data.mongodb.uri");
        
        if (mongoUri == null || mongoUri.isEmpty()) {
            throw new IllegalStateException("MongoDB URI not configured. Check .env file.");
        }
        
        if (mongoUri.contains("localhost") || mongoUri.contains("127.0.0.1")) {
            throw new IllegalStateException("Localhost connections not allowed. Only Atlas connections permitted.");
        }
        
        if (!mongoUri.contains("mongodb+srv://") || !mongoUri.contains(".mongodb.net")) {
            throw new IllegalStateException("Invalid MongoDB URI. Must be Atlas connection (mongodb+srv://...mongodb.net)");
        }
        
        logger.info("Connecting to MongoDB Atlas: {}", mongoUri.replaceAll("mongodb\\+srv://[^@]+@([^/]+).*", "$1"));
        
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient, 
                                                      @Value("${MONGODB_DATABASE:ecommerce_db}") String databaseName) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
    }

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
