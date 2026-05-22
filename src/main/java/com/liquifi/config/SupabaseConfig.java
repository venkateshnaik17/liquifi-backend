package com.liquifi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Supabase connectivity is handled via standard JDBC (PostgreSQL driver).
 * This config holds Supabase-specific properties for reference / injection.
 */
@Configuration
public class SupabaseConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }
}
