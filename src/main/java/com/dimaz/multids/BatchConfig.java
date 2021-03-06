package com.dimaz.multids;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig  {

	@Autowired
	@Qualifier("jdbcMySqlTemplate")
	JdbcTemplate mySQLJdbcTemp;
	
	@Autowired
	@Qualifier("jdbcPostgreSqlTemplate")
	JdbcTemplate postGreSQLJdbcTemp;

	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
/*	@Override
	@Autowired
	public void setDataSource(@Qualifier("mySQLDataSource")DataSource batchDataSource) {
		super.setDataSource(batchDataSource);
	}*/
	
	/*@Bean
	BatchConfigurer mySqlConfigurer(@Qualifier("mySQLDataSource") DataSource mySqlDS) {
		return new DefaultBatchConfigurer(mySqlDS);
	}
	
	@Bean
	BatchConfigurer postGreConfigurer(@Qualifier("postgreSQLDataSource") DataSource postGreDS) {
		return new DefaultBatchConfigurer(postGreDS);
	}*/
	
	@Bean
	public ItemReader<Contacts> readContacts() {
		JdbcCursorItemReader<Contacts> readContactsTbl = new JdbcCursorItemReader<>();
		
		readContactsTbl.setDataSource(mySQLJdbcTemp.getDataSource());
		
		readContactsTbl.setSql("select * from contacts");
		readContactsTbl.setRowMapper(new BeanPropertyRowMapper<>(Contacts.class));
		
		return readContactsTbl;
	}
	
	@Bean
	public JdbcBatchItemWriter<Contacts> writeContacts() {
		JdbcBatchItemWriter<Contacts> writeContactsTbl = new JdbcBatchItemWriter<Contacts>();
		
		StringBuffer insertSql = new StringBuffer();
		
		insertSql.append("INSERT INTO ACCOUNTS (firstname,lastname,telephone, email,created)");
		insertSql.append(" VALUES (:firstname, :lastname, :telephone, :email, :created) ");
		writeContactsTbl.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Contacts>());
		writeContactsTbl.setSql(insertSql.toString());
		
		writeContactsTbl.setDataSource(postGreSQLJdbcTemp.getDataSource());
		return writeContactsTbl;
	}
	
	@Bean
	public Job importContacts(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("importContacts")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1())
				.end()
				.build();
				
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Contacts, Contacts> chunk(2)
				.reader(readContacts())
				.writer(writeContacts())
				.build();
	}
}
