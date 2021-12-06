package com.data.report.datareport.modal;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CustomerAssignment {

	@Id
	private String assignmentId;
	@Field
	private String cid;
	@Field
	private String contactId;
	@Field
	private String contactTypeCode;
	@Field
	private String levelCode;
	@Field
	private String levelValue;
	@Field
	private String status;
	@Field
	private String rollupCid;
	@Field
	private Integer levelCodeGroup;
	@Field
	private String firstName;
	@Field
	private String lastName;
	@Field
	private String contactTypeName;
	@Field
	private String contactCustomerName;
	@Field
	private String contactStatus;
	@Field
	private String middleName;
	
	private Contacts contacts;
	private List<Email> contactEmail;
	private List<Address> contactAddress;

	

}
