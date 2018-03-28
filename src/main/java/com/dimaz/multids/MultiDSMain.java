package com.dimaz.multids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;

//@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@SpringBootApplication
public class MultiDSMain {

	public static void main (String [] args) {
		SpringApplication.run(MultiDSMain.class, args);
	}
	
/*	@Bean
	public PlatformTransactionManager transactionManager() {
	    return new ResourcelessTransactionManager();
	}*/
}
