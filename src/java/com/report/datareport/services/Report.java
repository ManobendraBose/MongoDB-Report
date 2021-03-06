package com.data.report.datareport.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.data.report.datareport.modal.Address;
import com.data.report.datareport.modal.Email;
import com.data.report.datareport.modal.CustAssignment;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CReport {
	
	private static final String REPORT_FILE_PATH = "/Reports/CReport.xlsx";
	
	public void generateReport(MongoTemplate mongoTemplate) throws IOException{

		Criteria status = Criteria.where("status").is("A");
		Criteria customerAssignmentLevelCode = Criteria.where("levelCode").is("ICA");
		Criteria contactsStatus = Criteria.where("contactStatus").is("A");
		

		
		MatchOperation match = Aggregation.match(new Criteria().andOperator(status, contactsStatus);

		LookupOperation assignmentLookupOperation = LookupOperation.newLookup().from("contacts")
				.localField("contactId").foreignField("_id").as("contacts");
		AggregationOperation unwindCustomerAssignment = Aggregation.unwind("contacts");

		LookupOperation addressLookupOperation = LookupOperation.newLookup().from("contactAddress").localField("contactId")
				.foreignField("contactId").as("contactAddress");

		LookupOperation emailLookupOperation = LookupOperation.newLookup().from("contactEmail").localField("contactId")
				.foreignField("contactId").as("contactEmail");

		Aggregation aggregation = Aggregation.newAggregation(match, 
				emailLookupOperation, 
				assignmentLookupOperation, 
				unwindCustomerAssignment, 
				addressLookupOperation);
		List<CustomerAssignment> results = mongoTemplate.aggregate(aggregation, CustomerAssignment.class, CustomerAssignment.class)
				.getMappedResults();
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet spreadsheet = workbook.createSheet("Export");
			createHeadings(spreadsheet);
			int rowid = 1;
			for (CustomerAssignment key : results) {
				log.info("Report rowid: " + rowid);
				XSSFRow row;
				int cellid = 0;
				row = spreadsheet.createRow(rowid++);
				Cell contactId = row.createCell(cellid++);
				Cell name = row.createCell(cellid++);
				Cell publicEmail = row.createCell(cellid++);
				Cell privateEmail = row.createCell(cellid++);
				Cell inum = row.createCell(cellid++);
				Cell contactType = row.createCell(cellid++);
				contactId.setCellValue(key.getContactId());
				name.setCellValue(key.getFirstName() + " " + key.getLastName());
				inum.setCellValue(Integer.parseInt(key.getLevelValue()));
				contactType.setCellValue(key.getContactTypeName());
				if (!key.getContactEmail().isEmpty()) {
					for (Email email : key.getContactEmail()) {
						if (email.getStatus().equals("A") && email.isPublic()) {
							publicEmail.setCellValue(email.getEmail());
						}
						if (email.getStatus().equals("A") && !email.isPublic()) {
							privateEmail.setCellValue(email.getEmail());
						}
					}
				}
				if (!key.getContactAddress().isEmpty()) {
					for (Address address : key.getContactAddress()) {
						if (address.getStatus().equals("A") && Objects.nonNull(address.getAlpha2CountryCode())) {
							region.setCellValue(address.getAlpha2CountryCode());
						}
					}
				}
			}

			FileOutputStream out = new FileOutputStream(new File(REPORT_FILE_PATH));
			workbook.write(out);
			out.close();
		}
		log.info("Report created with Assignment count: " + results.size());
	}
	
	private void createHeadings(XSSFSheet spreadsheet) {
		XSSFRow row;
		row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("CONTACT_ID");
		row.createCell(1).setCellValue("NAME");
		row.createCell(2).setCellValue("PRIMARY_EMAIL");
		row.createCell(3).setCellValue("SECONDARY_EMAIL");
		row.createCell(4).setCellValue("I_NUM");
		row.createCell(5).setCellValue("CONTACT_TYPE");

	}

}
