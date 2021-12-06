package com.data.report.datareport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.data.report.datareport.services.CCMReport;

@SpringBootApplication
public class DataReportApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext run = SpringApplication.run(DataReportApplication.class, args);
		MongoTemplate mongoTemplate = (MongoTemplate) run.getBean("mongoTemplate");
		CCMReport report = new CCMReport();
		report.generateReport(mongoTemplate);
	}
}
