package org.example.ecommercebackend.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads MongoDB connection details from .env file and sets as system property.
 */
public class DotenvConfig {

    static {
        loadMongoConfig();
    }

    private static void loadMongoConfig() {
        try {
            File envFile = new File(".env");
            if (!envFile.exists()) {
                envFile = new File("../.env");
            }
            
            if (!envFile.exists()) {
                throw new IllegalStateException(".env file not found. Cannot connect to MongoDB.");
            }
            
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(envFile)) {
                props.load(fis);
            }
            
            String username = props.getProperty("MONGODB_USERNAME");
            String password = props.getProperty("MONGODB_PASSWORD");
            String cluster = props.getProperty("MONGODB_CLUSTER");
            String database = props.getProperty("MONGODB_DATABASE");
            
            if (username == null || password == null || cluster == null || database == null) {
                throw new IllegalStateException("Missing MongoDB configuration in .env file");
            }
            
            String mongoUri = String.format("mongodb+srv://%s:%s@%s/%s?appName=E-Com-Cluster&retryWrites=true&w=majority",
                    username.trim(), password.trim(), cluster.trim(), database.trim());
            
            System.setProperty("spring.data.mongodb.uri", mongoUri);
            
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load .env file: " + e.getMessage(), e);
        }
    }
}

