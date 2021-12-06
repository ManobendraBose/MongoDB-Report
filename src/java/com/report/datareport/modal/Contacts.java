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
public class Contacts {

	@Id
	private String contactId;
	@Field
	private String salutation;
	@Field
	private String businessTitle;
	@Field
	private String firstName;
	@Field
	private String middleName;
	@Field
	private String lastName;
	@Field

	private List<CustomerAssignment> customerAssignment;
	private List<Email> contactEmail;
	private List<Phone> contactPhone;
	
//	private CustomerAssignment customerAssignment;
//	private Email contactEmail;
//	private Address contacAddress;
}
