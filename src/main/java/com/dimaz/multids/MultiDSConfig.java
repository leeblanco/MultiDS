package com.dimaz.multids;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MultiDSConfig {

	@Bean
	@Primary
	@ConfigurationProperties(prefix="datasource.mysql")
	public DataSource mySQLDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix="datasource.postgre")
	public DataSource postgreSQLDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public JdbcTemplate jdbcMySqlTemplate(@Qualifier("mySQLDataSource") DataSource datasource) {
		return new JdbcTemplate(datasource);
	}
	
	@Bean
	public JdbcTemplate jdbcPostgreSqlTemplate(@Qualifier("postgreSQLDataSource") DataSource datasource) {
		return new JdbcTemplate(datasource);
	}
}
