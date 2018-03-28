package com.dimaz.multids;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MultiDSConfig {

//	@ConfigurationProperties(prefix="datasource.mysql")
	@Bean
	@Primary
	public DataSource mySQLDataSource() throws SQLException {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		
		ds.setDriver(new com.mysql.jdbc.Driver());
		ds.setUrl("jdbc:mysql://localhost:3306/account");
		ds.setUsername("dimaz");
		ds.setPassword("andres123");
		
		return ds;
//		return DataSourceBuilder.create().build();
	}
	
	
//	@ConfigurationProperties(prefix="datasource.postgre")
	@Bean
	public DataSource postgreSQLDataSource() {

		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		
		ds.setDriver(new org.postgresql.Driver());
		ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
		ds.setUsername("postgres");
		ds.setPassword("Andres123");
		
		return ds;
//		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public JdbcTemplate jdbcMySqlTemplate(@Qualifier("mySQLDataSource") DataSource datasource) {
		return new JdbcTemplate(datasource);
	}
	
	@Bean
	public JdbcTemplate jdbcPostgreSqlTemplate( DataSource datasource) {
		return new JdbcTemplate(datasource);
	}
	
	/*@Bean
	public JdbcTemplate jdbcPostgreSqlTemplate(@Qualifier("postgreSQLDataSource") DataSource datasource) {
		return new JdbcTemplate(datasource);
	}*/
}
