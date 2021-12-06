package com.data.report.datareport.modal;

import java.time.LocalDateTime;

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
@Document(collection="CAddress")
public class Address {
	
	@Id
	private String addressId;
	@Field
	private String contactId;
	@Field
	private int addressTypeId;
	@Field
	private String addressTypeDesc;
	@Field
	private String status;
	@Field
	private String addressLine_1;
	@Field
	private String addressLine_2;
	@Field
	private String addressLine_3;
	@Field
	private String city;
	@Field
	private String state;
	@Field
	private String zipcode;
	@Field
	private String poBox;
}
