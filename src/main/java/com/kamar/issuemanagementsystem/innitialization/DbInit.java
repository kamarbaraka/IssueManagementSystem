package com.kamar.issuemanagementsystem.innitialization;


import com.kamar.issuemanagementsystem.app_properties.InnitUserProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
public class DbInit {

    @Bean
	SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(InnitUserProperties properties,
                                                                               SqlInitializationProperties sqlProperties
    ) {

        DataSource dataSource = DataSourceBuilder.create()
                .type(SimpleDriverDataSource.class)
                .username(properties.dbUsername())
                .password(properties.dbPassword())
                .url(properties.dbUrl())
                .build();

        return new SqlDataSourceScriptDatabaseInitializer(dataSource, sqlProperties);
	}

}
