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
import com.data.report.datareport.modal.CustomerAssignment;
import com.data.report.datareport.modal.Email;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CCMReport {
	
	private static final String CCMREPORT_FILE_PATH = "C:/Work/Reports/CCMReport.xlsx";
	
	public void generateReport(MongoTemplate mongoTemplate) throws IOException{
		
//		Criteria contactsStatus = Criteria.where("status").is("A");
//		Criteria customerAssignmentStatus = Criteria.where("customerAssignment.status").is("A");
//		Criteria contactAddressStatus = Criteria.where("contactAddress.status").is("A");
//		Criteria contactEmailStatus = Criteria.where("contactEmail.status").is("A");
//		Criteria criteria3 = Criteria.where("contacts.status").is("A");
//		Criteria addCriteria = new Criteria().andOperator(contactsStatus,customerAssignmentStatus,contactAddressStatus,contactEmailStatus);
//      Criteria criteria = Criteria.where("cid").is("144091");
		
		Criteria status = Criteria.where("status").is("A");
		Criteria customerAssignmentLevelCode = Criteria.where("levelCode").is("ICA");
		Criteria contactsStatus = Criteria.where("contactStatus").is("A");
		
		MatchOperation match = Aggregation.match(new Criteria().andOperator(status, contactsStatus, customerAssignmentLevelCode));

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
				log.info("CCM Report rowid: " + rowid);
				XSSFRow row;
				int cellid = 0;
				row = spreadsheet.createRow(rowid++);
				Cell contactId = row.createCell(cellid++);
				Cell name = row.createCell(cellid++);
				Cell publicEmail = row.createCell(cellid++);
				Cell privateEmail = row.createCell(cellid++);
				Cell icanum = row.createCell(cellid++);
				Cell contactType = row.createCell(cellid++);
				Cell region = row.createCell(cellid++);
				contactId.setCellValue(key.getContactId());
				name.setCellValue(key.getFirstName() + " " + key.getLastName());
				icanum.setCellValue(Integer.parseInt(key.getLevelValue()));
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

			FileOutputStream out = new FileOutputStream(new File(CCMREPORT_FILE_PATH));
			workbook.write(out);
			out.close();
		}
		log.info("CCM Report created with Assignment count: " + results.size());
	}
	
	private void createHeadings(XSSFSheet spreadsheet) {
		XSSFRow row;
		row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("CCM_CONTACT_ID");
		row.createCell(1).setCellValue("NAME");
		row.createCell(2).setCellValue("PUBLIC_EMAIL_ADDR");
		row.createCell(3).setCellValue("PRIVATE_EMAIL_ADDR");
		row.createCell(4).setCellValue("ICA_NUM");
		row.createCell(5).setCellValue("CONTACT_TYPE");
		row.createCell(6).setCellValue("REGION_NAME");
		row.createCell(7).setCellValue("PROCESSOR_NUMBER");
	}

}
