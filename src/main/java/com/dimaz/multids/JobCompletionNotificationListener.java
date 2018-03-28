package com.dimaz.multids;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger logger = Logger.getLogger(JobCompletionNotificationListener.class);
	
	@Autowired
	@Qualifier("jdbcPostgreSqlTemplate")
	private JdbcTemplate jdbcPostGre;
	
	@Autowired
	public JobCompletionNotificationListener(@Qualifier("jdbcPostgreSqlTemplate")JdbcTemplate jdbcTemplate) {
		this.jdbcPostGre = jdbcTemplate;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		
		StringBuilder query = new StringBuilder();
		query.append("Select contacts_id, firstname, lastname, telephone, email, created ");
		query.append(" from contacts ");
		
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("JOB FINISHED");
			
			List<Contacts> results = jdbcPostGre.query(query.toString(), new RowMapper<Contacts>() {
				
				public Contacts mapRow(ResultSet rs, int row) throws SQLException {
					return new Contacts(rs.getString(1), rs.getString(2));
				}
			});
			
			for (Contacts contact: results) {
				
				logger.info("Contact FirstName: "+ contact.getFirstname() 
				+ "Contact LastName: " + contact.getLastname() );
				
			}
		}
	}
}
